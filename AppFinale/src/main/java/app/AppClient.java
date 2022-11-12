package app;

import controller.Client;

import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        System.out.println("Création du client...");
        Client client = new Client();
        Scanner sc = new Scanner(System.in);
        
        if (client.estConnecte()) {
            System.out.println("Controller.Client connecté");

            System.out.print("Login : ");
            String login = sc.nextLine();

            System.out.println("Mot de passe : ");
            String mdp = sc.nextLine();

            if (client.connexion(login, mdp)){
                System.out.println("Authentification réussi");
                String input;


                do {
                    System.out.println("Choisir une action : \n- consulter le vote en cours [c]\n- voter [v]\n- quitter [q]");
                    input = sc.nextLine();

                    switch (input) {
                        case "v":
                            client.voter();
                            break;
                        case "c":
                            client.consulterVoteEnCours();
                            break;
                        case "q":
                            client.deconnexion();
                            break;
                        default:
                            System.out.println("Commande non reconnue");
                    }

                } while (!input.equals("v") && !input.equals("q"));
            }else System.out.println("Authentification non permise");
            
        } else System.out.println("La connexion avec le serveur n'a pas pu être établie");
    }
}
