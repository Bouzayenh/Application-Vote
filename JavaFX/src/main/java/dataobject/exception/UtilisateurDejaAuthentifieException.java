package dataobject.exception;

public class UtilisateurDejaAuthentifieException extends FeedbackException {

    public UtilisateurDejaAuthentifieException() {
        super("L'utilisateur est déjà authentifié");
    }
}
