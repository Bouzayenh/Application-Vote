public class AppClient {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Création du client...");
        Client client = new Client();
        System.out.println("Client connecté");

        System.out.println(client.consulterVoteEnCour());
        client.voter();
    }
}
