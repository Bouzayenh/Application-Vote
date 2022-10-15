import java.math.BigInteger;
import java.security.SecureRandom;

public class Scrutateur {
    // clé publique
    private ClePublique clePublique;
    // clé privée
    private BigInteger x;

    public Scrutateur(int l) {
        BigInteger p, g, h, pPrime;
        SecureRandom random = new SecureRandom();

        // def p, p'
        do {
            pPrime = BigInteger.probablePrime(l, random);
            p = pPrime.multiply(BigInteger.TWO).add(BigInteger.ONE);
        } while (!p.isProbablePrime(40));

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

    public ClePublique getClePublique() {
        return clePublique;
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
            m++;
        }

        return m;
    }
}