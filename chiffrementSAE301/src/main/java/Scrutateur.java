import javax.crypto.CipherInputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

public class Scrutateur {
    private SecureRandom random;
    // clé publique
    private BigInteger p;
    private BigInteger g;
    private BigInteger h;
    // clé privée
    private BigInteger x;

    public Scrutateur(int l) {
        random = new SecureRandom();
        BigInteger pPrime;

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
    }

    public Chiffre encrypt(int m) { // algorithme encrypt (pas dans Scrutateur à terme)
        // utilise p, g, h et random
        BigInteger pPrime, r;
        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);

        do { // def r
            r = new BigInteger(pPrime.bitLength(), random);
        } while (r.compareTo(pPrime) >= 0);

        // def chiffré
        return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p).mod(p)));
    }

    public int decrypt(Chiffre chiffre) { // algorithme decrypt
        // def M
        BigInteger M = chiffre.getV().multiply(chiffre.getU().modPow(x.multiply(BigInteger.valueOf(-1)), p)).mod(p);

        int m = 0; // def m
        while (!M.equals(g.modPow(BigInteger.valueOf(m), p))) {
            m++;
        }

        return m;
    }
}