package app;

import controller.Scrutateur;

public class AppScrutateur {

    public static void main(String[] args) {
        // initialisation
        System.out.println("Création du scrutateur...");
        Scrutateur scrutateur = new Scrutateur(100);
        System.out.println("Scrutateur connecté");

        // mise en route
        scrutateur.run();
    }
}
