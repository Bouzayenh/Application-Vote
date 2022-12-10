package javafx;

import controller.Client;
import controller.Serveur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import javafx.view.AuthentificationView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.view.ListeVoteView;

import java.io.IOException;

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
        vueListeVote.setterForController();
        vueAuthentification.show();
    }

    public void authentificationToMainView(){
        vueAuthentification.hide();
        vueListeVote.afficher();
    }
    public void setClient(Client c){
        client = c;
    }

    public Client getClient(){
        return client;
    }

    public void voter(int choix, int idVote) {
        try {
            client.voter(choix, idVote);
        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
