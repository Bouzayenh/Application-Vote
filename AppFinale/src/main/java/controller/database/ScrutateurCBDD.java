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

    public void insertVote(int idVote, BigInteger p, BigInteger g, BigInteger h, BigInteger x) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "INSERT INTO SAEVOTES(IDVOTE, CLEPUBLIQUE_P, CLEPUBLIQUE_G, CLEPUBLIQUE_H, CLEPRIVEE)" +
                        " VALUES (?, ?, ?, ?, ?)"
        );
        statement.setString(1, String.valueOf(idVote));
        statement.setString(2, p.toString());
        statement.setString(3, g.toString());
        statement.setString(4, h.toString());
        statement.setString(5, x.toString());
        statement.executeUpdate();
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
