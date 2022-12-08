package javafx;

import controller.Client;
import controller.Serveur;
import javafx.view.AuthentificationView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.view.ListeVoteView;

public class ApplicationIHM extends Application {

    private Stage primaryStage;
    private Serveur serveur;
    private AuthentificationView vueAuthentification;
    private ListeVoteView vueListeVote;
    private Client client;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        vueAuthentification = new AuthentificationView(this);
        vueListeVote = new ListeVoteView(this);
        vueListeVote.setMaximized(true);
        vueAuthentification.show();
    }

    public void authentificationToMainView(){
        vueAuthentification.hide();
        vueListeVote.montrer();
    }
    public void setClient(Client c){
        client = c;
    }

    public Client getClient(){
        return client;
    }

    public static void main(String[] args) {
        launch();
    }

}
