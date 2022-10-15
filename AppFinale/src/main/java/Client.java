import java.math.BigInteger;
import java.security.SecureRandom;

public class Client {
    private SecureRandom random;

    public Client() {
        random = new SecureRandom();
    }

    public Chiffre encrypt(int m, ClePublique clePublique) {
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
        return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p)));
    }
}