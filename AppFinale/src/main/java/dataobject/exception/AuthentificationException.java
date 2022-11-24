package dataobject.exception;

public class AuthentificationException extends FeedbackException {

    public AuthentificationException() {
        super("Les identifiants d'authentification sont incorrects");
    }
}
