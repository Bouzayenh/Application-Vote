package dataobject;

import dataobject.Chiffre;
import dataobject.ClePublique;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;

/**
 * Contient toutes les méthodes de chiffrement.<br>
 * Elles suivent le protocole <a href="https://fr.wikipedia.org/wiki/Cryptosyst%C3%A8me_de_ElGamal">ElGamal</a>.
 */
public class Chiffrement {
    private static SecureRandom random;

    static {
        random = new SecureRandom();
    }

    /**
     * Génère une paire de clé de chiffrement.
     * @param l La longueur, en nombre de bits, des clés générées.
     * @return Les 3 entiers (p, g, h) composant la clé publique, puis celui (x) composant la clé privée.
     */
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

    /**
     * Déchiffre par tâtonnement. Si aucune valeur n'est trouvée, renvoie -1.
     * @param chiffre Le {@link Chiffre} (u, v) à déchiffrer.
     * @param max La valeur maximale que peut représenter le chiffré.
     * @param clePublique La {@link ClePublique} qui a permis le chiffrement.
     * @param clePrivee La clé privée (x) permettant le déchiffrement.
     * @return La valeur une fois déchiffrée si trouvée, -1 sinon.
     */
    public static int decrypt(Chiffre chiffre, int max, ClePublique clePublique, BigInteger clePrivee) {
        BigInteger p, g, M;
        p = clePublique.getP();
        g = clePublique.getG();
        // def M
        M = chiffre.getV().multiply(chiffre.getU().modPow(clePrivee.multiply(BigInteger.valueOf(-1)), p)).mod(p);
        // def m (message en clair)
        int m = 0;
        while (!M.equals(g.modPow(BigInteger.valueOf(m), p))) {
            if (m >= max)
                return -1;
            m++;
        }
        return m;
    }

    /**
     * Effectue le chiffrement selon le crypto-système de ElGamal.
     * @param m L'entier à chiffrer.
     * @param clePublique La clé publique permettant le chiffrement.
     * @return Un {@link Chiffre} représentant m chiffré selon le crypto-système de ElGamal.
     */
    public static Chiffre encrypt(int m, ClePublique clePublique, BigInteger r) {
        BigInteger p, g, h, pPrime;

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

    // maudit
    public static Chiffre encrypt(int m, ClePublique clePublique) {
        return null;
    }

    /**
     * Agrège deux chiffrés issus de la même clé plublique.
     * @param c1 {@link Chiffre} à agréger.
     * @param c2 {@link Chiffre} à agréger.
     * @param clePublique La {@link ClePublique} ayant permis le chiffrement de c1 et c2.
     * @return Un nouveau {@link Chiffre} représentant la somme de c1 et c2.
     */
    public static synchronized Chiffre agreger(Chiffre c1, Chiffre c2, ClePublique clePublique) {
        BigInteger p = clePublique.getP();

        // def Chiffré agrégé
        return new Chiffre(c1.getU().multiply(c2.getU()).mod(p), c1.getV().multiply(c2.getV()).mod(p));
    }
}
