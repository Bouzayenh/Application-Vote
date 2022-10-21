public class AppClient {

    public static void main(String[] args) {
        System.out.println("Création du client...");
        Client client = new Client();
        System.out.println("Client connecté");

        client.voter();
    }
}
