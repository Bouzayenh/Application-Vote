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
        vueAuthentification.show();

        vueListeVote = new ListeVoteView(this);
        //vueListeVote.setMaximized(true);
        vueListeVote.setResizable(false);
        vueListeVote.setterForController();
    }

    public void clientDeconnexion() {
        try {
            client.deconnexion();
            vueListeVote.hide();

            vueAuthentification = new AuthentificationView(this);
            vueAuthentification.show();

            vueListeVote = new ListeVoteView(this);
            vueListeVote.setMaximized(true);
            vueListeVote.setterForController();
        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void authentificationToMainView(){
        vueAuthentification.close();
        vueListeVote.setClient(client);
        vueListeVote.afficher(0);
    }
    public void setClient(Client c){
        client = c;
    }

    public Client getClient(){
        return client;
    }

    public void voter(int choix, int idVote) {
       try {
           //client.voter(choix, idVote);
           vueListeVote.animation();
           return;
       } catch (Exception ignored) {}
        vueListeVote.afficherVueErreur();
        vueListeVote.cacherVueChoix();
    }

    public Vote consulterResultat(int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        return client.consulterResultats(idVote);
    }

    public static void main(String[] args) {
        launch();
    }

}
