package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;

public class DechiffrerFeedbackPaquet extends FeedbackPaquet {
    private int somme;

    public DechiffrerFeedbackPaquet(int somme) {
        this(null);
        this.somme = somme;
    }

    public DechiffrerFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public int getSomme() {
        return somme;
    }
}
