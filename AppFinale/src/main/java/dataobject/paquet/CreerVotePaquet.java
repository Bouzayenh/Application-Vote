package dataobject.paquet;

public class CreerVotePaquet extends Paquet {
    private int idVote;

    public CreerVotePaquet(int idVote) {
        super(Type.CREER_VOTE);
        this.idVote = idVote;
    }

    public int getIdVote() {
        return idVote;
    }
}
