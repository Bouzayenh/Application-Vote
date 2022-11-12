package app;

import controller.Scrutateur;

public class AppScrutateur {

    public static void main(String[] args) {
        System.out.println("Création du scrutateur...");
        Scrutateur scrutateur = new Scrutateur(100);
        System.out.println("Controller.Scrutateur connecté");
        scrutateur.run();
    }
}
