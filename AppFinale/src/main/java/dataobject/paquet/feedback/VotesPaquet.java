package dataobject.paquet.feedback;

import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.util.Map;

public class VotesPaquet extends FeedbackPaquet {

    private Map<Integer, Vote> votes;

    public VotesPaquet(Map<Integer, Vote> votes) {
        this((FeedbackException) null);
        this.votes = votes;
    }

    public VotesPaquet(FeedbackException exception) {
        super(exception);
    }

    public Map<Integer, Vote> getVotes() {
        return votes;
    }
}
