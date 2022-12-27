package javafx.view;

import dataobject.Vote;
import javafx.controller.ListeVoteController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
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
        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        this.initStyle(StageStyle.UNDECORATED);
    }

    public void afficherChoix(Vote v, int choix){

        this.choix = choix;
        this.idVote = v.getIdentifiant();

        for ( Node element : anchorPane.getChildren()) {

            if (element.getId().equals("IntituleVote")) {
                String intitule = v.getIntitule();
                ((Label)element).setText(intitule);
                if(intitule.length()>24) ((Label)element).setFont(new Font(15));
            }
            if (element.getId().equals("labelChoix")) {
                String voteChoisis;
                if( choix == 1 ){
                    voteChoisis = v.getOption1();
                }else {
                    voteChoisis = v.getOption2();
                }
                ((Label)element).setText(voteChoisis);
                if(voteChoisis.length()>24) ((Label)element).setFont(new Font(15));

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
