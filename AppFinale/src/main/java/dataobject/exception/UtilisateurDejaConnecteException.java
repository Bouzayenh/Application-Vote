package dataobject.exception;

public class UtilisateurDejaConnecteException extends FeedbackException {

    public UtilisateurDejaConnecteException() {
        super("L'utilisateur est déjà authentifié");
    }
}
