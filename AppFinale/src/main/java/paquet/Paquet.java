package paquet;

import java.io.Serializable;

public abstract class Paquet implements Serializable {

    public enum Source {
        CLIENT,
        SERVEUR,
        SCRUTATEUR,
        DATABASE
    }

    /**
     * L'application à l'origine du paquet (Client, Serveur, Scrutateur ou Database)
     */
    private Source source;

    public Paquet(Source source) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }
}