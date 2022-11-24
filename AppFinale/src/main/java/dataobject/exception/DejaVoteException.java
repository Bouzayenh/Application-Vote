package dataobject.exception;

public class DejaVoteException extends FeedbackException {

    public DejaVoteException() {
        super("Cet utilisateur a déjà déposé un bulletin pour ce vote");
    }
}
