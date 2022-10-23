public class AppServeur {

    public static void main(String[] args) {
        System.out.println("Création du serveur... En attente de connexion du scrutateur");
        Serveur serveur = new Serveur();
        System.out.println("Scrutateur connecté\nCréation du vote...");
        serveur.creerVote("Quel parfum ?", "Noisette", "Vanille");
        System.out.println("Vote créé");

        System.out.println("Serveur lancé, connexions client possibles");
        serveur.run();
    }
}
