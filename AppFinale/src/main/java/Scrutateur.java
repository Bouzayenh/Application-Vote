import java.io.*;
import java.net.Socket;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Scrutateur {
    private int l;
    // clé publique
    private ClePublique clePublique;
    // clé privée
    private BigInteger x;

    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public Scrutateur(int l) {
        try {
            this.l = l;

            // demande de connexion au serveur
            serveurSocket = new Socket("localhost", 2999);
            outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
            inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        } catch (IOException ignored) {}
    }

    public boolean estConnecte() {
        return serveurSocket != null && !serveurSocket.isClosed();
    }

    public void run() {
        try {
            while (true) {
                // attend une requête du serveur
                Requete requete = (Requete) inputServeur.readObject();
                System.out.println(requete); // debug

                // traîte la requête
                switch (requete) {
                    case SERVEUR_DEMANDER_CLE_PUBLIQUE:
                        outputServeur.writeObject(clePublique);
                        break;
                    case SERVEUR_CREER_VOTE:
                        keygen();
                        break;
                    case SERVEUR_DEMANDER_DECHIFFREMENT:
                        outputServeur.writeObject(decrypt((Chiffre) inputServeur.readObject()));
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Serveur déconnecté");
        }
    }

    public void keygen() {
        BigInteger p, g, h, pPrime;
        SecureRandom random = new SecureRandom();

        // def p, p'
        do {
            p = BigInteger.probablePrime(l, random);
            pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);
        } while (!pPrime.isProbablePrime(40));

        // def g
        do {
            g = new BigInteger(p.bitLength(), random);
        } while (g.compareTo(p) >= 0 || !g.modPow(pPrime, p).equals(BigInteger.ONE));


        // def x (clé privée)
        do {
            x = new BigInteger(pPrime.bitLength(), random);
        } while (x.compareTo(pPrime) >= 0);

        // def h
        h = g.modPow(x, p);

        clePublique = new ClePublique(p, g, h);
    }

    public int decrypt(Chiffre chiffre) {
        BigInteger p, g, M;

        p = clePublique.getP();
        g = clePublique.getG();
        // def M
        M = chiffre.getV().multiply(chiffre.getU().modPow(x.multiply(BigInteger.valueOf(-1)), p)).mod(p);

        // def m (message en clair)
        int m = 0;
        while (!M.equals(g.modPow(BigInteger.valueOf(m), p))) {
            if (m == Integer.MAX_VALUE) return -1;
            m++;
        }

        return m;
    }
}