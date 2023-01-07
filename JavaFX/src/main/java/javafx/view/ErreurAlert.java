package javafx.view;

import dataobject.exception.FeedbackException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ErreurAlert extends Alert {

    public ErreurAlert(Exception exception) {
        super(AlertType.WARNING);
        setTitle("Erreur");
        setContentText(exception.getMessage());
    }

    public ErreurAlert(IOException e) {
        super(AlertType.ERROR);
        erreurCritique(e);
    }

    public ErreurAlert(ClassNotFoundException e) {
        super(AlertType.ERROR);
        erreurCritique(e);
    }

    private void erreurCritique(Exception e) {
        setTitle("Erreur critique");
        setContentText("Impossible de se connecter au serveur");
        e.printStackTrace();
    }
}
