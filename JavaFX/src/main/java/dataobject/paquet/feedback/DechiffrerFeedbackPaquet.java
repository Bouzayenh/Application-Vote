package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;

/**
 * Paquet utilisé en réponse à un {@link dataobject.paquet.DechiffrerPaquet}.<br>
 * Il contient le résultat clair du vote nouvellement terminé.
 */
public class DechiffrerFeedbackPaquet extends FeedbackPaquet {

    /**
     * Le résultat clair du vote.
     */
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
