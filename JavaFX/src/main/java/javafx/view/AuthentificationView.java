package javafx.view;

import controller.Client;
import javafx.ApplicationIHM;
import javafx.application.Application;
import javafx.controller.AuthentificationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthentificationView extends Stage{

    private Scene scene;
    private AuthentificationController authentificationController;

    public AuthentificationView(ApplicationIHM mainApp) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueAuthentification.fxml"));
        scene = new Scene(fxmlLoader.load(), 620, 540);
        this.setTitle("Authentification!");
        this.setScene(scene);
        authentificationController = fxmlLoader.getController();
        authentificationController.setMyApp(mainApp);
    }

}
