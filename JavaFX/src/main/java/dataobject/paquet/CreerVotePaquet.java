package dataobject.paquet;

/**
 * Paquet servant au {@link controller.Serveur} à demander au {@link controller.Scrutateur} de générer des clés pour un vote venant d'être créé.
 * Il ne contient aucune information.<br>
 * L'émetteur attendra un {@link dataobject.paquet.feedback.CreerVoteFeedbackPaquet} en retour.
 */
public class CreerVotePaquet extends Paquet {

    public CreerVotePaquet() {
        super(Type.CREER_VOTE);
    }
}
