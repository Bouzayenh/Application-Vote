package dataobject.paquet;

public class DemanderResultatPaquet extends Paquet {

    private int idVote;

    public DemanderResultatPaquet(int idVote) {
        super(Type.DEMANDER_RESULTAT);
        this.idVote = idVote;
    }
}
