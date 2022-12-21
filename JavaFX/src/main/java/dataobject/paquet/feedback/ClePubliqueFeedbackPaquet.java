package dataobject.paquet.feedback;

import dataobject.ClePublique;
import dataobject.exception.FeedbackException;

/**
 * Paquet utilisé en réponse à un {@link dataobject.paquet.DemanderClePubliquePaquet}.<br>
 * Il contient la {@link ClePublique} demandée.
 */
public class ClePubliqueFeedbackPaquet extends FeedbackPaquet {

    /**
     * La {@link ClePublique} demandée.
     */
    private ClePublique clePublique;

    public ClePubliqueFeedbackPaquet(ClePublique clePublique) {
        this((FeedbackException) null);
        this.clePublique = clePublique;
    }

    public ClePubliqueFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public ClePublique getClePublique() {
        return clePublique;
    }
}
