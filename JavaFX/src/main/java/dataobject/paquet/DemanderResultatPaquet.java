package dataobject.paquet;


/**
 * Paquet servant au {@link controller.Client} à demander les résultats d'un vote particulier.<br>
 * Il contient l'identifiant du vote dont on souhaite connaître les résultats.<br>
 * En retour, l'émetteur attendra un {@link dataobject.paquet.feedback.ResultatFeedbackPaquet}.
 */
public class DemanderResultatPaquet extends Paquet {
    private int idVote;

    public DemanderResultatPaquet(int idVote) {
        super(Type.DEMANDER_RESULTAT);
        this.idVote = idVote;
    }

    public int getIdVote() {
        return idVote;
    }
}
