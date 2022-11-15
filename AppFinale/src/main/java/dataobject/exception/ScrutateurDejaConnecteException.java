package dataobject.exception;

public class ScrutateurDejaConnecteException extends FeedbackException {

    public ScrutateurDejaConnecteException() {
        super("Il existe déjà une connexion à un scrutateur");
    }
}
