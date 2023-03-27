package app;

import dataobject.Chiffre;
import dataobject.Chiffrement;
import dataobject.ClePublique;
import dataobject.Preuve;

import java.math.BigInteger;
import java.security.SecureRandom;


public class Main {

    private static int MESSAGE = 0;
    private static SecureRandom random;
    private static BigInteger r;

    static {
        random = new SecureRandom();
    }

    public static void main(String[] args) {
        BigInteger[] cles = Chiffrement.keygen(6);

        ClePublique pk = new ClePublique(cles[0], cles[1], cles[2]);
        BigInteger sk = cles[3];

        BigInteger pPrime = pk.getP().add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);

        do {
            r = new BigInteger(pPrime.bitLength(), random);
        } while (r.compareTo(pPrime) >= 0);

        int m = 0;
        Chiffre c = Chiffrement.encrypt(m, pk, r);
        Preuve p = Chiffrement.proofgen(m, c, pk, r);

        System.out.println(Chiffrement.proofCheck(pk, c, p));
    }
}