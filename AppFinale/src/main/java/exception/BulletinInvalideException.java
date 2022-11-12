package exception;

public class BulletinInvalideException extends Exception {

    public BulletinInvalideException() {
        super("Le bulletin n'a pas été pris en compte");
    }
}
