import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Scrutateur scrutateur = new Scrutateur(10);
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int n = random.nextInt(10);
            Chiffre c = scrutateur.encrypt(n);
            int m = scrutateur.decrypt(c);
            System.out.println("message original=" + n + ", message déchiffré=" + m);
        }
    }
}
