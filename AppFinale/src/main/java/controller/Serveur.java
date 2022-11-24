package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import controller.communication.RecepteurConnexion;
import controller.database.ServeurCBDD;
import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.*;
import dataobject.paquet.*;
import dataobject.paquet.feedback.*;
import datastatic.Chiffrement;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Serveur {
    private ArrayList<String> utilisateursAuthentifies;

    private ServerSocket serverSocket;
    private EmetteurConnexion scrutateur;
    private ServeurCBDD connexionBDD;

    public Serveur() throws IOException, SQLException {
        utilisateursAuthentifies = new ArrayList<>();
        serverSocket = new ServerSocket(2999);
        connexionBDD = new ServeurCBDD();
    }

    public Set<Vote> consulterVotes() throws FeedbackException, SQLException {
        Set<Vote> votes = connexionBDD.selectVotes();
        if (votes.size() == 0)
            throw new AucunVoteException();
        return votes;
    }

    public Vote consulterResultats(int idVote) throws FeedbackException, SQLException {
        Vote vote = connexionBDD.selectVote(idVote);

        if (!vote.estFini())
            throw new VoteNonTermineException();
        else
            return vote;
    }

    public void creerVote(String intitule, String option1, String option2) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        scrutateur.ecrirePaquet(new CreerVotePaquet(connexionBDD.insertVote(new Vote(intitule, option1, option2))));
        scrutateur.lireFeedback();
    }

    public void terminerVote(int idVote) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        Vote vote = connexionBDD.selectVote(idVote);
        double nbBulletins = vote.getNbBulletins();
        if (nbBulletins == 0)
            nbBulletins = 1;

        // récupère le résultat en clair
        scrutateur.ecrirePaquet(new DechiffrerPaquet(idVote, vote.getUrne()));
        DechiffrerFeedbackPaquet paquet = (DechiffrerFeedbackPaquet) scrutateur.lireFeedback();

        connexionBDD.terminerVote(idVote, paquet.getSomme() / nbBulletins);
    }

    public Set<Utilisateur> consulterUtilisateurs() throws FeedbackException, SQLException {
        Set<Utilisateur> utilisateurs = connexionBDD.selectUtilisateurs();
        if (utilisateurs.size() == 0)
            throw new AucunUtilisateurException();
        return utilisateurs;
    }

    public void creerUtilisateur(String login, String motDePasse, String email) throws SQLException {
        connexionBDD.insertUtilisateur(new Utilisateur(login, motDePasse, email).hasherMotdePasse());
    }

    public void supprimerUtilisateur(String login) throws SQLException {
        connexionBDD.deleteUtilisateur(login);
        deconnexion(login);
    }

    public void modifierUtilisateur(String login, String motDePasse, String email) throws SQLException {
        Utilisateur utilisateur = new Utilisateur(login, motDePasse, email).hasherMotdePasse();
        if (!Objects.equals(motDePasse, ""))
            connexionBDD.updateUtilisateurMotDePasse(utilisateur);
        if (!Objects.equals(email, ""))
            connexionBDD.updateUtilisateurEmail(utilisateur);
    }

    private synchronized void deconnexion(String login) {
        utilisateursAuthentifies.remove(login);
    }

    public synchronized boolean estConnecteScrutateur() {
        try {
            scrutateur.ecrirePaquet(new HeartbeatPaquet());
            scrutateur.lirePaquet();
            return true;
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            return false;
        }
    }

    public void run() {
        new Thread(() -> {
            while (true) {
                try {
                    // attend une connexion et la traite séparément afin d'écouter de nouveau
                    new ThreadGestionConnexion(new Connexion(serverSocket.accept())).start();
                } catch (IOException e) {
                    e.printStackTrace(); // TODO DEBUG, sera ignored
                }
            }
        }).start();
    }

    private class ThreadGestionConnexion extends Thread {
        Connexion connexion;

        public ThreadGestionConnexion(Connexion connexion) {
            this.connexion = connexion;
        }

        @Override
        public void run() {
            try {
                // gère la connexion selon l'identité de l'auteur de la connexion
                switch (((IdentificationPaquet) connexion.lirePaquet()).getIdentification()) {

                    case CLIENT:
                        new ThreadConnexionClient(new RecepteurConnexion(connexion)).start();
                        break;

                    case SCRUTATEUR:
                        // attribue la connexion s'il n'y pas déjà une connexion scrutateur
                        if (estConnecteScrutateur())
                            connexion.ecrirePaquet(new DeconnexionPaquet());
                        else
                            scrutateur = new EmetteurConnexion(connexion);
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace(); // TODO DEBUG, sera ignored
            }
        }
    }

    private class ThreadConnexionClient extends Thread {
        private RecepteurConnexion client;
        private String idUtilisateurCourant;

        public ThreadConnexionClient(RecepteurConnexion client) throws IOException {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // attend un paquet du client
                    Paquet paquet = client.lirePaquet();
                    try {
                        // traîte le paquet
                        switch (paquet.getType()) {
                            // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                            case AUTHENTIFICATION:
                                AuthentificationPaquet authPaquet = (AuthentificationPaquet) paquet;
                                idUtilisateurCourant = authPaquet.getUtilisateur().getLogin();
                                if (estAuthentifie())
                                    client.ecrireException(new UtilisateurDejaConnecteException());
                                else {
                                    if (!connexionBDD.authentifier(authPaquet.getUtilisateur()))
                                        client.ecrireException(new ConnexionBDDException());
                                    else {
                                        authentification();
                                        client.ecrireConfirmation();
                                    }
                                    break;
                                }

                            case DECONNEXION:
                                if (!estAuthentifie())
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    deconnexion(idUtilisateurCourant);
                                    client.ecrireConfirmation();
                                }
                                break;

                            case DEMANDER_CLE_PUBLIQUE:
                                if (!estAuthentifie())
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else
                                    client.ecrirePaquet(demanderClePublique(((DemanderClePubliquePaquet) paquet).getIdVote()));
                                break;

                            case DEMANDER_VOTES:
                                if (!estAuthentifie())
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Set<Vote> votes = connexionBDD.selectVotes();
                                    if (votes.size() == 0)
                                        client.ecrireException(new AucunVoteException());
                                    else
                                        client.ecrirePaquet(new VotesPaquet(votes));
                                }
                                break;

                            case DEMANDER_RESULTAT:
                                if (!estAuthentifie())
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Vote vote = connexionBDD.selectVote(((DemanderResultatPaquet) paquet).getIdVote());
                                    if (!vote.estFini())
                                        client.ecrireException(new VoteNonTermineException());
                                    else
                                        client.ecrirePaquet(new ResultatFeedbackPaquet(vote));
                                }

                            case BULLETIN:
                                if (!estAuthentifie())
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    BulletinPaquet bulPaquet = (BulletinPaquet) paquet;
                                    // TODO vérifier que l'utilisateur n'a pas déjà voté
                                    Vote vote = connexionBDD.selectVote(bulPaquet.getIdVote());
                                    if (vote == null)
                                        client.ecrireException(new VoteInexistantException());
                                    else {
                                        // TODO vérifier que le bulletin est bien un 0 ou un 1
                                        ClePubliqueFeedbackPaquet clePaquet = demanderClePublique(bulPaquet.getIdVote());
                                        if (clePaquet.getException() != null)
                                            client.ecrireException(clePaquet.getException());
                                        else {
                                            // agrège le bulletin dans l'urne
                                            Chiffre bulletin = bulPaquet.getBulletin();
                                            connexionBDD.updateUrneEtNbBulletins(
                                                    bulPaquet.getIdVote(),
                                                    Chiffrement.agreger(bulletin, vote.getUrne(), clePaquet.getClePublique())
                                            );
                                            client.ecrireConfirmation();
                                        }
                                    }
                                }
                                break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        client.ecrireException(new ScrutateurDeconnecteException());
                    } catch (SQLException e) {
                        client.ecrireException(new ConnexionBDDException());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                deconnexion(idUtilisateurCourant);
            }
        }

        private synchronized ClePubliqueFeedbackPaquet demanderClePublique(int idVote) throws IOException, ClassNotFoundException {
            scrutateur.ecrirePaquet(new DemanderClePubliquePaquet(idVote));
            return (ClePubliqueFeedbackPaquet) scrutateur.transfererFeedback();
        }

        private synchronized boolean estAuthentifie() {
            return idUtilisateurCourant != null && utilisateursAuthentifies.contains(idUtilisateurCourant);
        }

        private synchronized void authentification() {
            utilisateursAuthentifies.add(idUtilisateurCourant);
        }
    }
}