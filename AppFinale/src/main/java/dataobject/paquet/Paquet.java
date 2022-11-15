package dataobject.paquet;

import java.io.Serializable;

public abstract class Paquet implements Serializable {

    public enum Type {
        HEARTBEAT,
        FEEDBACK,

        DEMANDER_IDENTIFICATION,
        IDENTIFICATION,

        DEMANDER_CLE_PUBLIQUE,
        DECHIFFRER,

        CREER_VOTE,
        CREER_UTILISATEUR,

        AUTHENTIFICATION,
        DECONNEXION,
        DEMANDER_VOTES,
        DEMANDER_RESULTAT,
        BULLETIN
    }

    private Type type;

    public Paquet(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}