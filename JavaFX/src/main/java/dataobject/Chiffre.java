package dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // Deux chiffrés sont égaux ssi u et v le sont
        Chiffre chiffre = (Chiffre) o;
        return Objects.equals(u, chiffre.u) && Objects.equals(v, chiffre.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u, v);
    }
}