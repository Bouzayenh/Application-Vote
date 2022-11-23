package controller.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractCBDD {
    private Connection connexion;

    public AbstractCBDD(String login, String motDePasse) throws SQLException {
        connexion = DriverManager.getConnection(
                "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT",
                login,
                motDePasse
        );
    }

    protected Connection getConnexion() {
        return connexion;
    }
}
