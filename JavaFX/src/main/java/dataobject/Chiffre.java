package dataobject;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Un couple d'entiers représentant un message chiffré selon le crypto-système de ElGamal.
 */
public class Chiffre implements Serializable {
    private BigInteger u;
    private BigInteger v;

    public Chiffre(BigInteger u, BigInteger v) {
        this.u = u;
        this.v = v;
    }

    public BigInteger getU() {
        return u;
    }

    public BigInteger getV() {
        return v;
    }

    @Override
    public String toString() {
        return "Chiffre(u=" + u + ", v=" + v + ")";
    }
}