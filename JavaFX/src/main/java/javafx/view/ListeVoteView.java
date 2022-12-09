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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Set;


public class ListeVoteView extends Stage {

    private Scene scene;
    private ListeVoteController listeVoteController;
    private AnchorPane anchorPane;

    public ListeVoteView(ApplicationIHM mainApp) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueListeVote.fxml"));
        scene = new Scene(fxmlLoader.load(), 620, 540);
        this.setTitle("Liste de votes");
        this.setScene(scene);
        listeVoteController = fxmlLoader.getController();
        listeVoteController.setMyApp(mainApp);
        VBox root = (VBox) this.scene.getRoot();
        for ( Node element : root.getChildren()){
            if(element instanceof AnchorPane) {
                anchorPane = (AnchorPane) element;
            }
        }

    }

    public void montrer() {
        Client c = listeVoteController.getClient();
        VBox root = (VBox) this.scene.getRoot();

        try {

            Set<Vote> votes = c.consulterVotes();
            for ( Node vbox : anchorPane.getChildren()) {
                if (vbox.getId().equals("VoteContaining")){
                    for (Vote v : votes) {

                        Label label = new Label("en cours");
                        Button BVote = new Button();

                        BVote.setText(v.getIntitule());
                        BVote.setPrefWidth(((VBox)vbox).getPrefWidth()-60);
                        BVote.setOnAction(actionEvent -> {
                            setFlou();
                        });

                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(BVote,label);
                        hbox.setPadding(new Insets(0.0,0.0,5.0,0.0));
                        ((VBox)vbox).getChildren().add(hbox);
                    }
                }
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
}

