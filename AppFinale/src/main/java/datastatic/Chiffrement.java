package datastatic;

import dataobject.Chiffre;
import dataobject.ClePublique;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Chiffrement {

    private static SecureRandom random;

    static {
        random = new SecureRandom();
    }

    public static BigInteger[] keygen(int l) {
        BigInteger p, g, h, x, pPrime;
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

        return new BigInteger[]{p, g, h, x};
    }

    public static int decrypt(Chiffre chiffre, ClePublique clePublique, BigInteger clePrivee) {
        BigInteger p, g, M;

        p = clePublique.getP();
        g = clePublique.getG();
        // def M
        M = chiffre.getV().multiply(chiffre.getU().modPow(clePrivee.multiply(BigInteger.valueOf(-1)), p)).mod(p);

        // def m (message en clair)
        int m = 0;
        while (!M.equals(g.modPow(BigInteger.valueOf(m), p))) {
            if (m == Integer.MAX_VALUE) return -1;
            m++;
        }

        return m;
    }

    public static Chiffre encrypt(int m, ClePublique clePublique) {
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
    }

    public static synchronized Chiffre agreger(Chiffre c1, Chiffre c2, ClePublique clePublique) {
        BigInteger p = clePublique.getP();

        // def Chiffré agrégé
        return new Chiffre(c1.getU().multiply(c2.getU()).mod(p), c1.getV().multiply(c2.getV()).mod(p));
    }
}
