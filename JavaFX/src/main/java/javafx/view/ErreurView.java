package javafx.view;

import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ErreurView extends Stage {

    private Scene scene;

    public ErreurView() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ErreurView.class.getResource("/javafx/vueErreur.fxml"));
        scene = new Scene(fxmlLoader.load());
        this.setScene(scene);

    }

}
