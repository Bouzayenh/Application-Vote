package javafx.controller;

import controller.Client;
import dataobject.Utilisateur;
import javafx.ApplicationIHM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.view.ListeVoteView;

public class ListeVoteController {

    @FXML
    private HBox VotesContaining;
    @FXML
    private Label IntituleVote;
    @FXML
    private Button btnRetour;
    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnConfirmer;
    @FXML
    private Label labelChoix;


    private ApplicationIHM myApp;
    private ListeVoteView vueListeVote;

    public void setMyApp(ApplicationIHM app) {
        myApp= app;
    }
    public void setMyView(ListeVoteView vue){
        vueListeVote = vue;
    }

    public Client getClient() {
        return myApp.getClient();
    }

    @FXML
    void btnRetourClicked(ActionEvent event) {
        vueListeVote.setDefloutage();
        vueListeVote.cacherVueVote();
        vueListeVote.cacherVueResultat();
    }

    @FXML
    void btnAnnulerClicked(ActionEvent event) {
        vueListeVote.cacherVueChoix();
        vueListeVote.afficherVueVote();
    }

    @FXML
    void btnConfirmerClicked(ActionEvent event) {
        vueListeVote.cacherVueChoix();
        vueListeVote.setDefloutage();
        vueListeVote.voter();
    }

    @FXML
    void btnRafraichirClicked(ActionEvent event) {
        vueListeVote.rafraichir();
    }
}
