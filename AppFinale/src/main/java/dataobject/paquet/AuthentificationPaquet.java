package dataobject.paquet;

import dataobject.Utilisateur;

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
