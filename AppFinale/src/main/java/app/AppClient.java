package app;

import controller.Client;
import dataobject.Vote;
import exception.AuthentificationRefuseeException;
import exception.BulletinInvalideException;

import java.io.IOException;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // initialisation
            System.out.println("Création du client...");
            Client client = new Client();
            System.out.println("Client connecté");

            // authentification
            System.out.print("Entrez votre login : ");
            String login = sc.nextLine();
            System.out.println("Entrez votre mot de passe : ");
            String mdp = sc.nextLine();
            client.authentification(login, mdp);
            System.out.println("Authentification réussie");

            // boucle principale
            String input;
            do {
                System.out.println("Choisir une action : \n- [c] consulter le vote en cours\n- [v] voter\n- [q] quitter");
                input = sc.nextLine();
                switch (input) {
                    case "v":
                        System.out.print("Entrez votre choix : ");
                        int bulletin = sc.nextInt();

                        try {
                            client.voter(bulletin);

                        } catch (BulletinInvalideException e) {
                            e.printStackTrace(); // debug
                            System.out.println(e.getMessage());
                        }
                        break;
                    case "c":
                        Vote vote = client.consulterVoteEnCours();
                        System.out.println(
                                "Intitulé : " + vote.getIntitule() +
                                        "\n1 - " + vote.getOption1() +
                                        "\n2 - " + vote.getOption2()
                        );
                        break;
                    case "q":
                        client.deconnexion();
                        break;
                    default:
                        System.out.println("Commande non reconnue");
                }
            } while (!input.equals("v") && !input.equals("q"));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // debug
            System.out.println("Impossible de se connecter au serveur");
        } catch (AuthentificationRefuseeException e) {
            e.printStackTrace(); // debug
            System.out.println(e.getMessage());
        }
    }
}
