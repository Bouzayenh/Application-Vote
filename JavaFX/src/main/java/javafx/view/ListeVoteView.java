package javafx.view;

import controller.Client;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import javafx.ApplicationIHM;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
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
import java.util.Set;


public class ListeVoteView extends Stage {

    private Scene scene;
    private ListeVoteController listeVoteController;
    private AnchorPane anchorPane;
    private VoteView vueVote;
    private ChoixVue vueChoix;
    private ApplicationIHM myAppli;
    private VBox vboxListeVote;
    private ScrollPane scrollPanelisteVote;
    private ResultatView vueResusltat;

    public ListeVoteView(ApplicationIHM mainApp) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueListeVote.fxml"));
        scene = new Scene(fxmlLoader.load());
        this.setTitle("Liste de votes");
        this.setScene(scene);

        myAppli = mainApp;

        listeVoteController = fxmlLoader.getController();
        vueVote = new VoteView(listeVoteController, this);
        vueChoix = new ChoixVue(listeVoteController);
        vueResusltat = new ResultatView(listeVoteController, this);

        listeVoteController.setMyApp(mainApp);
        VBox root = (VBox) this.scene.getRoot();
        for ( Node element : root.getChildren()){
            if(element instanceof AnchorPane) {
                anchorPane = ((AnchorPane) element);
                for ( Node scrollpane : anchorPane.getChildren()) {
                    if (scrollpane.getId().equals("scrollPane")){
                        scrollPanelisteVote = (ScrollPane) scrollpane;
                    }
                }
            }
        }
        vboxListeVote = new VBox();
        vboxListeVote.setPrefWidth(700);
        vboxListeVote.setPrefHeight(425);
        scrollPanelisteVote.setContent(vboxListeVote);
    }

    public void setterForController(){
        listeVoteController.setMyView(this);
    }
    public void afficher() {
        Client c = listeVoteController.getClient();
        VBox root = (VBox) this.scene.getRoot();

        try {

            Set<Vote> votes = c.consulterVotes();
            for (Vote v : votes) {
                Label label;
                if(!v.estFini()){
                    label = new Label("en cours");
                }else{
                    label = new Label(" terminé");
                }
                label.setFont(new Font(20));

                Button BVote = new Button();
                BVote.setText(v.getIntitule());
                BVote.setFont(new Font(20));
                BVote.setPrefWidth(vboxListeVote.getPrefWidth()-110);
                BVote.setOnAction(actionEvent -> {
                    setFlou();
                    afficherVueVote(v);
                });

                HBox hbox = new HBox();
                hbox.getChildren().addAll(BVote,label);
                hbox.setPadding(new Insets(0.0,0.0,5.0,0.0));
                vboxListeVote.getChildren().add(hbox);

            }
            this.show();
        } catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFlou(){

        ColorAdjust adj = new ColorAdjust(0, -0.9, -0.5, 0);
        GaussianBlur blur = new GaussianBlur(55); // 55 is just to show edge effect more clearly.
        adj.setInput(blur);
        anchorPane.setEffect(adj);
    }

    public void setDefloutage(){

        ColorAdjust adj = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0);
        adj.setInput(blur);
        anchorPane.setEffect(adj);
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

    public void rafraichir(){
        vboxListeVote.getChildren().clear();
        afficher();
    }

    public void cacherVueResultat() {
        vueResusltat.hide();
    }
}

