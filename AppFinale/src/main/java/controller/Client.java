package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.BulletinInvalideException;
import dataobject.exception.FeedbackException;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.ResultatFeedbackPaquet;
import dataobject.paquet.feedback.VotesPaquet;
import datastatic.Chiffrement;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class Client {
    private EmetteurConnexion serveur;

    public Client() throws IOException {
        serveur = new EmetteurConnexion(new Socket("localhost", 2999));
        serveur.ecrirePaquet(new IdentificationPaquet(Connexion.Source.CLIENT));
    }

    public void authentification(String identifiant, String motDePasse) throws FeedbackException, IOException, ClassNotFoundException {
        serveur.ecrirePaquet(new AuthentificationPaquet(new Utilisateur(identifiant, motDePasse).hasherMotdePasse()));
        serveur.lireFeedback();
    }

    public void deconnexion() throws FeedbackException, IOException, ClassNotFoundException {
        serveur.ecrirePaquet(new DeconnexionPaquet());
        serveur.lireFeedback();
    }

    public void voter(int bulletin, int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        if (bulletin != 1 && bulletin != 2) throw new BulletinInvalideException();

        // chiffrement et envoi du bulletin
        serveur.ecrirePaquet(new DemanderClePubliquePaquet(idVote));
        ClePubliqueFeedbackPaquet paquet = (ClePubliqueFeedbackPaquet) serveur.lireFeedback();
        serveur.ecrirePaquet(new BulletinPaquet(Chiffrement.encrypt(bulletin-1, paquet.getClePublique()), idVote));
        serveur.lireFeedback();
    }

    public Set<Vote> consulterVotes() throws FeedbackException, IOException, ClassNotFoundException {
        serveur.ecrirePaquet(new DemanderVotesPaquet());
        return ((VotesPaquet) serveur.lireFeedback()).getVotes();
    }

    public Vote consulterResultats(int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        serveur.ecrirePaquet(new DemanderResultatPaquet(idVote));
        return ((ResultatFeedbackPaquet) serveur.lireFeedback()).getVote();
    }
}
