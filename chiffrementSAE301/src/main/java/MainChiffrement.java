import java.util.Scanner;

public class MainChiffrement {
    static Scrutateur scrutateur;
    static Chiffre chiffre;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scrutateur = new Scrutateur(200);

        do {
            System.out.println("[c:chiffrer, d:déchiffrer, a:agréger, x:sortir]");
        } while(lireLigne(scanner.nextLine()));

    }

    public static boolean lireLigne(String ligne){
        char commande = ligne.charAt(0);
        int message;

        try {
            message = Integer.parseInt(ligne.substring(1));
        }catch (Exception e){
            message = 0;
        }



        switch (commande){
            case 'c' :
                chiffrer(message);
                break;
            case 'd' :
                dechiffrer();
                break;
            case 'a' :
                agreger(message);
                break;
            case 'x' : return false;
        }
        return true;
    }

    private static void agreger(int message) {
        System.out.println("Agregation...");
        chiffre = scrutateur.agreger(chiffre, scrutateur.encrypt(message));
        System.out.println("Message chiffré, puis aggrégé avec le chiffré courant");
        System.out.println("Voici le chiffré courant : " + chiffre.toString());
    }

    private static void dechiffrer() {
        System.out.println("Déchiffrement...");
        try{
            System.out.println("Voici le chiffré courant après déchiffrement : " + scrutateur.decrypt(chiffre));
        }catch (Exception e){
            System.out.println("Aucun message chiffré pour le moment");
        }
    }

    private static void chiffrer(int message) {
        System.out.println("Chiffrement...");
        chiffre = scrutateur.encrypt(message);
        System.out.println("Voici le chiffré courant : " + chiffre.toString());
    }
}
