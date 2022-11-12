package controller;

import dataobject.Chiffre;
import dataobject.ClePublique;
import dataobject.Utilisateur;
import dataobject.Vote;
import datastatic.Chiffrement;
import datastatic.Requete;
import exception.AuthentificationRefuseeException;
import exception.BulletinInvalideException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Client() throws IOException {
        // demande de connexion au serveur
        serveurSocket = new Socket("localhost", 2999);
        outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
        inputServeur = new ObjectInputStream(serveurSocket.getInputStream());
    }

    public void authentification(String login, String mdp) throws IOException, ClassNotFoundException, AuthentificationRefuseeException {
        // demande d'authentification au serveur
        outputServeur.writeObject(Requete.CLIENT_CONNEXION);
        outputServeur.writeObject(new Utilisateur(login, mdp));

        // lèvement d'erreur : authentification refusée
        if (!(boolean) inputServeur.readObject()) throw new AuthentificationRefuseeException();
    }

    public ClePublique demanderClePublique() throws IOException, ClassNotFoundException {
        outputServeur.writeObject(Requete.CLIENT_DEMANDER_CLE_PUBLIQUE);
        return (ClePublique) inputServeur.readObject();
    }

    public Vote consulterVoteEnCours() throws IOException, ClassNotFoundException {
        outputServeur.writeObject(Requete.CLIENT_DEMANDER_VOTE_EN_COURS);
        return (Vote) inputServeur.readObject();
    }

    public void voter(int bulletin) throws IOException, ClassNotFoundException, BulletinInvalideException {
        // lèvement d'erreur : bulletin hors du bon intervalle
        if (bulletin < 1 || bulletin > 2) throw new BulletinInvalideException();
        else {
            // chiffrement du bulletin
            ClePublique clePublique = demanderClePublique();
            Chiffre chiffre = Chiffrement.encrypt(bulletin-1, clePublique);

            // demande à voter au serveur
            outputServeur.writeObject(Requete.CLIENT_VOTER);
            outputServeur.writeObject(chiffre);

            // lèvement d'erreur : bulletin jugé invalide par le serveur
            if (!(boolean) inputServeur.readObject()) throw new BulletinInvalideException();
        }
    }

    public void deconnexion() throws IOException {
        outputServeur.writeObject(Requete.CLIENT_DECONNEXION);
    }
}