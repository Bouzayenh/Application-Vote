package dataobject.exception;

public class VoteNonTermineException extends FeedbackException {

    public VoteNonTermineException() {
        super("Le vote n'est pas terminé");
    }
}
