package dataobject.paquet;

import java.io.Serializable;

public abstract class Paquet implements Serializable {
    private Type type;

    public enum Type {
        HEARTBEAT,
        FEEDBACK,
        IDENTIFICATION,
        DEMANDER_CLE_PUBLIQUE,
        DECHIFFRER,
        CREER_VOTE,
        AUTHENTIFICATION,
        DECONNEXION,
        DEMANDER_VOTES,
        DEMANDER_RESULTAT,
        BULLETIN
    }

    public Paquet(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}