package dataobject.paquet;

/**
 * Paquet servant à demander la liste des votes.
 * Il ne contient aucune information, mais l'émetteur attendra un {@link dataobject.paquet.feedback.VotesPaquet} en retour.
 */
public class DemanderVotesPaquet extends Paquet {

    public DemanderVotesPaquet() {
        super(Type.DEMANDER_VOTES);
    }
}
