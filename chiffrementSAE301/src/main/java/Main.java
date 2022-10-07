import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Scrutateur scrutateur = new Scrutateur(10);
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int n1 = random.nextInt(10);
            int n2 = random.nextInt(10);
            Chiffre c1 = scrutateur.encrypt(n1);
            Chiffre c2 = scrutateur.encrypt(n2);
            Chiffre c = scrutateur.agreger(c1, c2);
            int m = scrutateur.decrypt(c);
            System.out.println(n1 + " + " + n2 + " = " + m + " | " + c1 + " + " + c2 + " = " + c);
        }
    }
}
