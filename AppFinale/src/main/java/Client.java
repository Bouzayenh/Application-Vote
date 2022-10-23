import java.io.*;
import java.net.Socket;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class Client {
    private SecureRandom random;
    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Client() {
        try {
            random = new SecureRandom();

            // demande de connexion au serveur
            serveurSocket = new Socket("localhost", 2999);
            outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
            inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        } catch (IOException ignored) {}
    }

    public boolean estConnecte() {
        return serveurSocket != null && !serveurSocket.isClosed();
    }

    public void voter() {
        try {
            // entrée du bulletin
            Scanner sc = new Scanner(System.in);
            int bulletin = 0;
            while (bulletin != 1 && bulletin != 2) {
                System.out.println("Entrez le numéro de l'option souhaitée : 1 ou 2");
               
                bulletin = sc.nextInt();
                if (bulletin != 1 && bulletin != 2) System.out.println("Sélection incorrecte");
            }
            Chiffre chiffre = encrypt(bulletin-1);

            outputServeur.writeObject(Requete.CLIENT_VOTER);
            outputServeur.writeObject(chiffre);

        } catch (IOException ignored) {}
    }

    public void consulterVoteEnCours() {
        try {
            outputServeur.writeObject(Requete.CLIENT_DEMANDER_VOTE_EN_COURS);
            Vote vote = (Vote) inputServeur.readObject();
            System.out.println(
                    "Intitulé : " + vote.getIntitule() +
                            "\n1 - " + vote.getOption1() +
                            "\n2 - " + vote.getOption2()
            );

        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public ClePublique demanderClePublique() throws IOException, ClassNotFoundException {
        outputServeur.writeObject(Requete.CLIENT_DEMANDER_CLE_PUBLIQUE);
        return (ClePublique) inputServeur.readObject();
    }

    public Chiffre encrypt(int m) {
        try {
            ClePublique clePublique = demanderClePublique();
            BigInteger p, g, h, pPrime, r;

            p = clePublique.getP();
            g = clePublique.getG();
            h = clePublique.getH();
            // récupération p'
            pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);

            // def r
            do {
                r = new BigInteger(pPrime.bitLength(), random);
            } while (r.compareTo(pPrime) >= 0);

            // def Chiffré
            return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p)).mod(p));

        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}