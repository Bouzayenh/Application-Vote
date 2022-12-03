package dataobject.paquet.feedback;

import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.util.Set;

public class VotesPaquet extends FeedbackPaquet {
    private Set<Vote> votes;

    public VotesPaquet(Set<Vote> votes) {
        this((FeedbackException) null);
        this.votes = votes;
    }

    public VotesPaquet(FeedbackException exception) {
        super(exception);
    }

    public Set<Vote> getVotes() {
        return votes;
    }
}
