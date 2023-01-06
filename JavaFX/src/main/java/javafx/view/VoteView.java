package javafx.view;

import dataobject.Vote;
import javafx.ApplicationIHM;
import javafx.controller.AuthentificationController;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Time;

public class VoteView extends Stage {

    private Scene scene;
    private AnchorPane anchorPane;
    private ListeVoteView vueListeVote;
    private Label intituleVote;
    private Button btnChoix1;
    private Button btnChoix2;

    public VoteView(ListeVoteController c, ListeVoteView v) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueVote.fxml"));
        fxmlLoader.setController(c);
        vueListeVote = v;
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
        intituleVote = c.getVoteIntituleVote();
        btnChoix1 = c.getVotebtnChoix1();
        btnChoix2 = c.getVotebtnChoix2();
    }

    public void afficherVote(Vote v){

        intituleVote.setText(v.getIntitule());

        btnChoix1.setText(v.getOption1());
        btnChoix1.setOnAction(actionEvent -> {
            vueListeVote.cacherVueVote();
            vueListeVote.afficherVueChoix(v,1);
        });

        btnChoix2.setText(v.getOption2());
        btnChoix2.setOnAction(actionEvent -> {
            vueListeVote.cacherVueVote();
            vueListeVote.afficherVueChoix(v,2);
        });

        this.show();

    }
}
