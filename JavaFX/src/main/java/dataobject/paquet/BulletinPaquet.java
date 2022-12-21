package dataobject.paquet;

import dataobject.Chiffre;

/**
 * Paquet servant à déposer un bulletin auprès du serveur.<br>
 * Il contient :
 * <ul>
 *     <li>un {@link Chiffre} représentant la valeur de son vote;</li>
 *     <li>l'identifiant du vote auquel le bulletin correspond.</li>
 * </ul><br>
 * L'émetteur attendra un {@link dataobject.paquet.feedback.FeedbackPaquet} lui indiquant si son vote a bien été pris en compte.
 */
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
