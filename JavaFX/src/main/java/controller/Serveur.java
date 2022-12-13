package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import controller.communication.RecepteurConnexion;
import controller.database.IStockageServeur;
import controller.database.StockageServeurMySQL;
import controller.database.StockageServeurOracle;
import dataobject.Chiffre;
import dataobject.Mail;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.*;
import dataobject.paquet.*;
import dataobject.paquet.feedback.*;
import datastatic.Chiffrement;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Serveur {
    private Set<String> utilisateursAuthentifies;

    private ServerSocket serverSocket;
    private EmetteurConnexion scrutateur;
    private IStockageServeur stockageServeur;

    public Serveur() throws IOException, SQLException {
        utilisateursAuthentifies = new HashSet<>();
        serverSocket = new ServerSocket(3615);
        stockageServeur = new StockageServeurOracle();
    }

    public Set<Vote> consulterVotes() throws FeedbackException, SQLException {
        Set<Vote> votes = stockageServeur.getVotes();
        if (votes.size() == 0)
            throw new AucunVoteException();
        return votes;
    }

    public Vote consulterResultats(int idVote) throws FeedbackException, SQLException {
        Vote vote = stockageServeur.getVote(idVote);

        if (vote == null)
            throw new VoteInexistantException();
        if (!vote.estFini())
            throw new VoteNonTermineException();
        return vote;
    }

    public void creerVote(String intitule, String option1, String option2) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        scrutateur.ecrirePaquet(new CreerVotePaquet());
        CreerVoteFeedbackPaquet paquet = (CreerVoteFeedbackPaquet) scrutateur.lireFeedback();
        stockageServeur.creerVote(new Vote(paquet.getIdVote(), intitule, option1, option2).setUrne(paquet.getChiffre()));
    }

    public void terminerVote(int idVote) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        Vote vote = stockageServeur.getVote(idVote);
        int nbBulletins = vote.getNbBulletins();
        if (nbBulletins == 0)
            nbBulletins = 1;

        // récupère le résultat en clair
        scrutateur.ecrirePaquet(new DechiffrerPaquet(idVote, vote.getUrne(), nbBulletins));
        DechiffrerFeedbackPaquet paquet = (DechiffrerFeedbackPaquet) scrutateur.lireFeedback();
        stockageServeur.terminerVote(idVote, paquet.getResultat());

        // envoie un mail aux utilisateurs
        for (Utilisateur utilisateur : stockageServeur.getUtilisateurs()) {
            if (stockageServeur.aVote(utilisateur.getLogin(), idVote)) {
                try {
                    Mail mail = new Mail();
                    mail.envoyerMail("FinVote",utilisateur.getEmail(), stockageServeur.getVote(idVote));
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Set<Utilisateur> consulterUtilisateurs() throws FeedbackException, SQLException {
        Set<Utilisateur> utilisateurs = stockageServeur.getUtilisateurs();
        if (utilisateurs.size() == 0)
            throw new AucunUtilisateurException();
        return utilisateurs;
    }

    public void creerUtilisateur(String login, String motDePasse, String email) throws SQLException {
        stockageServeur.creerUtilisateur(new Utilisateur(login, motDePasse, email).hasherMotdePasse());
    }

    public void supprimerUtilisateur(String login) throws SQLException {
        stockageServeur.supprimerUtilisateur(login);
        deconnecter(login);
    }

    public void modifierUtilisateur(String login, String motDePasse, String email) throws SQLException {
        Utilisateur utilisateur = new Utilisateur(login, motDePasse, email).hasherMotdePasse();
        if (!Objects.equals(motDePasse, ""))
            stockageServeur.mettreAJourUtilisateurMotDePasse(utilisateur);
        if (!Objects.equals(email, ""))
            stockageServeur.mettreAJourUtilisateurEmail(utilisateur);
    }

    private synchronized void deconnecter(String login) {
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
                } catch (IOException ignored) {}
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
            } catch (IOException | ClassNotFoundException ignored) {}
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
                                if (estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new SessionDejaAuthentifieException());
                                else if (estAuthentifie(authPaquet.getUtilisateur().getLogin()))
                                    client.ecrireException(new UtilisateurDejaAuthentifieException());
                                else {
                                    if (!stockageServeur.verifierMotDePasse(authPaquet.getUtilisateur().hasherMotdePasse()))
                                        client.ecrireException(new AuthentificationException());
                                    else {
                                        idUtilisateurCourant = authPaquet.getUtilisateur().getLogin();
                                        authentifier();
                                        client.ecrireConfirmation();
                                    }
                                }
                                break;

                            case DECONNEXION:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    deconnecter(idUtilisateurCourant);
                                    client.ecrireConfirmation();
                                }
                                break;

                            case DEMANDER_CLE_PUBLIQUE:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else
                                    client.ecrirePaquet(demanderClePublique(((DemanderClePubliquePaquet) paquet).getIdVote()));
                                break;

                            case DEMANDER_VOTES:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Set<Vote> votes = stockageServeur.getVotes();
                                    if (votes.size() == 0)
                                        client.ecrireException(new AucunVoteException());
                                    else
                                        client.ecrirePaquet(new VotesPaquet(votes));
                                }
                                break;

                            case DEMANDER_RESULTAT:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Vote vote = stockageServeur.getVote(((DemanderResultatPaquet) paquet).getIdVote());
                                    if (!vote.estFini())
                                        client.ecrireException(new VoteNonTermineException());
                                    else
                                        client.ecrirePaquet(new ResultatFeedbackPaquet(vote));
                                }
                                break;

                            case BULLETIN:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    BulletinPaquet bulPaquet = (BulletinPaquet) paquet;
                                    if (stockageServeur.aVote(idUtilisateurCourant, bulPaquet.getIdVote()))
                                        client.ecrireException(new DejaVoteException());
                                    else {
                                        Vote vote = stockageServeur.getVote(bulPaquet.getIdVote());
                                        if (vote == null)
                                            client.ecrireException(new VoteInexistantException());
                                        else if (vote.estFini())
                                            client.ecrireException(new VoteTermineException());
                                        else {
                                            scrutateur.ecrirePaquet(bulPaquet);
                                            try {
                                                // agrège le bulletin dans l'urne
                                                ClePubliqueFeedbackPaquet clePaquet = (ClePubliqueFeedbackPaquet) scrutateur.lireFeedback();
                                                Chiffre bulletin = bulPaquet.getBulletin();
                                                stockageServeur.updateUrne(
                                                        bulPaquet.getIdVote(),
                                                        Chiffrement.agreger(bulletin, vote.getUrne(), clePaquet.getClePublique())
                                                );
                                                stockageServeur.voter(idUtilisateurCourant, bulPaquet.getIdVote());
                                                client.ecrireConfirmation();
                                                Mail mail = new Mail();
                                                mail.envoyerMail("Vote",stockageServeur.getUtilisateur(idUtilisateurCourant).getEmail(),stockageServeur.getVote(bulPaquet.getIdVote()));
                                            } catch (FeedbackException e) {
                                                client.ecrireException(e);
                                            } catch (MessagingException e) {
                                                throw new RuntimeException(e);
                                            } catch (GeneralSecurityException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                                break;
                        }
                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                        client.ecrireException(new ScrutateurDeconnecteException());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                deconnecter(idUtilisateurCourant);
            }
        }

        private synchronized ClePubliqueFeedbackPaquet demanderClePublique(int idVote) throws IOException, ClassNotFoundException {
            scrutateur.ecrirePaquet(new DemanderClePubliquePaquet(idVote));
            return (ClePubliqueFeedbackPaquet) scrutateur.transfererFeedback();
        }

        private synchronized void authentifier() {
            utilisateursAuthentifies.add(idUtilisateurCourant);
        }

        private synchronized boolean estAuthentifie(String login) {
            return login != null && utilisateursAuthentifies.contains(login);
        }
    }
}
