package dataobject.exception;

public class VoteTermineException extends FeedbackException {

    public VoteTermineException() {
        super("Le vote est déjà terminé");
    }
}
