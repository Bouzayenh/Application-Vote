package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import controller.config.Conf;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.BulletinInvalideException;
import dataobject.exception.FeedbackException;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.UtilisateurFeedbackPaquet;
import dataobject.paquet.feedback.ResultatFeedbackPaquet;
import dataobject.paquet.feedback.VotesPaquet;
import dataobject.Chiffrement;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class Client {
    private EmetteurConnexion serveur;

    public Client() throws IOException {

        if (Conf.UTILISE_SSL){

            System.setProperty("javax.net.ssl.trustStore", "JavaFX/src/main/resources/ssl/saeTrustStore.jts");
            System.setProperty("javax.net.ssl.trustStorePassword", "caracal");

            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            serveur = new EmetteurConnexion((SSLSocket) sslSocketFactory.createSocket("localhost", Conf.PORT));
        }else {
            serveur = new EmetteurConnexion(new Socket("localhost", Conf.PORT));
        }

        // identification
        serveur.ecrirePaquet(new IdentificationPaquet(Connexion.Source.CLIENT));
    }

    public List<Vote> consulterVotesEnCours() throws FeedbackException, IOException, ClassNotFoundException {
        List<Vote> votes = new ArrayList<>(consulterVotes());
        votes.removeIf(Vote::estFini);
        votes.sort(Comparator.comparingInt(Vote::getIdentifiant));
        return votes;
    }

    public List<Vote> consulterVotesFinis() throws FeedbackException, IOException, ClassNotFoundException {
        List<Vote> votes = new ArrayList<>(consulterVotes());
        votes.removeIf(Predicate.not(Vote::estFini));
        votes.sort(Comparator.comparingInt(Vote::getIdentifiant));
        return votes;
    }

    public void authentification(String identifiant, String motDePasse) throws FeedbackException, IOException, ClassNotFoundException {
        serveur.ecrirePaquet(new AuthentificationPaquet(new Utilisateur(identifiant, motDePasse)));
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

    public void changerMotDePasse(String login, String ancienMotDePasse, String nouveauMotDePasse) throws IOException, FeedbackException, ClassNotFoundException {
        serveur.ecrirePaquet(new ChangerMotDePassePaquet(
                new Utilisateur(login, ancienMotDePasse, ""),
                nouveauMotDePasse
        ));

        serveur.lireFeedback();
    }

    public Utilisateur demandeUtilisateur() throws IOException, FeedbackException, ClassNotFoundException {
        serveur.ecrirePaquet(new DemanderUtilisateurPaquet());
        return ((UtilisateurFeedbackPaquet) serveur.lireFeedback()).getUtilisateur();
    }
}
