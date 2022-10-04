import java.math.BigInteger;

public class ClePrivee extends Cle {
    private BigInteger x;
    private BigInteger p;

    public ClePrivee(BigInteger x, BigInteger p) {
        this.x = x;
        this.p = p;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getP() {
        return p;
    }

    @Override
    public String toString() {
        return "ClePrivee{" +
                "x=" + x +
                ", p=" + p +
                '}';
    }
}
