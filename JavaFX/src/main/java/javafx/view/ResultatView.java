package javafx.view;

import dataobject.Vote;
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

import java.io.IOException;

public class ResultatView extends Stage {

    private Scene scene;
    private AnchorPane anchorPane;
    private int choix;
    private int idVote;
    private ListeVoteView vueListeVote;

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

        this.initStyle(StageStyle.UNDECORATED);
    }

    public void afficherResultat(Vote v) {

        int resultat = 0;
        for (Node element : anchorPane.getChildren()) {

            if (element.getId().equals("IntituleVote")) {

                ((Label) element).setText(v.getIntitule());

            } else if (element.getId().equals("hbox")) {
                for (Node vbox : ((HBox) element).getChildren()) {

                    for (Node fils : ((VBox) vbox).getChildren()) {
                        if (fils.getId().equals("pourcentageG")) {
                            ((Label) fils).setText(((double) (int) ((1 - v.getResultat()) * 10000)) / 100 + "%");

                        } else if (fils.getId().equals("resultatG")) {
                            ((Label) fils).setText("" + v.getOption1());
                            ((Label) fils).setPrefHeight(((double) (int) ((1 - v.getResultat()) * 10000)) / 100);
                            ((Label) fils).setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-5.0))));

                        } else if (fils.getId().equals("pourcentageD")) {
                            ((Label) fils).setText(((double) (int) (v.getResultat() * 10000)) / 100 + "%");

                        } else if (fils.getId().equals("resultatD")) {
                            ((Label) fils).setText("" + v.getOption2());
                            ((Label) fils).setPrefHeight(((double) (int) (v.getResultat() * 10000)) / 100);
                            ((Label) fils).setBackground(new Background(new BackgroundFill(Color.rgb(80, 0, 0, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
                        }
                    }
                }
                this.show();
            }
        }
    }
}