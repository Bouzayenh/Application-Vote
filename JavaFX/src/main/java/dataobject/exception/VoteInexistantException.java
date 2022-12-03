package dataobject.exception;

public class VoteInexistantException extends FeedbackException {

    public VoteInexistantException() {
        super("Aucun vote ne correspond à cet identifiant");
    }
}
