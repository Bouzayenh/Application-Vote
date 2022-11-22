package controller;

import controller.database.CBDScrutateur;
import dataobject.ClePublique;
import dataobject.exception.ConnexionBaseDeDonneeException;
import dataobject.exception.FeedbackException;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.FeedbackPaquet;
import dataobject.paquet.feedback.DechiffrerFeedbackPaquet;
import datastatic.Chiffrement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.SQLException;

public class Scrutateur {

    private int l;

    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;
    private CBDScrutateur connexionBD;

    public Scrutateur(int l) throws IOException, ClassNotFoundException, SQLException, FeedbackException {
        this.l = l;

        // demande de connexion au serveur
        serveurSocket = new Socket("localhost", 2999);

        outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());

        inputServeur = new ObjectInputStream(serveurSocket.getInputStream());


        outputServeur.writeObject(new IdentificationPaquet(IdentificationPaquet.Source.SCRUTATEUR));



        connexionBD = new CBDScrutateur();
    }

    public void run() {
        while (true) {
            try {
                // attend un paquet du serveur
                Paquet paquet = (Paquet) inputServeur.readObject();

                // traîte le paquet
                switch (paquet.getType()) {

                    case DEMANDER_CLE_PUBLIQUE:
                        DemanderClePubliquePaquet demanderClePubliquePaquet = (DemanderClePubliquePaquet) paquet;

                        ClePublique clePublique = connexionBD.getClePublique(demanderClePubliquePaquet.getIdVote());

                        outputServeur.writeObject(new ClePubliqueFeedbackPaquet(clePublique));
                        break;

                    case CREER_VOTE:
                        CreerVotePaquet votePaquet = (CreerVotePaquet) paquet;
                        BigInteger[] cles = Chiffrement.keygen(l);

                        FeedbackException exception = null;

                        try {
                            connexionBD.insererCles(votePaquet.getVote().getIdentifiant(), cles[0], cles[1], cles[2], cles[3]);
                        } catch (SQLException e) {
                            exception = new ConnexionBaseDeDonneeException();
                        }

                        outputServeur.writeObject(new FeedbackPaquet(exception));
                        break;

                    case DECHIFFRER:
                        DechiffrerPaquet dechPaquet = (DechiffrerPaquet) paquet;
                        clePublique = connexionBD.getClePublique(dechPaquet.getIdVote());
                        
                        BigInteger clePrivee = connexionBD.getClePrivee(dechPaquet.getIdVote());
                        int resultat = Chiffrement.decrypt(dechPaquet.getChiffre(), clePublique, clePrivee);
                        outputServeur.writeObject(new DechiffrerFeedbackPaquet(resultat));
                        break;
                }

            } catch (IOException | ClassNotFoundException ignored) {} catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}