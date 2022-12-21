package dataobject.paquet;

import controller.communication.Connexion;

/**
 * Paquet servant à {@link controller.Client} ou à un {@link controller.Scrutateur} à annoncer son identité auprès du {@link controller.Serveur}.
 */
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
