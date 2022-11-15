package dataobject.exception;

public class AucunBulletinException extends FeedbackException {

    public AucunBulletinException() {
        super("Personne n'a participé à ce vote");
    }
}
