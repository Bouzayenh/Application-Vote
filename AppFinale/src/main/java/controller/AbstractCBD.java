package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractCBD {

    private Connection connection;

    public AbstractCBD(String login, String motDePasse) throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT",
                login,
                motDePasse
        );
    }

    protected Connection getConnection() {
        return connection;
    }
}
