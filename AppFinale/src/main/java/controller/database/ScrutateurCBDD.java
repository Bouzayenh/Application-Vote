package controller.database;

import dataobject.ClePublique;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScrutateurCBDD extends AbstractCBDD {

    public ScrutateurCBDD() throws SQLException {
        super("sanchezj", "Azertyuiop");
    }


    /**
     *
     * @param p
     * @param g
     * @param h
     * @param x
     * @return L'id du vote créé.
     * @throws SQLException
     */
    public int insertVote(BigInteger p, BigInteger g, BigInteger h, BigInteger x) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "INSERT INTO SAEVOTES(IDVOTE, CLEPUBLIQUE_P, CLEPUBLIQUE_G, CLEPUBLIQUE_H, CLEPRIVEE)" +
                        " VALUES (0, ?, ?, ?, ?)"
        );
        statement.setString(1, p.toString());
        statement.setString(2, g.toString());
        statement.setString(3, h.toString());
        statement.setString(4, x.toString());
        statement.executeUpdate();

        ResultSet result = getConnexion().createStatement().executeQuery(
                "SELECT SAEVOTES_AUTOINCREMENT.CURRVAL FROM DUAL"
        );
        result.next();
        return result.getInt(1);
    }

    public ClePublique selectClePublique(int idVote) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "SELECT CLEPUBLIQUE_P, CLEPUBLIQUE_G, CLEPUBLIQUE_H FROM SAEVOTES WHERE IDVOTE = ?"
        );
        statement.setInt(1, idVote);
        ResultSet result = statement.executeQuery();
        if (result.next())
            return new ClePublique(
                    new BigInteger(result.getString(1)),
                    new BigInteger(result.getString(2)),
                    new BigInteger(result.getString(3))
            );
        else
            return null;
    }

    public BigInteger selectClePrivee(int idVote) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "SELECT CLEPRIVEE FROM SAEVOTES WHERE IDVOTE = ?"
        );
        statement.setInt(1, idVote);
        ResultSet result = statement.executeQuery();
        if (result.next())
            return new BigInteger(result.getString(1));
        else
            return null;
    }
}
