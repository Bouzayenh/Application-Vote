package app;

import controller.Scrutateur;

import java.io.IOException;

public class AppScrutateur {

    public static void main(String[] args) {
        try {
            // initialisation
            System.out.println("Connexion au serveur...");
            Scrutateur scrutateur = new Scrutateur(100);
            System.out.println("Connecté avec succès. Scrutateur lancé");
            scrutateur.run();

        } catch (IOException e) {
            System.out.println("Erreur critique : Impossible de se connecter au serveur. Arrêt du scrutateur");
        }
    }
}
