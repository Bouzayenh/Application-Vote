import java.math.BigInteger;

public class Chiffre {
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
