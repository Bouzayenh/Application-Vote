package javafx.view;

import controller.Client;
import dataobject.Vote;
import javafx.ApplicationIHM;
import javafx.application.Application;
import javafx.controller.AuthentificationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        /*ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        for ( Node element : ((VBox)scene.getRoot()).getChildren()){
            if(element instanceof AnchorPane) {
                ((AnchorPane) element).setEffect(adj);
            }
        }*/

    }
}
