package dataobject.exception;

public class ConnexionBaseDeDonneeException extends FeedbackException{
    public ConnexionBaseDeDonneeException() {
        super("La connexion à la base de donnée n'a pas pu être établie");
    }
}
