package app;

import controller.Database;

public class AppDatabase {

    public static void main(String[] args) {
        System.out.println("connection au bdd");
        Database bdd = new Database();
        if (bdd.estConnecte()) {
            System.out.println("Base de données connecté\n");
            bdd.run();
        } else System.out.println("La connexion avec le serveur n'a pas pu être établie");

    }
}
