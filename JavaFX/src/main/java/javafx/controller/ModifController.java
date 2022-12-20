package javafx.controller;

import controller.Serveur;
import javafx.ApplicationIHM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ModifController {
    @FXML
    private TextField TFidentifiant;

    @FXML
    private PasswordField TFmdp;

    @FXML
    private PasswordField TFmail;

    private Serveur serveur;

    private ApplicationIHM myApp;



    public void btnMOKClicked(ActionEvent actionEvent) {
        try {
            serveur.modifierUtilisateurEmail(TFidentifiant.getText(), TFmail.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }





    public void setMyApp(ApplicationIHM app) {
        myApp= app;
    }


}
