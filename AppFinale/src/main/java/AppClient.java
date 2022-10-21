import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        System.out.println("Création du client...");
        Client client = new Client();
        System.out.println("Client connecté");

        Scanner reader = new Scanner(System.in);
        System.out.print("Entier à chiffrer: ");
        System.out.println(client.encrypt(reader.nextInt()));
    }
}
