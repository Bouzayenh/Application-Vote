package dataobject.paquet;

import dataobject.Utilisateur;

public class CreerUtilisateurPaquet extends Paquet {

    private Utilisateur utilisateur;

    public CreerUtilisateurPaquet(Utilisateur utilisateur) {
        super(Type.CREER_UTILISATEUR);
        this.utilisateur = utilisateur;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
