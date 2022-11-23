package dataobject.paquet.feedback;

import dataobject.Vote;
import dataobject.exception.FeedbackException;

public class ResultatFeedbackPaquet extends FeedbackPaquet {
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
