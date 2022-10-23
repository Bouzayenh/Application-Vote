import java.io.IOException;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Création du client...");
        Client client;
        try{
            client = new Client();
            System.out.println("Client connecté");


            String input;
            Scanner sc = new Scanner(System.in);

            do {
                System.out.println("Choisir une action : \n-consulter le vote en cours [c]\n-voter [v]\n-quitter [q]");
                input = sc.nextLine();

                switch (input){
                    case "v" :
                        client.voter();
                        break;
                    case "c" :
                        System.out.println(client.consulterVoteEnCour());
                        break;
                    case "q" :
                        break;
                    default:
                        System.out.println("Commande non-reconnue");
                }

            }while(!input.equals("v") && !input.equals("q"));

        }catch (IOException e){
            System.out.println("La connexion au serveur n'a pas pu être établie");
        }
    }
}
