import java.util.Scanner;

public class AppServeur {

    public static void main(String[] args) {
        System.out.println("Création du serveur... En attente de connexion du scrutateur");
        Serveur serveur = new Serveur();
        System.out.println("Scrutateur connecté\nCréation du vote...");
        serveur.creerVote("Quel parfum ?", "Noisette", "Vanille");
        System.out.println("Vote créé");

        System.out.println("Serveur lancé, connexions client possibles");
        serveur.run();

        //gestion des choix possibles de l'administrateur
        Scanner sc = new Scanner(System.in);
        String input;

        do {
            input = sc.nextLine();
        } while (!input.equals("x"));

        System.out.println("Vote terminé");
        serveur.arreterVote();

        System.out.println(serveur.consulterResultats());
    }
}
