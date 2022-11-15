package dataobject.exception;

public class ScrutateurDeconnecteException extends FeedbackException {

    public ScrutateurDeconnecteException() {
        super("Il n'existe pas de connexion à un scrutateur");
    }
}
