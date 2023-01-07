package controller.stockage;

import dataobject.ClePublique;

import java.math.BigInteger;

public interface IStockageScrutateur {
    /**
     * Stocke les informations de clés passées en paramètre.
     * @param p
     * @param g
     * @param h
     * @param x
     * @return L'identifiant du vote créé.
     */
    int insererVote(BigInteger p, BigInteger g, BigInteger h, BigInteger x);

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return La clé publique du vote s'il existe, null sinon.
     */
    ClePublique getClePublique(int idVote);

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return La clé privée du vote s'il existe, null sinon.
     */
    BigInteger getClePrivee(int idVote);
}
