import java.math.BigInteger;
import java.util.Scanner;

public class AppScrutateur {

    public static void main(String[] args) {
        System.out.println("Création du scrutateur...");
        Scrutateur scrutateur = new Scrutateur(100);
        System.out.println("Scrutateur connecté\nLancement du scrutateur x1...");
        scrutateur.run();
        System.out.println("Requête traitée x1\nLancement du scrutateur x2...");
        scrutateur.run();
        System.out.println("Requête traitée x2");

        Scanner reader = new Scanner(System.in);
        System.out.print("u du chiffré à déchiffrer : ");
        BigInteger u = new BigInteger(reader.next());
        System.out.print("v du chiffré à déchiffrer : ");
        BigInteger v = new BigInteger(reader.next());
        System.out.println(scrutateur.decrypt(new Chiffre(u, v)));
    }
}
