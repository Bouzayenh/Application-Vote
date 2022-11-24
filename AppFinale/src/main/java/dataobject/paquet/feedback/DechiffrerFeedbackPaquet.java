package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;

public class DechiffrerFeedbackPaquet extends FeedbackPaquet {
    private double resultat;

    public DechiffrerFeedbackPaquet(double resultat) {
        this(null);
        this.resultat = resultat;
    }

    public DechiffrerFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public double getResultat() {
        return resultat;
    }
}
