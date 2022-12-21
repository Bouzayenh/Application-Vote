package dataobject.paquet;

/**
 * Paquet servant à demander une déconnexion.
 */
public class DeconnexionPaquet extends Paquet {

    public DeconnexionPaquet() {
        super(Type.DECONNEXION);
    }
}
