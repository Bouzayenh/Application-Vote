package dataobject.paquet.feedback;

import dataobject.ClePublique;
import dataobject.exception.FeedbackException;

public class ClePubliqueFeedbackPaquet extends FeedbackPaquet {

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
