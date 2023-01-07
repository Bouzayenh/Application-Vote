package dataobject.exception;

public class ServeurVerrouilleException  extends FeedbackException{

    public ServeurVerrouilleException() {
        super("Le serveur est verouillé.");
    }
}
