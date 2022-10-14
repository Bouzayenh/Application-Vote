import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql. ResultSet;
import java.net.*;
import java.io.*;
import java.util.*;

public class JCBDD {

    public static void main(String[] args) throws SQLException, IOException {
        String url = "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT";
        String uname = "bouzayenh";
        String password = "H1q2w5boz3e4rBU";
        String msg;


        while (true) {
            Socket socBDD = new Socket("localhost", 1234);

            Scanner sc = new Scanner(System.in);

            System.out.println("Connecté");

            Scanner scmsg = new Scanner(socBDD.getInputStream());

            msg = scmsg.nextLine();

            System.out.println(msg);

            if (msg.equalsIgnoreCase("Deconnecte")){
                break;
            }

            String test = "'";
            String querymsg = "INSERT INTO messages VALUES (null,'" + msg + "')";
            String queryaffichage = "select * from messages";

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Connection con = DriverManager.getConnection(url, uname, password);
                Statement statementmsg = con.createStatement();
                int resultmsg = statementmsg.executeUpdate(querymsg);

                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(queryaffichage);


                while (result.next()) {
                    String messagesdata = "";
                    for (int i = 1; i <= 2; i++) {
                        messagesdata += result.getString(i) + ":";
                    }
                    System.out.println(messagesdata);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}