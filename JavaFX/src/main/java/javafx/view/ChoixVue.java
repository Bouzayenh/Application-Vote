package javafx.view;

import dataobject.Vote;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ChoixVue extends Stage {

    private Scene scene;
    private AnchorPane anchorPane;
    private int choix;
    private int idVote;

    public ChoixVue(ListeVoteController c) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(AuthentificationView.class.getResource("/javafx/vueChoix.fxml"));
        fxmlLoader.setController(c);
        scene = new Scene(fxmlLoader.load());
        this.setScene(scene);
        VBox root = (VBox) this.scene.getRoot();
        for ( Node element : root.getChildren()){
            if(element instanceof AnchorPane) {
                anchorPane = (AnchorPane) element;
            }
        }
        this.initStyle(StageStyle.UNDECORATED);
    }

    public void afficherChoix(Vote v, int choix){

        this.choix = choix;
        this.idVote = v.getIdentifiant();

        for ( Node element : anchorPane.getChildren()) {

            if (element.getId().equals("IntituleVote")) {

                ((Label)element).setText(v.getIntitule());
            }
            if (element.getId().equals("labelChoix")) {

                if( choix == 1 ){
                    ((Label)element).setText(v.getOption1());
                }else {
                    ((Label)element).setText(v.getOption2());
                }
            }
        }
        this.show();
    }

    public int getChoix(){
        return choix;
    }

    public int getIdVote(){
        return idVote;
    }

}
