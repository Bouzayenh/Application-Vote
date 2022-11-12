package exception;

public class AuthentificationRefuseeException extends Exception {

    public AuthentificationRefuseeException() {
        super("Authentification non permise");
    }
}
