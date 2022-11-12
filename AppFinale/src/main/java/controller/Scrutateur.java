package controller;

import dataobject.Chiffre;
import dataobject.ClePublique;
import datastatic.Chiffrement;
import datastatic.Requete;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class Scrutateur {
    private int l;
    // clé publique
    private ClePublique clePublique;
    // clé privée
    private BigInteger clePrivee;

    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Scrutateur(int l) {
        try {
            this.l = l;

            // demande de connexion au serveur
            serveurSocket = new Socket("localhost", 2999);
            outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
            inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        } catch (IOException e) {
            connexionIntrouvable();
        }
    }

    public void connexionIntrouvable() {
        System.out.println("Impossible de se connecter au serveur");
    }

    public void run() {
        try {
            while (true) {
                // attend une requête du serveur
                Requete requete = (Requete) inputServeur.readObject();

                // traîte la requête
                switch (requete) {
                    case SERVEUR_DEMANDER_CLE_PUBLIQUE:
                        outputServeur.writeObject(clePublique);
                        break;
                    case SERVEUR_CREER_VOTE:
                        BigInteger[] cles = Chiffrement.keygen(l);
                        clePublique = new ClePublique(cles[0], cles[1], cles[2]);
                        clePrivee = cles[3];
                        break;
                    case SERVEUR_DEMANDER_DECHIFFREMENT:
                        outputServeur.writeObject(Chiffrement.decrypt((Chiffre) inputServeur.readObject(), clePublique, clePrivee));
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            connexionIntrouvable();
        }
    }
}