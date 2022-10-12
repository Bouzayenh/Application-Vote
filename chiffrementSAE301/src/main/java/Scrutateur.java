import java.math.BigInteger;
import java.security.SecureRandom;

public class Scrutateur {
    private SecureRandom random;
    // clé publique
    private ClePublique clePublique;
    // clé privée
    private BigInteger x;

    public Scrutateur(int l) {
        random = new SecureRandom();
        BigInteger pPrime, p, g, h;

        // algorithme keygen
        do { // def p, p'
            pPrime = BigInteger.probablePrime(l, random);
            p = pPrime.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(40));

        do { // def g
            g = new BigInteger(p.bitLength(), random);
        } while (g.compareTo(p) >= 0 || !g.modPow(pPrime, p).equals(BigInteger.ONE));

        do { // def x
            x = new BigInteger(pPrime.bitLength(), random);
        } while (x.compareTo(pPrime) >= 0);

        h = g.modPow(x, p); // def h

        clePublique = new ClePublique(p, g, h);
    }

    public Chiffre encrypt(int m) { // algorithme encrypt (pas dans Scrutateur à terme)
        // utilise p, g, h et random
        BigInteger pPrime, r, p, g, h;

        p = clePublique.getP();
        g = clePublique.getG();
        h = clePublique.getH();

        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);


        do { // def r
            r = new BigInteger(pPrime.bitLength(), random);
        } while (r.compareTo(pPrime) >= 0);

        // def chiffré
        return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p)));
    }

    public int decrypt(Chiffre chiffre) { // algorithme decrypt
        // def M
        BigInteger M, p, g;

        p = clePublique.getP();
        g = clePublique.getG();
        M = chiffre.getV().multiply(chiffre.getU().modPow(x.multiply(BigInteger.valueOf(-1)), p)).mod(p);

        int m = 0; // def m
        while (!M.equals(g.modPow(BigInteger.valueOf(m), p))) {
            m++;
        }

        return m;
    }

    public Chiffre agreger(Chiffre c1, Chiffre c2) { // algorithme agreger (pas dans Scrutateur à terme)
        BigInteger p = clePublique.getP();
        return new Chiffre(c1.getU().multiply(c2.getU()).mod(p), c1.getV().multiply(c2.getV()).mod(p));
    }

    public ClePublique getClePublique() {
        return clePublique;
    }
}