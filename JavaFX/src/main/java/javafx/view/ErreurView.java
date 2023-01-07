package javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ErreurView extends Stage {

    private Scene scene;

    public ErreurView() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(ErreurView.class.getResource("/xml/vueErreur.fxml"));
        scene = new Scene(fxmlLoader.load());
        this.setScene(scene);

    }

}
