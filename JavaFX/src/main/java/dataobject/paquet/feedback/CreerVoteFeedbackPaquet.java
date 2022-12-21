package dataobject.paquet.feedback;

import dataobject.Chiffre;
import dataobject.exception.FeedbackException;

/**
 * Paquet envoyé par le {@link controller.Scrutateur} après avoir généré des clés à la demande d'un {@link dataobject.paquet.CreerVotePaquet}.<br>
 * Il contient :
 * <ul>
 *     <li>l'identifiant du vote qui a été créé;</li>
 *     <li>le {@link Chiffre} correspondant au 0 chiffré avec la clé publique fraîchement générée.</li>
 * </ul>
 */
public class CreerVoteFeedbackPaquet extends FeedbackPaquet {

    /**
     * L'identifiant du vote qui a été créé.
     */
    private int idVote;

    /**
     * La valeur 0 chiffrée avec la clé publique fraîchement générée.
     */
    private Chiffre chiffre;

    public CreerVoteFeedbackPaquet(int idVote, Chiffre chiffre) {
        this((FeedbackException) null);
        this.idVote = idVote;
        this.chiffre = chiffre;
    }

    public CreerVoteFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public int getIdVote() {
        return idVote;
    }

    public Chiffre getChiffre() {
        return chiffre;
    }
}
