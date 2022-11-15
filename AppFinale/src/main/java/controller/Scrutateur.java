package controller;

import dataobject.ClePublique;
import dataobject.exception.FeedbackException;
import dataobject.paquet.CreerVotePaquet;
import dataobject.paquet.DechiffrerPaquet;
import dataobject.paquet.Paquet;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.FeedbackPaquet;
import dataobject.paquet.feedback.DechiffrerFeedbackPaquet;
import datastatic.Chiffrement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class Scrutateur {

    private int l;

    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Scrutateur(int l) throws IOException {
        this.l = l;

        // demande de connexion au serveur
        serveurSocket = new Socket("localhost", 2999);
        outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
        inputServeur = new ObjectInputStream(serveurSocket.getInputStream());
    }

    public void run() {
        while (true) {
            try {
                // attend un paquet du serveur
                Paquet paquet = (Paquet) inputServeur.readObject();

                // traîte le paquet
                switch (paquet.getType()) {
                    case DEMANDER_CLE_PUBLIQUE:
                        // TODO récupérer sur la base de données : clePublique
                        ClePublique clePublique = null; // placeholder

                        outputServeur.writeObject(new ClePubliqueFeedbackPaquet(clePublique));
                        break;

                    case CREER_VOTE:
                        CreerVotePaquet votePaquet = (CreerVotePaquet) paquet;
                        BigInteger[] cles = Chiffrement.keygen(l);
                        clePublique = new ClePublique(cles[0], cles[1], cles[2]);
                        BigInteger clePrivee = cles[3];
                        // TODO envoyer clePublique, clePrivee à base de données, qui mets à jour votePaquet.getVote()
                        // TODO récupérer sur la base de données : exception
                        FeedbackException exception = null; // placeholder

                        outputServeur.writeObject(new FeedbackPaquet(exception));
                        break;

                    case DECHIFFRER:
                        DechiffrerPaquet dechPaquet = (DechiffrerPaquet) paquet;
                        // TODO récupérer sur base de données : clePublique, clePrivee correspondants à dechPaquet.getIdVote()
                        clePublique = null; // placeholder
                        clePrivee = null; // placeholder

                        int resultat = Chiffrement.decrypt(dechPaquet.getChiffre(), clePublique, clePrivee);
                        outputServeur.writeObject(new DechiffrerFeedbackPaquet(resultat));
                        break;
                }

            } catch (IOException | ClassNotFoundException ignored) {}
        }
    }
}