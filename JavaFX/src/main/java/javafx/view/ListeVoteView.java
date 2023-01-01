package javafx.view;

import controller.Client;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import javafx.ApplicationIHM;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Set;


public class ListeVoteView extends Stage {

    private Scene scene;
    private ListeVoteController listeVoteController;
    private VBox vboxMain;
    private VoteView vueVote;
    private ChoixVue vueChoix;
    private ApplicationIHM myAppli;
    private VBox vboxListeVote;
    private ScrollPane scrollPanelisteVote;
    private ResultatView vueResusltat;
    private VBox backgrounVBOX;
    private Label titre;
    private Client myClient;
    private VBox profilView;
    ColorAdjust flou;
    ColorAdjust net;

    public ListeVoteView(ApplicationIHM mainApp) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueListeVote.fxml"));
        scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("/javafx/vueListeVote.css").toExternalForm());
        this.setTitle("Liste de votes");
        this.setScene(scene);

        FXMLLoader fxmlLoader1 = new FXMLLoader(ModifUtilisateurView.class.getResource("/javafx/VueProfil.fxml"));
        profilView = fxmlLoader1.load();
        profilView.getStylesheets().add(getClass().getResource("/javafx/vueListeVote.css").toExternalForm());


        flou = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur1 = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        flou.setInput(blur1);
        net = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0);
        net.setInput(blur);

        myAppli = mainApp;
        myClient = myAppli.getClient();

        listeVoteController = fxmlLoader.getController();
        vueVote = new VoteView(listeVoteController, this);
        vueChoix = new ChoixVue(listeVoteController);
        vueResusltat = new ResultatView(listeVoteController, this);

        backgrounVBOX = (VBox) this.scene.getRoot();

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

    public  void afficher() {
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
        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //scrollPanelisteVote.setContent(vboxListeVote);
        vboxMain.getChildren().addAll(titre, scrollPanelisteVote);
    }

    public void setFlou(){

        backgrounVBOX.setEffect(flou);
    }

    public void setDefloutage(){

        backgrounVBOX.setEffect(net);
    }
    public void afficherVueProfil() {
        //scrollPanelisteVote.setContent(profilView);
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
        myAppli.voter(vueChoix.getChoix(), vueChoix.getIdVote());
    }

    public void cacherVueResultat() {
        vueResusltat.hide();
    }

    public void clientDeconnexion() {
        myAppli.clientDeconnexion();
    }

    public void setClient(Client client) {
        myClient = client;
    }

    public Vote consulterResulat(int identifiant) throws FeedbackException, IOException, ClassNotFoundException {
        return myClient.consulterResultats(identifiant);
    }

    public ApplicationIHM getMyApp() {
        return myAppli;
    }
}

