package app;

import controller.Scrutateur;

import java.io.IOException;
import java.sql.SQLException;

public class AppScrutateur {

    public static void main(String[] args) {
        try {
            // initialisation
            Scrutateur scrutateur = new Scrutateur(100);
            System.out.println("Connecté avec succès. Scrutateur lancé");
            scrutateur.run();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur critique : Impossible de se connecter au serveur. Arrêt du scrutateur");
        } catch (SQLException e) {
            System.out.println("Erreur critique : Impossible de se connecter à la base de données. Arrêt du scrutateur");
        }
    }
}
