package dataobject.paquet.feedback;

import dataobject.Utilisateur;
import dataobject.exception.FeedbackException;
import dataobject.paquet.DemanderUtilisateurPaquet;

/**
 * Paquet utilisé en réponse à un {@link DemanderUtilisateurPaquet}.<br>
 * Il contient un {@link String} contenant le mail d'un utilisateur.
 */
public class UtilisateurFeedbackPaquet extends FeedbackPaquet{
    private Utilisateur client;

    public UtilisateurFeedbackPaquet(Utilisateur client) {
        this((FeedbackException) null);
        this.client = client;
    }

    public UtilisateurFeedbackPaquet(FeedbackException exception) {
        super(exception);
    }

    public Utilisateur getUtilisateur() {
        return client;
    }
}
