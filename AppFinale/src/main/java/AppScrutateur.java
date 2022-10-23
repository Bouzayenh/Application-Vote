public class AppScrutateur {

    public static void main(String[] args) {
        System.out.println("Création du scrutateur...");
        Scrutateur scrutateur = new Scrutateur(100);
        if (scrutateur.estConnecte()) {
            System.out.println("Scrutateur connecté\nScrutateur lancé");
            scrutateur.run();
        } else System.out.println("La connexion avec le serveur n'a pas pu être établie");

    }
}
