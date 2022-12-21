package dataobject.paquet;

import dataobject.Utilisateur;

/**
 * Paquet servant à s'authentifier auprès du serveur.<br>
 * Il contient un {@link Utilisateur} contenant le login et le mot de passe permettant l'authentification.<br>
 * L'émetteur attendra un {@link dataobject.paquet.feedback.FeedbackPaquet} lui indiquant si l'authentification a eu lieu avec succès, ou si une erreur a eu lieu.
 */
public class AuthentificationPaquet extends Paquet {
    private Utilisateur utilisateur;

    public AuthentificationPaquet(Utilisateur utilisateur) {
        super(Type.AUTHENTIFICATION);
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
