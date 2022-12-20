package dataobject;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Un triplet d'entier représentant une clé publique selon le crypto-système de ElGamal.
 */
public class ClePublique implements Serializable {
    private BigInteger p;
    private BigInteger g;
    private BigInteger h;

    public ClePublique(BigInteger p, BigInteger g, BigInteger h) {
        this.p = p;
        this.g = g;
        this.h = h;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getH() {
        return h;
    }

    @Override
    public String toString() {
        return "ClePublique(p=" + p + ", g=" + g + ", h=" + h + ")";
    }
}