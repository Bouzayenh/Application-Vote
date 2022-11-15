package dataobject.paquet;

import dataobject.Chiffre;

public class DechiffrerPaquet extends Paquet {

    private Chiffre chiffre;
    private int idVote;

    public DechiffrerPaquet(Chiffre chiffre, int idVote) {
        super(Type.DECHIFFRER);
        this.chiffre = chiffre;
        this.idVote = idVote;
    }

    public Chiffre getChiffre() {
        return chiffre;
    }

    public int getIdVote() {
        return idVote;
    }
}
