package javafx.controller;

import controller.Client;
import dataobject.Utilisateur;
import javafx.ApplicationIHM;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.view.ListeVoteView;

public class ListeVoteController {

    @FXML
    private HBox VotesContaining;
    private ApplicationIHM myApp;

    public void setMyApp(ApplicationIHM app) {
        myApp= app;
    }

    public Client getClient() {
        return myApp.getClient();
    }
}
