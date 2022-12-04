package javafx.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AuthentificationView extends Stage{

    private Scene scene;

    public AuthentificationView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueAuthentification.fxml"));
        scene = new Scene(fxmlLoader.load(), 620, 540);
        this.setTitle("Authentification!");
        this.setScene(scene);
        System.out.println("oui");
    }

}
