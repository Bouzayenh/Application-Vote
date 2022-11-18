package controller.database;

import dataobject.ClePublique;
import oracle.jdbc.proxy.annotation.Pre;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CBDScrutateur extends AbstractCBD {

    public CBDScrutateur() throws SQLException {
        super("sanchezj", "Azertyuiop");
    }

    /**
     *
     * @param idVote L'identifiant du vote.
     * @param p La composante "p" de la clé publique.
     * @param g La composante "g" de la clé publique.
     * @param h La composante "h" de la clé publique.
     * @param x La clé privée.
     * @throws SQLException
     */
    public void insererCles(int idVote, BigInteger p, BigInteger g, BigInteger h, BigInteger x) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "CALL SAEinsererClesVote(?, ?, ?, ?, ?)"
        );

        statement.setString(1, String.valueOf(idVote));
        statement.setString(2, p.toString());
        statement.setString(3, g.toString());
        statement.setString(4, h.toString());
        statement.setString(5, x.toString());

        statement.executeQuery();
    }

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return La clé publique du vote passé en paramètre.
     * @throws SQLException
     */
    public ClePublique getClePublique(int idVote) throws SQLException {
        BigInteger p, g, h;
        PreparedStatement statement;
        ResultSet result;

        //P
        statement = super.getConnection().prepareStatement(
                "SELECT SAEGETCLEPUBLIQUE_P(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        result = statement.executeQuery();
        result.next();
        p = new BigInteger(result.getString(1));

        //G
        statement = super.getConnection().prepareStatement(
                "SELECT SAEGETCLEPUBLIQUE_G(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        result = statement.executeQuery();
        result.next();
        g = new BigInteger(result.getString(1));

        //H
        statement = super.getConnection().prepareStatement(
                "SELECT SAEGETCLEPUBLIQUE_H(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        result = statement.executeQuery();
        result.next();
        h = new BigInteger(result.getString(1));

        return new ClePublique(p,g,h);
    }

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return La clé privée du vote passé en paramètre.
     * @throws SQLException
     */
    public BigInteger getClePrivee(int idVote) throws SQLException{
        PreparedStatement statement = super.getConnection().prepareStatement(
                "SELECT SAEGETCLEPRIVEE(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        ResultSet result = statement.executeQuery();
        result.next();

        return new BigInteger(result.getString(1));
    }
}
