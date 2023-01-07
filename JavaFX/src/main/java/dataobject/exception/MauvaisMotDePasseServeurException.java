package dataobject.exception;

public class MauvaisMotDePasseServeurException extends FeedbackException{

    public MauvaisMotDePasseServeurException() {
        super("Le mot de passe serveur est incorrect.");
    }
}
