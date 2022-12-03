package dataobject.exception;

public class UtilisateurDeconnecteException extends FeedbackException {

    public UtilisateurDeconnecteException() {
        super("L'utilisateur n'est pas authentifié");
    }
}
