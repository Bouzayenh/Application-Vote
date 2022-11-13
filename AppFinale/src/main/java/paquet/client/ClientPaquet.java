package paquet.client;

import paquet.Paquet;

public abstract class ClientPaquet extends Paquet {

    public enum Action {
        DEMANDER_CLE_PUBLIQUE,
        ENVOYER_BULLETIN,
        DEMANDER_VOTES,
        ENVOYER_IDENTIFIANTS,
        DECONNEXION
    }

    /**
     * L'action pour laquelle le paquet est transmis
     */
    private Action action;

    public ClientPaquet(Action action) {
        super(Source.CLIENT);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}