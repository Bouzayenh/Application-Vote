package javafx.controller;

import controller.Client;
import dataobject.exception.FeedbackException;
import javafx.ApplicationIHM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AuthentificationController {

    @FXML
    private TextField TFidentifiant;

    @FXML
    private PasswordField TFmdp;

    private Client client;

    private ApplicationIHM myApp;

    @FXML
    void btnOKClicked(ActionEvent event) {

        try {
            String id = TFidentifiant.getText();
            String mdp = TFmdp.getText();
            if ( !id.equals("") && !mdp.equals("")) {
                client.authentification( id,mdp );
                myApp.setClient(client);
                myApp.authentificationToMainView();
            }

        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void voteChoisi(ActionEvent event) {

    }
    public AuthentificationController()  {
        try {
            // initialisation
            System.out.println("Connexion au serveur...");
            client = new Client();
            System.out.println("Connecté avec succès");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMyApp(ApplicationIHM app) {
        myApp= app;
    }
}
