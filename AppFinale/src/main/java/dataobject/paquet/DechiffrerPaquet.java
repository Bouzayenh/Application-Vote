package dataobject.paquet;

import dataobject.Chiffre;

public class DechiffrerPaquet extends Paquet {
    private int idVote;
    private Chiffre chiffre;

    public DechiffrerPaquet(int idVote, Chiffre chiffre) {
        super(Type.DECHIFFRER);
        this.idVote = idVote;
        this.chiffre = chiffre;
    }

    public int getIdVote() {
        return idVote;
    }

    public Chiffre getChiffre() {
        return chiffre;
    }
}
