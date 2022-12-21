package dataobject.paquet.feedback;

import dataobject.Vote;
import dataobject.exception.FeedbackException;

/**
 * Paquet utilisé en réponse à un {@link dataobject.paquet.DemanderResultatPaquet}.<br>
 * Il contient un {@link Vote} contenant les résultats en plus des informations normales.
 */
public class ResultatFeedbackPaquet extends FeedbackPaquet {

    /**
     * Le vote demandé.
     */
    private Vote vote;

    public ResultatFeedbackPaquet(Vote vote) {
        this((FeedbackException) null);
        this.vote = vote;
    }

    public ResultatFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public Vote getVote() {
        return vote;
    }
}
