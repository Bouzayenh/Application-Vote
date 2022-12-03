package dataobject.paquet;

import controller.communication.Connexion;

public class IdentificationPaquet extends Paquet {
        private Connexion.Source source;

    public IdentificationPaquet(Connexion.Source source) {
        super(Type.IDENTIFICATION);
        this.source = source;
    }

    public Connexion.Source getIdentification() {
        return source;
    }
}
