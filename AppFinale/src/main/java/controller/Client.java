package controller;

import dataobject.Chiffre;
import dataobject.ClePublique;
import dataobject.Utilisateur;
import dataobject.Vote;
import datastatic.Chiffrement;
import datastatic.Requete;

import java.io.*;
import java.net.Socket;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Client {
    private SecureRandom random;
    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;
    private Utilisateur utilisateur;

    public Client() {
        try {
            random = new SecureRandom();

            // demande de connexion au serveur
            serveurSocket = new Socket("localhost", 2999);
            outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
            inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        } catch (IOException ignored) {}
    }

    public boolean estConnecte() {
        return serveurSocket != null && !serveurSocket.isClosed();
    }

    public void voter() {
        try {
            // entrée du bulletin
            Scanner sc = new Scanner(System.in);
            int bulletin = 0;
            while (bulletin != 1 && bulletin != 2) {
                System.out.println("Entrez le numéro de l'option souhaitée : 1 ou 2");
               
                bulletin = sc.nextInt();
                if (bulletin != 1 && bulletin != 2) System.out.println("Sélection incorrecte");
            }
            ClePublique clePublique = demanderClePublique();
            Chiffre chiffre = Chiffrement.encrypt(bulletin-1, clePublique);

            outputServeur.writeObject(Requete.CLIENT_VOTER);
            outputServeur.writeObject(chiffre);

        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public void deconnexion() {
        try {
            outputServeur.writeObject(Requete.CLIENT_DEMANDER_DECONNEXION);
            System.out.println("Vous avez été deconnecté");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void consulterVoteEnCours() {
        try {
            outputServeur.writeObject(Requete.CLIENT_DEMANDER_VOTE_EN_COURS);
            Vote vote = (Vote) inputServeur.readObject();
            System.out.println(
                    "Intitulé : " + vote.getIntitule() +
                            "\n1 - " + vote.getOption1() +
                            "\n2 - " + vote.getOption2()
            );

        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public ClePublique demanderClePublique() throws IOException, ClassNotFoundException {
        outputServeur.writeObject(Requete.CLIENT_DEMANDER_CLE_PUBLIQUE);
        return (ClePublique) inputServeur.readObject();
    }

    public boolean connexion(String login, String mdp){
        try {
            //envoyer les informations de connexion
            outputServeur.writeObject(Requete.CLIENT_DEMANDER_CONNEXION);
            outputServeur.writeObject(new Utilisateur(login, mdp));
            
            //lire la réponse du serveur
            return (Boolean) inputServeur.readObject();
        } catch (IOException | ClassNotFoundException ignored) {
            return false;
        }
    }
}