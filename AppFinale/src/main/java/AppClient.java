import java.io.IOException;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        System.out.println("Création du client...");
        Client client = new Client();
        
        if (client.estConnecte()) {
            System.out.println("Client connecté");


            if (client.connexion()){
                System.out.println("Authentification réussi");
                String input;
                Scanner sc = new Scanner(System.in);

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
                            break;
                        default:
                            System.out.println("Commande non reconnue");
                    }

                } while (!input.equals("v") && !input.equals("q"));
            }else System.out.println("Authentification non permise");
            
        } else System.out.println("La connexion avec le serveur n'a pas pu être établie");
    }
}
