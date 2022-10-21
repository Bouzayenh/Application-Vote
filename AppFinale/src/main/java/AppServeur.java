public class AppServeur {

    public static void main(String[] args) {
        System.out.println("Création du serveur... En attente de connexion du scrutateur et du client");
        Serveur serveur = new Serveur();
        System.out.println("Serveur créé\nCréation du vote...");
        serveur.creerVote("Quel parfum ?", "Noisette", "Vanille");
        System.out.println("Vote créé\nLancement du serveur x1...");
        serveur.run();
        System.out.println("Requête traitée");
    }
}
