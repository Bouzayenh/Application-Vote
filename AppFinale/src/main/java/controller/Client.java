package controller;

import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.*;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.FeedbackPaquet;
import dataobject.paquet.feedback.ResultatFeedbackPaquet;
import dataobject.paquet.feedback.VotesPaquet;
import datastatic.Chiffrement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Client() throws IOException, ClassNotFoundException, FeedbackException {
        // demande de connexion au serveur
        serveurSocket = new Socket("localhost", 2999);
        outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
        inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        outputServeur.writeObject(new IdentificationPaquet(IdentificationPaquet.Source.CLIENT));
    }

    public void authentification(String identifiant, String motDePasse) throws FeedbackException, IOException, ClassNotFoundException {
        outputServeur.writeObject(new AuthentificationPaquet(new Utilisateur(identifiant, motDePasse)));
        ((FeedbackPaquet) inputServeur.readObject()).throwException();
    }

    public void deconnexion() throws FeedbackException, IOException, ClassNotFoundException {
        outputServeur.writeObject(new DeconnexionPaquet());
        ((FeedbackPaquet) inputServeur.readObject()).throwException();
    }

    public void voter(int bulletin, int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        if (bulletin < 1 || bulletin > 2) throw new BulletinInvalideException();

        // chiffrement et envoi du bulletin
        outputServeur.writeObject(new DemanderClePubliquePaquet(idVote));
        ClePubliqueFeedbackPaquet clePaquet = (ClePubliqueFeedbackPaquet) inputServeur.readObject();
        clePaquet.throwException();
        outputServeur.writeObject(new BulletinPaquet(Chiffrement.encrypt(bulletin-1, clePaquet.getClePublique()), idVote));

        ((FeedbackPaquet) inputServeur.readObject()).throwException();
    }

    public Map<Integer, Vote> consulterVotes() throws FeedbackException, IOException, ClassNotFoundException {
        outputServeur.writeObject(new DemanderVotesPaquet());
        VotesPaquet votesPaquet = (VotesPaquet) inputServeur.readObject();
        votesPaquet.throwException();
        return votesPaquet.getVotes();
    }

    public Vote consulterResultats(int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        outputServeur.writeObject(new DemanderResultatPaquet(idVote));
        ResultatFeedbackPaquet resPaquet = (ResultatFeedbackPaquet) inputServeur.readObject();
        resPaquet.throwException();
        return resPaquet.getVote();
    }
}
