package javafx.controller;

import controller.Client;
import dataobject.Utilisateur;
import dataobject.exception.FeedbackException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.view.ListeVoteView;
import javafx.view.ProfilView;

import java.io.IOException;

public class ListeVoteController {

    @FXML
    private HBox VotesContaining;
    @FXML
    private Label ListeIntituleVote;
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

    //*******VueResultat***********\\
    @FXML
    private Label ResIntituleVote;
    @FXML
    private Label ResPourcentageD;
    @FXML
    private Label ResPourcentageG;
    @FXML
    private Label ResResultatD;
    @FXML
    private Label ResResultatG;

    //*******VueVote***********\\
    @FXML
    private Label VoteIntituleVote;
    @FXML
    private Button VotebtnChoix1;
    @FXML
    private Button VotebtnChoix2;

    //*******VueChoix***********\\
    @FXML
    private Label ChoixIntituleVote;
    @FXML
    private Label ChoixLabelChoix;

    //*******VueModifier***********\\
    @FXML
    private PasswordField Ancienmdp;
    @FXML
    private PasswordField Nouveaumdp;
    @FXML
    private Label identifiant;
    //*******************************\\


    private ListeVoteView vueListeVote;
    public void setMyViewVote(ListeVoteView vue){
        vueListeVote = vue;
    }

    //------------------------Vue Vote et Vue Resultat-------------------------\\
    @FXML
    void btnRetourClicked(ActionEvent event) {
        vueListeVote.setDefloutage();
        vueListeVote.cacherVueVote();
        vueListeVote.cacherVueResultat();
    }

    //---------------------------Vue Choix-------------------------------\\
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

    //------------------------------Bar de navigation------------------------------\\
    @FXML
    void btnDeconnexionClicked(ActionEvent event) {
        vueListeVote.clientDeconnexion();
    }

    @FXML
    void btnProfilClicked(ActionEvent event) throws Exception {

        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficherVueProfil();
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


    //------------------------------Vue Profil------------------------------\\
    public void btnModifierClicked(ActionEvent actionEvent) throws Exception {
        vueListeVote.afficherVueModif();
        Utilisateur utilisateur = vueListeVote.getMyApp().getClient().demandeUtilisateur();
        identifiant.setText(utilisateur.getLogin());
    }

    //------------------------------Vue modifierUtilisateur------------------------------\\
    @FXML
    void btnRAnnulerClicked(ActionEvent actionEvent) throws Exception {
        vueListeVote.cacherVueModif();

    }
    @FXML
    void btnValiderClicked(ActionEvent actionEvent) {
        Client client= vueListeVote.getMyApp().getClient();
        try {
            client.changerMotDePasse(identifiant.getText(),Ancienmdp.getText(),Nouveaumdp.getText());
            vueListeVote.cacherVueModif();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //-----------------------------------------------------------------------------\\


    //méthodes pour la vue principale
    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public VBox getVboxMain() {
        return vboxMain;
    }

    public Label getTitre() {
        return ListeIntituleVote;
    }

    //méthodes pour vueResultat
    public Label getResIntituleVote(){
        return ResIntituleVote;
    }
    public Label getResPourcentageD(){
        return ResPourcentageD;
    }
    public Label getResResultatD(){
        return ResResultatD;
    }
    public Label getResPourcentageG(){
        return ResPourcentageG;
    }
    public Label getResResultatG(){
        return ResResultatG;
    }

    //méthodes pour vueVote
    public Label getVoteIntituleVote(){
        return VoteIntituleVote;
    }
    public Button getVotebtnChoix1() {
        return VotebtnChoix1;
    }
    public Button getVotebtnChoix2() {
        return VotebtnChoix2;
    }

    //méthodes pour vueChoix
    public Label getChoixIntituleVote() {
        return ChoixIntituleVote;
    }
    public Label getChoixLabelChoix() {
        return ChoixLabelChoix;
    }

}
