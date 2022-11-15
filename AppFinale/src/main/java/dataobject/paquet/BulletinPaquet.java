package dataobject.paquet;

import dataobject.Chiffre;

public class BulletinPaquet extends Paquet {

    private Chiffre bulletin;
    private int idVote;

    public BulletinPaquet(Chiffre bulletin, int idVote) {
        super(Type.BULLETIN);
        this.bulletin = bulletin;
        this.idVote = idVote;
    }

    public Chiffre getBulletin() {
        return bulletin;
    }

    public int getIdVote() {
        return idVote;
    }
}
