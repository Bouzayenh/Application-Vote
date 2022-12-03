package dataobject.exception;

public class AucunVoteException extends FeedbackException {

    public AucunVoteException() {
        super("Il n'y a pas de vote");
    }
}
