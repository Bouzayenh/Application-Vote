package controller.stockage;

import dataobject.ClePublique;

import java.math.BigInteger;
import java.sql.*;

public class StockageScrutateurMySQL implements IStockageScrutateur {

    private Connection connection;


    public StockageScrutateurMySQL() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class form name crash ...");
        }
        String hostname = System.getenv("BDD_SCRUT_HOSTNAME") == null ? "localhost" : System.getenv("BDD_SCRUT_HOSTNAME");
        String bddPort = System.getenv("BDD_PORT") == null ? "3306" : System.getenv("BDD_PORT");
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + hostname + ":" + bddPort + "/sae",
                "ScrutateurSAE",
                "jesuislescrutateur"
        );
    }

    private Connection getConnexion() {
        return connection;
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
                    "SELECT MAX(IDVOTE) FROM SAEVOTES"
            );
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            return -1;
        }
    }

    public ClePublique getClePublique(int idVote) {

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
        } catch (SQLException e) {
            return null;
        }
    }

    public BigInteger getClePrivee(int idVote) {

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
        } catch (SQLException e) {
            return null;
        }


    }


}

