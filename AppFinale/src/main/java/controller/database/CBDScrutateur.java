package controller.database;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CBDScrutateur extends AbstractCBD {

    public CBDScrutateur() throws SQLException {
        super("sanchezj", "Azertyuiop");
    }

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
}
