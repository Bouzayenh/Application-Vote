package dataobject.paquet;

public class DemanderClePubliquePaquet extends Paquet {
    private int idVote;

    public DemanderClePubliquePaquet(int idVote) {
        super(Type.DEMANDER_CLE_PUBLIQUE);
        this.idVote = idVote;
    }

    public int getIdVote() {
        return idVote;
    }
}
