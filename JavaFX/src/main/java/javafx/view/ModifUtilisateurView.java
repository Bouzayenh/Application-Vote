package javafx.view;

import controller.Client;
import javafx.ApplicationIHM;
import javafx.controller.ListeVoteController;
import javafx.controller.ProfilController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ModifUtilisateurView extends Stage {

    private Scene scene;


    private ProfilController modifController;

    private ApplicationIHM myAppli;

    private Client myClient;


    public ModifUtilisateurView(ListeVoteController c, ListeVoteView v) throws IOException {
        FXMLLoader fxml= new FXMLLoader(ModifUtilisateurView.class.getResource("/xml/VueModifUtilisateur.fxml"));
        fxml.setController(c);
        scene = new Scene(fxml.load());
        this.initModality(Modality.WINDOW_MODAL);
        this.initOwner(v.getScene().getWindow());

        this.setTitle("Modifcation Utilisateur");
        this.setScene(scene);

       // modifController = fxml.getController();

       // myClient = myAppli.getClient();

        VBox root = (VBox) this.scene.getRoot();
        root.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        this.initStyle(StageStyle.UNDECORATED);


    }

    public void setterForController() throws Exception {
        modifController.setModifView(this);
    }

    public void afficher() {
        this.show();
    }

    public ApplicationIHM getMyApp() {
        return myAppli;
    }

    public Client getMyClient() {
        return myClient;
    }
}
