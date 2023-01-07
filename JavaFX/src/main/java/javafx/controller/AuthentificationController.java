package javafx.controller;

import controller.Client;
import dataobject.exception.AuthentificationException;
import dataobject.exception.FeedbackException;
import javafx.ApplicationIHM;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.view.ErreurAlert;

import java.io.IOException;

public class AuthentificationController {
    private Client client;
    private ApplicationIHM myApp;

    @FXML
    private TextField TFidentifiant;

    @FXML
    private PasswordField TFmdp;

    @FXML
    void btnOKClicked() {
        try {
            if (TFidentifiant.getText().equals("") || TFmdp.getText().equals(""))
                new ErreurAlert(new AuthentificationException()).show();
            else {
                client.authentification(TFidentifiant.getText(), TFmdp.getText());
                myApp.authentificationToMainView();
            }
        } catch (FeedbackException | IOException | ClassNotFoundException e) {
            new ErreurAlert(e).show();
        }
    }

    public void initialiser(ApplicationIHM app) {
        try {
            myApp = app;
            // initialisation
            client = new Client();
            myApp.setClient(client);
        } catch (IOException e) {
            new ErreurAlert(e).show();
        }
    }
}
