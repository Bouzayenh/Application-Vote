package dataobject.paquet;

import dataobject.Chiffre;

public class DechiffrerPaquet extends Paquet {
    private int idVote;
    private Chiffre chiffre;
    private int nbBulletins;

    public DechiffrerPaquet(int idVote, Chiffre chiffre, int nbBulletins) {
        super(Type.DECHIFFRER);
        this.idVote = idVote;
        this.chiffre = chiffre;
        this.nbBulletins = nbBulletins;
    }

    public int getIdVote() {
        return idVote;
    }

    public Chiffre getChiffre() {
        return chiffre;
    }

    public int getNbBulletins() {
        return nbBulletins;
    }
}
