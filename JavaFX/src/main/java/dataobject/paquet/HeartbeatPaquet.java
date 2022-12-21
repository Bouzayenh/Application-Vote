package dataobject.paquet;

/**
 * Paquet vide servant à confirmer la connexion avec un tier.
 */
public class HeartbeatPaquet extends Paquet {

    public HeartbeatPaquet() {
        super(Type.HEARTBEAT);
    }
}
