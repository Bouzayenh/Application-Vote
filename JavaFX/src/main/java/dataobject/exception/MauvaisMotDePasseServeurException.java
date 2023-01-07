package dataobject.exception;

public class MauvaisMotDePasseServeurException extends Exception{

    public MauvaisMotDePasseServeurException() {
        super("Le mot de passe serveur est incorrect.");
    }
}
