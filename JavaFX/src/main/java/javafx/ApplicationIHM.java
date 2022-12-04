package javafx;

import controller.Serveur;
import javafx.view.AuthentificationView;
import javafx.application.Application;
import javafx.stage.Stage;

public class ApplicationIHM extends Application {

    private Stage primaryStage;
    private Serveur serveur;
    private AuthentificationView vueAuthentification;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        vueAuthentification = new AuthentificationView();
        vueAuthentification.show();

    }

    public static void main(String[] args) {
        launch();
    }

}
