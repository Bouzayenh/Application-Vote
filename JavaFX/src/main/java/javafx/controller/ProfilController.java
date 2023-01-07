package javafx.controller;

import controller.Client;
import controller.Serveur;
import dataobject.Utilisateur;
import dataobject.exception.FeedbackException;
import app.AppClientGraphique;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.view.ErreurAlert;
import javafx.view.ListeVoteView;
import javafx.view.ModifUtilisateurView;
import javafx.view.ProfilView;

import java.io.IOException;

public class ProfilController {


    @FXML
    private Label login;

    @FXML
    private Label mail;

    @FXML
    private Button btnmodifier;

    @FXML
    private Label identifiant;

    @FXML
    private PasswordField Ancienmdp;

    @FXML
    private TextField Nouveaumdp;

    @FXML
    private Button btnretour;

    @FXML
    private Button btnconfirmer;

    @FXML
    private Button btnProfil;
    @FXML
    private Button btnResultats;
    @FXML
    private Button btnVotes;
    @FXML
    private ScrollPane scrollPane;

    private Serveur serveur;

    private AppClientGraphique myApp;

    private ProfilView profilView ;

    private ModifUtilisateurView modifUtilisateurView;

    private ListeVoteView vueListeVote;

    private Client client;

    private Utilisateur utilisateur;

    public void vueVoteinit(int m) throws IOException {
        vueListeVote = new ListeVoteView(profilView.getMyApp());
        vueListeVote.setMaximized(true);
        vueListeVote.setterForController();
        vueListeVote.afficher(m);
    }

    public void setModifView(ModifUtilisateurView view) {
        modifUtilisateurView = view;
    }

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
        //profilView.hide();
        try {
            this.vueVoteinit(1);
        }catch(IOException e){
            new ErreurAlert(e).show();
        }
    }
    @FXML
    void btnVotesClicked() {
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        //profilView.hide();
        try {
            this.vueVoteinit(0);
        }catch(IOException e){
            new ErreurAlert(e).show();
        }
    }

    public void setMyApp(AppClientGraphique app) {
        myApp= app;
    }


    public void btnProfilmodifer() {
        try {
            profilView.afficheVueModif();
            utilisateur = profilView.getClient().demandeUtilisateur();
            identifiant.setText(" " + utilisateur.getLogin());
        }catch(IOException | FeedbackException | ClassNotFoundException e){
            new ErreurAlert(e).show();
        }
    }

    @FXML
    void btnROKClicked() {
       profilView.hideVueModif();
    }

    @FXML
    void btnMOKClicked() {
        client= profilView.getClient();
    }
}
