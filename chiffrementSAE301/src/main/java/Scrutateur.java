import java.math.BigInteger;
import java.util.Random;

public class Scrutateur {



    public Scrutateur() {
    }

    public PaireDeCles keyGen(int l){
        BigInteger p, pPrime, g, h, x;
        Random random = new Random();

        do {
            pPrime = BigInteger.probablePrime(l, random);
            p = pPrime.multiply(BigInteger.TWO).add(BigInteger.ONE);
        }while(!p.isProbablePrime(20));

        do {
            g = new BigInteger(p.bitLength(), random);
        }while(!g.modPow(pPrime, p).equals(BigInteger.ONE));

        x = new BigInteger(pPrime.bitLength(), random);

        h = g.modPow(x, p);


        ClePrivee clePrivee = new ClePrivee(x, p);
        ClePublique clePublique = new ClePublique(p, g, h);
        PaireDeCles paireDeCles = new PaireDeCles(clePrivee, clePublique);

        return paireDeCles;
    }

    public BigInteger[] encrypt(byte m, ClePublique cP){
        BigInteger[] c = new BigInteger[2];
        BigInteger p, pPrime, g, h, r;
        Random random = new Random();

        p = cP.getP();
        pPrime = p.add(BigInteger.valueOf(-1)).divide(BigInteger.TWO);
        g = g = cP.getG();
        h = cP.getH();
        r = new BigInteger(pPrime.bitLength(), random);

        c[0] = g.modPow(r, p);
        c[1] = g.pow(m).multiply(h.modPow(r, p));

        return c;
    }

    public int decrypt(BigInteger[] chiffre, ClePrivee cP){
        int m;
        BigInteger grandM, u, v, x, p;
        p = cP.getP();
        u = chiffre[0];
        v = chiffre[1];
        x = cP.getX();

        grandM = v.multiply(u.modPow(x.multiply(BigInteger.valueOf(-1)), p));


        return 0;
    }
}
