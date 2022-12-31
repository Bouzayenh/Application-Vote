package javafx.controller;

import controller.Client;
import controller.Serveur;
import dataobject.Utilisateur;
import dataobject.exception.FeedbackException;
import javafx.ApplicationIHM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.view.ListeVoteView;
import javafx.view.ModifUtilisateurView;
import javafx.view.ProfilView;

import java.io.IOException;
import java.sql.SQLException;

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

    private ApplicationIHM myApp;

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

    public void setModifView(ModifUtilisateurView view) throws Exception {
        modifUtilisateurView = view;
    }

    public  void setMyViewProfil(ProfilView vue) throws FeedbackException, IOException, ClassNotFoundException {
        profilView = vue;
        utilisateur = profilView.getClient().demandeUtilisateur();
        login.setText("Login : "+utilisateur.getLogin());
        mail.setText("Mail : "+utilisateur.getEmail());
    }

    @FXML
    void btnDeconnexionClicked(ActionEvent event) {
        vueListeVote.clientDeconnexion();
    }

    @FXML
    void btnProfilClicked(ActionEvent event) throws Exception {

        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        profilView.afficher();
    }
    @FXML
    void btnResultatClicked(ActionEvent event) throws IOException {
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        profilView.hide();
        this.vueVoteinit(1);
    }
    @FXML
    void btnVotesClicked(ActionEvent event) throws IOException {
        btnVotes.setStyle("-fx-background-radius: 30; -fx-background-color: #414185 ");
        btnProfil.setStyle("-fx-background-radius: 30; -fx-background-color: transparent");
        btnResultats.setStyle("-fx-background-radius: 30; -fx-background-color: transparent ");
        profilView.hide();
        this.vueVoteinit(0);
    }

    public void setMyApp(ApplicationIHM app) {
        myApp= app;
    }


    public void btnProfilmodifer(ActionEvent actionEvent) throws Exception {
        profilView.afficheVueModif();
        utilisateur = profilView.getClient().demandeUtilisateur();
        identifiant.setText(utilisateur.getLogin());
    }

    @FXML
    void btnROKClicked(ActionEvent actionEvent) throws Exception {
       profilView.hideVueModif();


    }

    @FXML
    void btnMOKClicked(ActionEvent actionEvent) {
        client= profilView.getClient();
    }
}
