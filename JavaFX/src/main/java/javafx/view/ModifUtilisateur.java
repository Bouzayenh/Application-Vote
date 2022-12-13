package javafx.view;

import javafx.ApplicationIHM;
import javafx.controller.ModifController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifUtilisateur extends Stage {

    @FXML
    private TextField TFidentifiant;

    private Scene scene;


    private ModifController modifController;


    public ModifUtilisateur(ApplicationIHM mainApp) throws IOException {
        FXMLLoader fxml= new FXMLLoader(ModifUtilisateur.class.getResource("/javafx/VueModifUtilisateur.fxml"));
        scene = new Scene(fxml.load());

        this.setTitle("Modifcation Utilisateur");
        this.setScene(scene);

        modifController = fxml.getController();
        modifController.setMyApp(mainApp);




    }

    public void afficher() {
        this.show();
    }
}
