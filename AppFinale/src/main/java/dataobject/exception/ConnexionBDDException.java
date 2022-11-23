package dataobject.exception;

public class ConnexionBDDException extends FeedbackException {

    public ConnexionBDDException() {
        super("La base de données a rencontré un problème lors du traitement");
    }
}
