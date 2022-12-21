package dataobject.paquet;

/**
 * Paquet servant à demander une clé publique.<br>
 * Il est utilisé tant par {@link controller.Client} que par {@link controller.Serveur}.<br>
 * Il contient l'identifiant du vote dont on souhaite connaître la clé publique.<br>
 * L'émetteur attendra un {@link dataobject.paquet.feedback.ClePubliqueFeedbackPaquet} en retour.
 */
public class DemanderClePubliquePaquet extends Paquet {

    /**
     * L'identifiant du vote dont on souhaite connaître la clé publique.
     */
    private int idVote;

    public DemanderClePubliquePaquet(int idVote) {
        super(Type.DEMANDER_CLE_PUBLIQUE);
        this.idVote = idVote;
    }

    public int getIdVote() {
        return idVote;
    }
}
