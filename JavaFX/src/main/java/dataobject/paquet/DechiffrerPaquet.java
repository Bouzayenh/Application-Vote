package dataobject.paquet;

import dataobject.Chiffre;

/**
 * Paquet servant au {@link controller.Serveur} à demander au {@link controller.Scrutateur} de déchiffrer.<br>
 * Il contient :
 * <ul>
 *     <li>le {@link Chiffre} à déchiffrer;</li>
 *     <li>l'identifiant du vote (afin de trouver les clés correspondantes);</li>
 *     <li>le nombre de personnes ayant participé au vote.</li>
 * </ul><br>
 * L'émetteur attendra un {@link dataobject.paquet.feedback.DechiffrerFeedbackPaquet} en retour.
 */
public class DechiffrerPaquet extends Paquet {

    /**
     * L'identifiant du vote.
     */
    private int idVote;

    /**
     * Le {@link Chiffre} à déchiffrer.
     */
    private Chiffre chiffre;

    /**
     * Le nombre de personnes ayant participé au vote.
     */
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
