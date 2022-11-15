package dataobject.paquet;

import dataobject.Vote;

public class CreerVotePaquet extends Paquet {

    private Vote vote;

    public CreerVotePaquet(Vote vote) {
        super(Type.CREER_VOTE);
        this.vote = vote;
    }

    public Vote getVote() {
        return vote;
    }
}
