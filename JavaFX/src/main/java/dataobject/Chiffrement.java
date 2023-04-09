package dataobject;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;

/**
 * Contient toutes les méthodes de chiffrement.<br>
 * Elles suivent le protocole <a href="https://fr.wikipedia.org/wiki/Cryptosyst%C3%A8me_de_ElGamal">ElGamal</a>.
 */
public class Chiffrement {
    private static final SecureRandom RANDOM;

    static {
        RANDOM = new SecureRandom();
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
        if (r == null) do {
            r = new BigInteger(pPrime.bitLength(), RANDOM);
        } while (r.compareTo(pPrime) >= 0);

        // def Chiffré
        return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p)).mod(p));
    }

    // maudit
    public static Chiffre encrypt(int m, ClePublique clePublique) {
        BigInteger p, g, h, pPrime, r;

        p = clePublique.getP();
        g = clePublique.getG();
        h = clePublique.getH();
        // récupération p'
        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);

        // def r
        do {
            r = new BigInteger(pPrime.bitLength(), RANDOM);
        } while (r.compareTo(pPrime) >= 0);

        // def Chiffré
        return new Chiffre(g.modPow(r, p), g.modPow(BigInteger.valueOf(m), p).multiply(h.modPow(r, p)).mod(p));
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

    public static Preuve proofgen(int m, Chiffre c, ClePublique clePublique, BigInteger r) {

        BigInteger p, g, h, pPrime, chall0, rep0, chall1, rep1, A0, B0, A1, B1, gamma;

        p = clePublique.getP();
        g = clePublique.getG();
        h = clePublique.getH();
        // récupération p'
        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);
        gamma = new BigInteger(pPrime.bitLength(), RANDOM);

        if (m == 0){ //TODO : c'est immonde, y'a moyen de permutter des valeurs et de réduire par 2 cette immondice
            //P1
            chall1 = new BigInteger(pPrime.bitLength(), RANDOM);
            rep1 = new BigInteger(pPrime.bitLength(), RANDOM);
            A1 = g.modPow(rep1, p).multiply(c.getU().modPow(chall1, p)).mod(p);
            B1 = h.modPow(rep1, p).multiply(c.getV().multiply(g.modInverse(p)).modPow(chall1, p)).mod(p);

            // P0
            A0 = g.modPow(gamma, p);
            B0 = h.modPow(gamma, p);

            chall0 = hacher(c.getU(), c.getV(), A0, B0, A1, B1).mod(pPrime).subtract(chall1).mod(pPrime);

            rep0 = gamma.subtract(r.multiply(chall0)).mod(pPrime);
        }
        else if (m == 1){
            //P0
            chall0 = new BigInteger(pPrime.bitLength(), RANDOM);
            rep0 = new BigInteger(pPrime.bitLength(), RANDOM);
            A0 = g.modPow(rep0, p).multiply(c.getU().modPow(chall0, p)).mod(p);
            B0 = h.modPow(rep0, p).multiply(c.getV().modPow(chall0, p)).mod(p);

            //P1
            A1 = g.modPow(gamma, p);
            B1 = h.modPow(gamma, p);
            chall1 = hacher(c.getU(), c.getV(), A0, B0, A1, B1).mod(pPrime).subtract(chall0).mod(pPrime);
            rep1 = gamma.subtract(r.multiply(chall1)).mod(pPrime);
        }
        else throw new IllegalArgumentException("Le message est différent de 0 ou 1");

        return new Preuve(chall0, rep0, chall1, rep1, A0, B0, A1, B1);
    }

    public static boolean proofCheck(ClePublique clePublique, Chiffre c, Preuve preuve) {

        BigInteger p, g, h, pPrime;
        p = clePublique.getP();
        g = clePublique.getG();
        h = clePublique.getH();
        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);

        //on vérifie p0
        boolean checkA0 = g.modPow(preuve.getRep0(), p).multiply(c.getU().modPow(preuve.getChall0(), p)).mod(p)      .equals(       preuve.getA0());
        boolean checkB0 = h.modPow(preuve.getRep0(), p).multiply(c.getV().modPow(preuve.getChall0(), p)).mod(p)      .equals(       preuve.getB0());

        //on vérifie p1
        boolean checkA1 = g.modPow(preuve.getRep1(), p).multiply(c.getU().modPow(preuve.getChall1(), p)).mod(p)                               .equals(       preuve.getA1());
        boolean checkB1 = h.modPow(preuve.getRep1(), p).multiply(c.getV().multiply(g.modInverse(p)).modPow(preuve.getChall1(), p)).mod(p)     .equals(       preuve.getB1());

        //on vérifie que le haché soit le bon (cf. '2' dans 'vérification' de ZKor.pdf)
        boolean lesHachesSontEgaux = hacher(c.getU(), c.getV(), preuve.getA0(), preuve.getB0(), preuve.getA1(), preuve.getB1()).mod(pPrime)    .equals(    preuve.getChall0().add(preuve.getChall1()).mod(pPrime));

        System.out.println(checkA0 + " " + checkB0 + " " + checkA1 + " " + checkB1 + " " + lesHachesSontEgaux);



        return checkA0 & checkA1 & checkB0 & checkB1 & lesHachesSontEgaux;
    }

    /**
     * Hache la gueule des entiers passés en paramêtre après les avoir concaténé.
     */
    public static BigInteger hacher(BigInteger c1, BigInteger c2, BigInteger A0, BigInteger B0, BigInteger A1, BigInteger B1) {
        String concatene = c1.toString() + c2.toString() + A0.toString() + B0.toString() + A1.toString() + B1.toString();

        // je lui hache la gueule
        MessageDigest shake = null;
        try {
            shake = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignored) {}
        assert shake != null;

        return new BigInteger(1, shake.digest(concatene.getBytes()));
        //return new BigInteger(concatene);
    }
}
