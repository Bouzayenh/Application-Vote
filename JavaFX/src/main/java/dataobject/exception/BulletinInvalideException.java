package dataobject.exception;

public class BulletinInvalideException extends FeedbackException {

    public BulletinInvalideException() {
        super("Le bulletin n'est pas valide");
    }
}
