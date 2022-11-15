package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;

public class DechiffrerFeedbackPaquet extends FeedbackPaquet {

    private int resultat;

    public DechiffrerFeedbackPaquet(int resultat) {
        this(null);
        this.resultat = resultat;
    }

    public DechiffrerFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public int getResultat() {
        return resultat;
    }
}
