package javafx.view;

import controller.Serveur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import javafx.animation.TranslateTransition;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class ResultatView extends Stage {

    private Scene scene;
    private AnchorPane anchorPane;
    private ListeVoteView vueListeVote;
    private Label intitule;
    private Label pourcentageD;
    private Label resultatD;
    private Label pourcentageG;
    private Label resultatG;


    public ResultatView(ListeVoteController c, ListeVoteView v) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueResultat.fxml"));
        fxmlLoader.setController(c);
        scene = new Scene(fxmlLoader.load());
        this.setScene(scene);
        vueListeVote = v;
        VBox root = (VBox) this.scene.getRoot();
        for ( Node element : root.getChildren()){
            if(element instanceof AnchorPane) {
                anchorPane = (AnchorPane) element;
            }
        }
        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        this.initStyle(StageStyle.UNDECORATED);
        intitule = c.getResIntituleVote();
        pourcentageD = c.getResPourcentageD();
        pourcentageG = c.getResPourcentageG();
        resultatD = c.getResResultatD();
        resultatG = c.getResResultatG();
    }

    public void afficherResultat(Vote v) {
        double pourcentageG = 0.0;
        double pourcentageD = 0.0;
        try {
            Vote vote = vueListeVote.consulterResulat(v.getIdentifiant());
            if(vote.getNbBulletins() != 0) {
                pourcentageD = ((double) (int) (vote.getResultat() * 10000)) / 100;
                pourcentageG = ((double) (int) ((1 - vote.getResultat()) * 10000)) / 100;
            }

        }catch (FeedbackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        intitule.setText(v.getIntitule());
        this.pourcentageG.setText( pourcentageG + "%");
        this.resultatG.setText("" + v.getOption1());
        this.resultatG.setPrefHeight(pourcentageG);
        this.resultatG.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        this.pourcentageD.setText(pourcentageD + "%");
        this.resultatD.setText("" + v.getOption2());
        this.resultatD.setPrefHeight(pourcentageD);
        this.resultatD.setBackground(new Background(new BackgroundFill(Color.rgb(80, 0, 0, 0.7), new CornerRadii(5.0), new Insets(-5.0))));

        vueListeVote.setFlou();
        this.show();

    }

}