package javafx;

import controller.Client;
import controller.Serveur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import javafx.view.AuthentificationView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.view.ErreurAlert;
import javafx.view.ListeVoteView;

import java.io.IOException;

public class ApplicationIHM extends Application {
    private AuthentificationView vueAuthentification;
    private ListeVoteView vueListeVote;
    private Client client;

    @Override
    public void start(Stage primaryStage) {
        try {
            showAuthentificationView();
        } catch (IOException e) {
            new ErreurAlert(e).show();
        }
    }

    public void showAuthentificationView() throws IOException {
        vueAuthentification = new AuthentificationView(this);
        vueAuthentification.show();
    }

    public void authentificationToMainView() throws IOException {
        vueAuthentification.close();
        vueListeVote = new ListeVoteView(this);
        vueListeVote.setMaximized(true);
        vueListeVote.setterForController();
        vueListeVote.setClient(client);
        vueListeVote.afficher(0);
    }

    public void deconnexion() {
        try {
            client.deconnexion();
            vueListeVote.hide();
            showAuthentificationView();
        } catch (FeedbackException | IOException | ClassNotFoundException e) {
            new ErreurAlert(e).show();
        }
    }

    public void setClient(Client c) {
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
        vueListeVote.cacherVueChoix();
    }

    public Vote consulterResultat(int idVote) throws FeedbackException, IOException, ClassNotFoundException {
        return client.consulterResultats(idVote);
    }

    public static void main(String[] args) {
        launch();
    }

}
