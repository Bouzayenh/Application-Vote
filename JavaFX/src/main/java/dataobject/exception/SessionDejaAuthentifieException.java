package dataobject.exception;

public class SessionDejaAuthentifieException extends FeedbackException {

    public SessionDejaAuthentifieException() {
        super("Un utilisateur est déjà authentifié sur cette session");
    }
}
