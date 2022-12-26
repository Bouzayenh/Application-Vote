package javafx.view;

import dataobject.Vote;
import javafx.ApplicationIHM;
import javafx.controller.AuthentificationController;
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
import java.sql.Time;

public class VoteView extends Stage {

    private Scene scene;
    private AnchorPane anchorPane;
    private ListeVoteView vueListeVote;

    public VoteView(ListeVoteController c, ListeVoteView v) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueVote.fxml"));
        fxmlLoader.setController(c);
        vueListeVote = v;
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
    }

    public void afficherVote(Vote v){

        for ( Node element : anchorPane.getChildren()) {

            if (element.getId().equals("IntituleVote")) {

                ((Label)element).setText(v.getIntitule());

            }else if (element.getId().equals("btnChoix1")) {

                ((Button)element).setText(v.getOption1());
                ((Button)element).setOnAction(actionEvent -> {
                    vueListeVote.cacherVueVote();
                    vueListeVote.afficherVueChoix(v,1);
                });

            }else if (element.getId().equals("btnChoix2")) {

                ((Button)element).setText(v.getOption2());
                ((Button)element).setOnAction(actionEvent -> {
                    vueListeVote.cacherVueVote();
                    vueListeVote.afficherVueChoix(v,2);
                });

            }
        }
        this.show();
        vueListeVote.setFlou();

    }
}
