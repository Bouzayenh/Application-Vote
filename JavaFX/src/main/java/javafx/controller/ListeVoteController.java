package javafx.controller;

import controller.Client;
import dataobject.Utilisateur;
import dataobject.exception.FeedbackException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.view.ErreurAlert;
import javafx.view.ListeVoteView;
import javafx.view.ProfilView;

import java.io.IOException;

public class ListeVoteController {

    // ListeVotes
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

    // Resultats
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

    // Vote
    @FXML
    private Label VoteIntituleVote;
    @FXML
    private Button VotebtnChoix1;
    @FXML
    private Button VotebtnChoix2;

    // Choix
    @FXML
    private Label ChoixIntituleVote;
    @FXML
    private Label ChoixLabelChoix;
    @FXML
    private Rectangle recC;
    @FXML
    private Rectangle recD;
    @FXML
    private Rectangle recG;

    // Profil
    @FXML
    private Label login;
    @FXML
    private Label mail;

    // ModifierUtilisateur
    @FXML
    private PasswordField Ancienmdp;
    @FXML
    private PasswordField Nouveaumdp;
    @FXML
    private Label identifiant;

    private ListeVoteView vueListeVote;

    public void setMyViewVote(ListeVoteView vue){
        vueListeVote = vue;
    }

    // Navigation
    @FXML
    void btnDeconnexionClicked() {
        vueListeVote.deconnexion();
    }

    @FXML
    void btnProfilClicked() {
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficherVueProfil();
    }

    @FXML
    void btnResultatClicked() {
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficher(1);
    }

    @FXML
    void btnVotesClicked() {
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        vueListeVote.afficher(0);
    }

    // Vote, Resultats
    @FXML
    void btnRetourClicked() {
        vueListeVote.cacherVueVote();
        vueListeVote.cacherVueResultat();
    }

    // Choix
    @FXML
    void btnAnnulerClicked() {
        vueListeVote.cacherVueChoix();
        vueListeVote.afficherVueVote();
    }

    @FXML
    void btnConfirmerClicked() {
       /* vueListeVote.cacherVueChoix();
        vueListeVote.setDefloutage();*/
        vueListeVote.voter();
    }

    // Profil
    public void btnModifierClicked() {
        try {
            vueListeVote.afficherVueModif();
            Utilisateur utilisateur = vueListeVote.getMyApp().getClient().demandeUtilisateur();
            identifiant.setText(utilisateur.getLogin());
        }catch(IOException | FeedbackException | ClassNotFoundException e){
            new ErreurAlert(e).show();
        }
    }

    // ModifierUtilisateur
    @FXML
    void btnRAnnulerClicked() {
        vueListeVote.cacherVueModif();
    }

    @FXML
    void btnValiderClicked() {
        Client client= vueListeVote.getMyApp().getClient();
        try {
            client.changerMotDePasse(identifiant.getText(),Ancienmdp.getText(),Nouveaumdp.getText());
            vueListeVote.cacherVueModif();
        } catch (IOException | FeedbackException | ClassNotFoundException e) {
            new ErreurAlert(e).show();
        }
    }

    // méthodes pour la vue principale
    public ScrollPane getScrollPane() {
        return scrollPane;
    }
    public VBox getVboxMain() {
        return vboxMain;
    }
    public Label getTitre() {
        return ListeIntituleVote;
    }

    // méthodes pour Resultats
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

    //méthodes pour Vote
    public Label getVoteIntituleVote(){
        return VoteIntituleVote;
    }
    public Button getVotebtnChoix1() {
        return VotebtnChoix1;
    }
    public Button getVotebtnChoix2() {
        return VotebtnChoix2;
    }

    //méthodes pour Choix
    public Label getChoixIntituleVote() {
        return ChoixIntituleVote;
    }
    public Label getChoixLabelChoix() {
        return ChoixLabelChoix;
    }
    public Rectangle getRecC() {
        return recC;
    }
    public Rectangle getRecD() {
        return recD;
    }
    public Rectangle getRecG() {
        return recG;
    }

}
