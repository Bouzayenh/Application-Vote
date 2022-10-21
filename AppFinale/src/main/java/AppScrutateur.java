public class AppScrutateur {

    public static void main(String[] args) {
        System.out.println("Création du scrutateur...");
        Scrutateur scrutateur = new Scrutateur(100);
        System.out.println("Scrutateur connecté");

        System.out.println("Scrutateur lancé");
        scrutateur.run();
    }
}
