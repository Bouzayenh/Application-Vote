package dataobject.paquet.feedback;

import dataobject.Chiffre;
import dataobject.exception.FeedbackException;

public class CreerVoteFeedbackPaquet extends FeedbackPaquet {
    private int idVote;
    private Chiffre chiffre;

    public CreerVoteFeedbackPaquet(int idVote, Chiffre chiffre) {
        this((FeedbackException) null);
        this.idVote = idVote;
        this.chiffre = chiffre;
    }

    public CreerVoteFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public int getIdVote() {
        return idVote;
    }

    public Chiffre getChiffre() {
        return chiffre;
    }
}
