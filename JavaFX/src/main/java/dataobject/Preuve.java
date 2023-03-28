package dataobject;

import java.math.BigInteger;

public class Preuve {

    private BigInteger chall0;
    private BigInteger rep0;
    private BigInteger chall1;
    private BigInteger rep1;
    private BigInteger a0;
    private BigInteger b0;
    private BigInteger a1;
    private BigInteger b1;

    public Preuve(BigInteger chall0, BigInteger rep0, BigInteger chall1, BigInteger rep1, BigInteger a0, BigInteger b0, BigInteger a1, BigInteger b1) {
        this.chall0 = chall0;
        this.rep0 = rep0;
        this.chall1 = chall1;
        this.rep1 = rep1;
        this.a0 = a0;
        this.b0 = b0;
        this.a1 = a1;
        this.b1 = b1;
    }

    public BigInteger getChall0() {
        return chall0;
    }

    public BigInteger getRep0() {
        return rep0;
    }

    public BigInteger getChall1() {
        return chall1;
    }

    public BigInteger getRep1() {
        return rep1;
    }

    public BigInteger getA0() {
        return a0;
    }

    public BigInteger getB0() {
        return b0;
    }

    public BigInteger getA1() {
        return a1;
    }

    public BigInteger getB1() {
        return b1;
    }
}
