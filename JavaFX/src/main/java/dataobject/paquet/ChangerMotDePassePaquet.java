package dataobject.paquet;

import dataobject.Utilisateur;

/**
 * Paquet pour demander le changement de mot de passe au serveur,
 * il contient l'utilisateur, ainsi qu'une chaîne de charactères correspondant au nouveau mot de passe.
 */
public class ChangerMotDePassePaquet extends Paquet{

    /**
     * L'objet {@link Utilisateur} correspondant à l'utilisateur qu'il faut changer (login, ancien mot de passe)
     */
    private Utilisateur utilisateur;

    /**
     * Le nouveau mot de passe.
     */
    private String nouveauMotDePasse;

    public ChangerMotDePassePaquet(Utilisateur utilisateur, String nouveauMotDePasse) {
        super(Type.CHANGER_MOT_DE_PASSE);
        this.utilisateur = utilisateur;
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public String getAncienMotDePasse(){
        return utilisateur.getMotDePasse();
    }

    public String getLogin(){
        return utilisateur.getLogin();
    }
}
