package javafx.view;

import dataobject.Vote;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ChoixView extends Stage {

    private ListeVoteView listeVoteView;
    private Scene scene;
    private AnchorPane anchorPane;
    private int choix;
    private int idVote;
    private Label intitule;
    private Label choixLabel;
    private Rectangle recG;
    private Rectangle recD;
    private Rectangle recC;

    public ChoixView(ListeVoteController c, ListeVoteView v) throws IOException {

        listeVoteView = v;
        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/xml/vueChoix.fxml"));
        fxmlLoader.setController(c);
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(v.getScene().getWindow());
        scene = new Scene(fxmlLoader.load());
        this.setScene(scene);
        VBox root = (VBox) this.scene.getRoot();
        for ( Node element : root.getChildren()){
            if(element instanceof AnchorPane) {
                anchorPane = (AnchorPane) element;
            }
        }
        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        this.initStyle(StageStyle.UNDECORATED);
        intitule = c.getChoixIntituleVote();
        choixLabel = c.getChoixLabelChoix();
        recG = c.getRecG();
        recD = c.getRecD();
        recC = c.getRecC();
    }

    public void afficherChoix(Vote v, int choix){

        this.choix = choix;
        this.idVote = v.getIdentifiant();

        String intitule = v.getIntitule();
        this.intitule.setText(intitule);
        if(intitule.length()>24) this.intitule.setFont(new Font(15));

        String voteChoisis;
        if( choix == 1 ){
            voteChoisis = v.getOption1();
        }else {
            voteChoisis = v.getOption2();
        }
        choixLabel.setText(voteChoisis);
        if(voteChoisis.length()>24) choixLabel.setFont(new Font(15));

        this.show();
    }

    public int getChoix(){
        return choix;
    }

    public int getIdVote(){
        return idVote;
    }


    public void animation(){

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(1));
        transition.setNode(anchorPane);
        transition.setToY(300);

        TranslateTransition transitionRecC = new TranslateTransition();
        transitionRecC.setDuration(Duration.millis(100));
        transitionRecC.setNode(recC);
        transitionRecC.setToX(180);
        transitionRecC.setToY(-410);

        TranslateTransition transitionRecD = new TranslateTransition();
        transitionRecD.setDuration(Duration.millis(100));
        transitionRecD.setNode(recD);
        transitionRecD.setToX(262);
        transitionRecD.setToY(-280);

        TranslateTransition transitionRecG = new TranslateTransition();
        transitionRecG.setDuration(Duration.millis(100));
        transitionRecG.setNode(recG);
        transitionRecG.setToX(200);
        transitionRecG.setToY(-150);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(100),recC);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(100),recD);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);

        FadeTransition fadeTransition2 = new FadeTransition(Duration.millis(100),recG);
        fadeTransition2.setFromValue(0);
        fadeTransition2.setToValue(1);

        TranslateTransition transitionRecC2 = new TranslateTransition();
        transitionRecC2.setDuration(Duration.seconds(1));
        transitionRecC2.setNode(recC);
        transitionRecC2.setToX(500);


        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(transition,transitionRecD,transitionRecG,
                                                    transitionRecC ,fadeTransition, fadeTransition1,
                                                    fadeTransition2, transitionRecC2);
        sequentialTransition.setOnFinished(e -> listeVoteView.finAnimation());
        sequentialTransition.play();
    }

}
