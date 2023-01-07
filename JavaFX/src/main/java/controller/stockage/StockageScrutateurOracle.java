package controller.stockage;

import dataobject.ClePublique;

import java.math.BigInteger;
import java.sql.*;

public class StockageScrutateurOracle implements IStockageScrutateur{

    private Connection connexion;

    public StockageScrutateurOracle() throws SQLException {
        connexion = DriverManager.getConnection(
                "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT",
                "sanchezj",
                "Azertyuiop"
        );
    }

    private Connection getConnexion() {
        return connexion;
    }

    public int insererVote(BigInteger p, BigInteger g, BigInteger h, BigInteger x) {

        try {
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
        }catch (SQLException e){
            return -1;
        }
    }

    public ClePublique getClePublique(int idVote){

        try {
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
        }catch (SQLException e){
            return null;
        }
    }

    public BigInteger getClePrivee(int idVote){

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "SELECT CLEPRIVEE FROM SAEVOTES WHERE IDVOTE = ?"
            );
            statement.setInt(1, idVote);
            ResultSet result = statement.executeQuery();
            if (result.next())
                return new BigInteger(result.getString(1));
            else
                return null;
        }catch (SQLException e) {
            return null;
        }


    }
}
