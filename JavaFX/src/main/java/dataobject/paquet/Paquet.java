package dataobject.paquet;

import java.io.Serializable;

/**
 * Un paquet qui sera envoyé entre le serveur et les différents clients.
 */
public abstract class Paquet implements Serializable {

    /**
     * Le type du paquet, sert à indiquer au receveur comment traiter celui-ci.
     */
    private Type type;

    public enum Type {
        /**
         *  Indique que le paquet courant est un {@link HeartbeatPaquet}
         */
        HEARTBEAT,

        /**
         *  Indique que le paquet courant est un {@link dataobject.paquet.feedback.FeedbackPaquet}
         */
        FEEDBACK,

        /**
         *  Indique que le paquet courant est un {@link IdentificationPaquet}
         */
        IDENTIFICATION,

        /**
         *  Indique que le paquet courant est un {@link DemanderClePubliquePaquet}
         */
        DEMANDER_CLE_PUBLIQUE,

        /**
         * Indique que le paquet courant est un {@link DechiffrerPaquet}
         */
        DECHIFFRER,

        /**
         * Indique que le paquet courant est un {@link CreerVotePaquet}
         */
        CREER_VOTE,

        /**
         * Indique que le paquet courant est un {@link AuthentificationPaquet}
         */
        AUTHENTIFICATION,

        /**
         * Indique que le paquet courant est un {@link DeconnexionPaquet}
         */
        DECONNEXION,

        /**
         * Indique que le paquet courant est un {@link DemanderVotesPaquet}
         */
        DEMANDER_VOTES,

        /**
         * Indique que le paquet courant est un {@link DemanderResultatPaquet}
         */
        DEMANDER_RESULTAT,

        /**
         * Indique que le paquet courant est un {@link BulletinPaquet}
         */
        BULLETIN,

        /**
         * Indique que le paquet courant est un {@link ChangerMotDePassePaquet}
         */
        CHANGER_MOT_DE_PASSE
    }

    public Paquet(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}