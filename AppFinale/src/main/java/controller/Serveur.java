package controller;

import controller.database.CBDServeur;
import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.*;
import dataobject.paquet.*;
import dataobject.paquet.feedback.*;
import datastatic.Chiffrement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Serveur {

    private ArrayList<String> utilisateursAuthentifies;

    private ServerSocket serverSocket;

    private Socket socketScrutateur;
    private ObjectOutputStream outputScrutateur;
    private ObjectInputStream inputScrutateur;

    private CBDServeur connexionBD;
    public Serveur() throws IOException, SQLException {
        utilisateursAuthentifies = new ArrayList<>();

        // ouvre le serveur
        serverSocket = new ServerSocket(2999);

        connexionBD = new CBDServeur();
    }

    public void run() {
        new Thread(() -> { // thread d'attente de connexions
            while (true) {
                try {
                    // attend une connexion et la traite séparément afin d'écouter de nouveau
                    Socket socket = serverSocket.accept();
                    new ThreadGestionConnexion(socket).start();
                } catch (IOException e) {
                    e.printStackTrace(); // TODO DEBUG, sera ignored
                }
            }
        }).start();
    }

    public synchronized boolean estConnecteScrutateur() {
        try {
            outputScrutateur.writeObject(new HeartbeatPaquet());
            int timeout = socketScrutateur.getSoTimeout();
            socketScrutateur.setSoTimeout(1000);
            inputScrutateur.readObject();
            socketScrutateur.setSoTimeout(timeout);
            return true;
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            return false;
        }
    }

    public void creerVote(String intitule, String option1, String option2) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        Vote vote = new Vote.VoteBuilder()
                .informations(intitule, option1, option2)
                .build();

        //crée le vote et conserve l'id du vote
        int idVote = connexionBD.creerVote(vote);

        Vote voteAvecId = new Vote.VoteBuilder()
                .informations(intitule, option1, option2)
                .identifiant(idVote)
                        .build();

        outputScrutateur.writeObject(new CreerVotePaquet(voteAvecId));
        ((FeedbackPaquet) inputScrutateur.readObject()).throwException();
    }

    public void terminerVote(int idVote) throws FeedbackException, IOException, ClassNotFoundException, SQLException {
        // TODO récupérer sur la base de données : urne, nbBulletins correspondant à idVote

        Chiffre urne = connexionBD.getResultatsChiffresVote(idVote); // a vérifier si c'est ca

        int nbBulletins = connexionBD.getNbBulletinsVote(idVote);

        outputScrutateur.writeObject(new DechiffrerPaquet(urne, idVote));
        DechiffrerFeedbackPaquet dechPaquet = (DechiffrerFeedbackPaquet) inputScrutateur.readObject();
        dechPaquet.throwException();

        // TODO envoyer dechPaquet.getResultat(), idVote à la base de données, qui mets à jour le résultat en clair du vote correspondant à idVote
        connexionBD.mettreAJourResultatclaire(idVote,dechPaquet.getResultat());
    }

    public Map<Integer, Vote> consulterVotes() throws FeedbackException {
        // TODO récupérer sur la base de données : votes (tous)
        Map<Integer, Vote> votes = null; // placeholder

        if (votes.size() == 0)
            throw new AucunVoteException();
        return votes;
    }

    public Vote consulterResultats(int idVote) throws FeedbackException {
        // TODO récupérer sur la base de données : vote correspondant à idVote
        Vote vote = null; // placeholder

        if (!vote.estFini())
            throw new VoteNonTermineException();
        else
            return vote;
    }

    public void creerUtilisateur(String identifiant, String motDePasse) throws SQLException {
        connexionBD.creerUtilisateur(new Utilisateur(identifiant, motDePasse));
    }

    private class ThreadGestionConnexion extends Thread {
        Socket socket;

        public ThreadGestionConnexion(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                // identifie l'auteur de la connexion
                IdentificationPaquet paquet = (IdentificationPaquet) input.readObject();

                // gère la connexion selon l'identité de l'auteur
                switch (paquet.getSource()) {

                    case CLIENT:
                        ThreadConnexionVersClient connexionVersClient = new ThreadConnexionVersClient(socket, input, output);
                        connexionVersClient.start();
                        output.writeObject(new FeedbackPaquet());
                        break;

                    case SCRUTATEUR:
                        // attribue la connexion s'il n'y pas déjà une connexion scrutateur
                        if (estConnecteScrutateur()) {
                            output.writeObject(new FeedbackPaquet(new ScrutateurDejaConnecteException()));
                        }
                        else {
                            socketScrutateur = socket;
                            outputScrutateur = output;
                            inputScrutateur = input;
                            output.writeObject(new FeedbackPaquet());
                        }
                        break;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class ThreadConnexionVersClient extends Thread {

        Socket socketClient;
        ObjectOutputStream outputClient;
        ObjectInputStream inputClient;
        String idUtilisateurCourant;

        public ThreadConnexionVersClient(Socket socket, ObjectInputStream input, ObjectOutputStream output) throws IOException {
            this.socketClient = socket;
            this.inputClient = input;
            this.outputClient = output;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // attend un paquet du client
                    Paquet paquet = (Paquet) inputClient.readObject();
                    try {
                        // traîte le paquet
                        switch (paquet.getType()) {
                            // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                            case AUTHENTIFICATION:
                                AuthentificationPaquet authPaquet = (AuthentificationPaquet) paquet;
                                idUtilisateurCourant = authPaquet.getUtilisateur().getIdentifiant();
                                if (estAuthentifie())
                                    outputClient.writeObject(new FeedbackPaquet(new UtilisateurDejaConnecteException()));
                                else {
                                    // TODO envoyer authPaquet.getUtilisateur() à la base de données, qui vérifie si l'utilisateur existe
                                    // TODO récupérer sur la base de données : exception
                                    FeedbackException exception = null; // placeholder

                                    FeedbackPaquet feedback = new FeedbackPaquet(exception);

                                    if (feedback.estPositif())
                                        authentification();
                                    outputClient.writeObject(feedback);
                                    break;
                                }

                            case DECONNEXION:
                                if (!estAuthentifie())
                                    outputClient.writeObject(new FeedbackPaquet(new UtilisateurDeconnecteException()));
                                else {
                                    deconnexion();
                                    outputClient.writeObject(new FeedbackPaquet());
                                }
                                break;

                            case DEMANDER_CLE_PUBLIQUE:
                                if (!estAuthentifie())
                                    outputClient.writeObject(new ClePubliqueFeedbackPaquet(new UtilisateurDeconnecteException()));
                                else
                                    outputClient.writeObject(demanderClePublique(((DemanderClePubliquePaquet) paquet).getIdVote()));
                                break;

                            case DEMANDER_VOTES:
                                if (!estAuthentifie())
                                    outputClient.writeObject(new VotesPaquet(new UtilisateurDeconnecteException()));
                                else {
                                    // TODO récupérer sur la base de données : votes (tous)
                                    Map<Integer, Vote> votes = null; // placeholder

                                    if (votes.size() == 0)
                                        outputClient.writeObject(new VotesPaquet(new AucunVoteException()));
                                    else
                                        outputClient.writeObject(new VotesPaquet(votes));
                                }
                                break;

                            case DEMANDER_RESULTAT:
                                if (!estAuthentifie())
                                    outputClient.writeObject(new ResultatFeedbackPaquet(new UtilisateurDeconnecteException()));
                                else {
                                    DemanderResultatPaquet resPaquet = (DemanderResultatPaquet) paquet;
                                    // TODO récupérer sur la base de données : vote correspondant à resPaquet.getIdVote()
                                    Vote vote = null; // placeholder

                                    if (!vote.estFini())
                                        outputClient.writeObject(new ResultatFeedbackPaquet(new VoteNonTermineException()));
                                    else
                                        outputClient.writeObject(new ResultatFeedbackPaquet(vote));
                                }

                            case BULLETIN:
                                if (!estAuthentifie())
                                    outputClient.writeObject(new FeedbackPaquet(new UtilisateurDeconnecteException()));
                                else {
                                    BulletinPaquet bulPaquet = (BulletinPaquet) paquet;
                                    // TODO récupérer sur la base de données : urne correspondant à bulPaquet.getIdVote()
                                    Chiffre urne = null; // placeholder

                                    if (urne == null)
                                        outputClient.writeObject(new FeedbackPaquet(new VoteInexistantException()));
                                    else {
                                        // TODO vérifier que le bulletin est bien un 0 ou un 1
                                        ClePubliqueFeedbackPaquet clePaquet = demanderClePublique(((BulletinPaquet) paquet).getIdVote());
                                        if (!clePaquet.estPositif())
                                            outputClient.writeObject(clePaquet);
                                        else {
                                            // agrège le bulletin dans l'urne
                                            Chiffre bulletin = ((BulletinPaquet) paquet).getBulletin();
                                            urne = Chiffrement.agreger(bulletin, urne, clePaquet.getClePublique());

                                            // TODO envoyer urne, bulPaquet.getIdVote() à la base de données, qui la met à jour
                                            outputClient.writeObject(new FeedbackPaquet());
                                        }
                                    }
                                }
                                break;
                        }

                    } catch (IOException | ClassNotFoundException e) {
                        if (!estConnecteScrutateur())
                            switch (paquet.getType()) {
                                // différencier les héritiers de FeedbackPaquet
                                case DEMANDER_CLE_PUBLIQUE -> outputClient.writeObject(new ClePubliqueFeedbackPaquet(new ScrutateurDeconnecteException()));
                                default -> outputClient.writeObject(new FeedbackPaquet(new ScrutateurDeconnecteException()));
                            }
                        else
                            outputClient.writeObject(0);
                    }

                } catch (IOException | ClassNotFoundException ignored) {}
            }
        }

        private synchronized ClePubliqueFeedbackPaquet demanderClePublique(int idVote) throws IOException, ClassNotFoundException {
            outputScrutateur.writeObject(new DemanderClePubliquePaquet(idVote));
            return (ClePubliqueFeedbackPaquet) inputScrutateur.readObject();
        }

        private synchronized boolean estAuthentifie() {
            return idUtilisateurCourant != null && utilisateursAuthentifies.contains(idUtilisateurCourant);
        }

        private synchronized void authentification() {
            utilisateursAuthentifies.add(idUtilisateurCourant);
        }

        private synchronized void deconnexion() {
            utilisateursAuthentifies.remove(idUtilisateurCourant);
        }
    }
}