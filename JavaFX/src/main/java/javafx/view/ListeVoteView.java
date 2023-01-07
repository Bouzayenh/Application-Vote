package javafx.view;

import controller.Client;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import app.AppClientGraphique;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class ListeVoteView extends Stage {

    private Scene scene;
    private ListeVoteController listeVoteController;
    private AppClientGraphique myApp;
    private Client myClient;

    private VBox vboxMain;
    private VBox vboxListeVote;
    private ScrollPane scrollPanelisteVote;
    private Label titre;
    private VBox profilView;

    private VoteView vueVote;
    private ChoixView vueChoix;
    private ResultatView vueResusltat;
    private ModifUtilisateurView vueModif;

    public ListeVoteView(AppClientGraphique mainApp) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/xml/vueListeVote.fxml"));
        scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/vueListeVote.css")).toExternalForm());
        this.setTitle("Liste des votes");
        this.setScene(scene);

        myApp = mainApp;
        myClient = myApp.getClient();

        listeVoteController = fxmlLoader.getController();
        vueVote = new VoteView(listeVoteController, this);
        vueChoix = new ChoixView(listeVoteController, this);
        vueResusltat = new ResultatView(listeVoteController, this);
        vueModif = new ModifUtilisateurView(listeVoteController, this);
        FXMLLoader fxmlLoader1 = new FXMLLoader(ModifUtilisateurView.class.getResource("/xml/VueProfil.fxml"));
        fxmlLoader1.setController(listeVoteController);
        profilView = fxmlLoader1.load();
        profilView.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/vueListeVote.css")).toExternalForm());

        scrollPanelisteVote = listeVoteController.getScrollPane();
        vboxMain = listeVoteController.getVboxMain();
        titre = listeVoteController.getTitre();
        titre.setStyle("-fx-text-fill: white");
        vboxListeVote = new VBox();
        vboxListeVote.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0)");
        scrollPanelisteVote.setContent(vboxListeVote);
        scrollPanelisteVote.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void setterForController(){
        listeVoteController.setMyViewVote(this);
    }

    public void afficher() {
        this.show();
        double width = scene.getWidth();
        double height = scene.getHeight();
        scrollPanelisteVote.setPrefSize(width-60, height-150 );
        vboxListeVote.setPrefSize(width-60, height-150 );
        vboxMain.setPrefSize(width-60, height-120 );
        
    }
    public void afficher(int m) {
        vboxMain.getChildren().clear();
        afficher();
        //m == 0 -> voteEnCours | m != 0 -> voteFinis
        try {
            vboxListeVote.getChildren().clear();
            int pair=0;
            List<Vote> votes;

            if (m == 0) {
                 votes = myClient.consulterVotesEnCours();
                 titre.setText("Votes en cours");
            }else{
                 votes = myClient.consulterVotesFinis();
                 titre.setText("Consulter les résultats");
            }

            for (Vote v : votes) {

                Button BVote = new Button();
                BVote.setText(v.getIntitule());
                BVote.setFont(new Font(30));
                BVote.setPrefWidth(vboxListeVote.getPrefWidth()-10);
                BVote.setOnAction(actionEvent -> {
                    afficherVueVote(v);
                });
                BVote.maxWidthProperty().bind(scrollPanelisteVote.widthProperty());
                BVote.maxHeightProperty().bind(scrollPanelisteVote.heightProperty().divide(10));
                if(pair%2==0){
                    BVote.setStyle("-fx-background-color: rgba(68,76,168,0.5); -fx-text-fill: white");
                }else {
                    BVote.setStyle("-fx-background-color: rgba(36,42,119,0.5); -fx-text-fill: white");
                }
                pair++;
                vboxListeVote.getChildren().add(BVote);
            }
        } catch (FeedbackException | IOException | ClassNotFoundException e) {
            new ErreurAlert(e).show();
        }
        vboxMain.getChildren().addAll(titre, scrollPanelisteVote);
    }
    public void afficherVueProfil() {
        vboxMain.getChildren().clear();
        vboxMain.getChildren().add(profilView);
    }

    public void afficherVueChoix(Vote v, int choix){
        vueChoix.afficherChoix(v,choix);
    }

    public void cacherVueChoix(){
        vueChoix.hide();
    }

    public void afficherVueVote(Vote v){

        if(v.estFini()){
            vueResusltat.afficherResultat(v);
        }else{
            vueVote.afficherVote(v);
        }
    }

    public void afficherVueVote(){
        vueVote.show();
    }

    public void cacherVueVote(){
        vueVote.hide();
    }

    public void voter() {
        myApp.voter(vueChoix.getChoix(), vueChoix.getIdVote());
    }

    public void cacherVueResultat() {
        vueResusltat.hide();
    }

    public void deconnexion() {
        myApp.deconnexion();
    }

    public void setClient(Client client) {
        myClient = client;
    }

    public Vote consulterResulat(int identifiant) throws FeedbackException, IOException, ClassNotFoundException {
        return myClient.consulterResultats(identifiant);
    }

    public void animation() {
        vueChoix.animation();
    }

    public AppClientGraphique getMyApp() {
        return myApp;
    }

    public void afficherVueModif() {
        vueModif.show();
    }

    public void cacherVueModif() {
        vueModif.hide();
    }

    public void finAnimation() {
        cacherVueChoix();
    }
}

