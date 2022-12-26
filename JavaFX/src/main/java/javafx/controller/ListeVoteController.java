package javafx.controller;

import controller.Client;
import dataobject.Utilisateur;
import javafx.ApplicationIHM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.view.ListeVoteView;
import javafx.view.ModifUtilisateur;

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
    private Button btnDeconnexion;
    @FXML
    private Button btnProfil;
    @FXML
    private Button btnResultats;
    @FXML
    private Button btnVotes;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vboxMain;


    private ListeVoteView vueListeVote;

    private ModifUtilisateur vuemodifUtilisateur;

    public void setMyView(ListeVoteView vue){
        vueListeVote = vue;
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
    void btnDeconnexionClicked(ActionEvent event) {
        vueListeVote.clientDeconnexion();
    }

    @FXML
    void btnProfilClicked(ActionEvent event) {

        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vuemodifUtilisateur.afficher();
    }
    @FXML
    void btnResultatClicked(ActionEvent event) {
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficher(1);
    }
    @FXML
    void btnVotesClicked(ActionEvent event) {
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficher(0);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public VBox getVboxMain() {
        return vboxMain;
    }
}
