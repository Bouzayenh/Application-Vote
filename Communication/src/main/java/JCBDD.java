import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql. ResultSet;
import java.sql.ResultSetMetaData;
import java.net.*;
import java.io.*;
import java.util.*;

public class JCBDD {

    public static void main(String[] args) throws SQLException, IOException {
        String url = "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT";
        String uname = "bouazzatiy"; //votre login
        String password = "Azertyuiop";  // votre mdp
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
            String queryaffichage;
            if (msg.equalsIgnoreCase("consulter les votes")){
                String test = "'";
                //String querymsg = "INSERT INTO messages VALUES (null,'" + msg + "')";
                queryaffichage = "select * from votes";
            }else{
                String test = "'";
                String querymsg = "INSERT INTO messages VALUES (null,'" + msg + "')";
                queryaffichage = "select * from messages";
            }

            

            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                Connection con = DriverManager.getConnection(url, uname, password);
                /*Statement statementmsg = con.createStatement();
                int resultmsg = statementmsg.executeUpdate(querymsg);*/

                Statement statement = con.createStatement();
                ResultSet result = statement.executeQuery(queryaffichage);
                ResultSetMetaData resultMD = (ResultSetMetaData) result.getMetaData();
                int columnCount = resultMD.getColumnCount();
                
                for ( int i=1; i <= columnCount ; i++) {
                    System.out.print(resultMD.getColumnName(i) + "\t");
                }
                System.out.print("\n");

                while(result.next()){
                    System.out.print("-");
                    for ( int i=1; i <= columnCount ; i++) {
                        System.out.print(result.getObject(i) + "\t");
                    }
                    System.out.print("\n");
                }

               /*while (result.next()) {
                    String messagesdata = "";
                    for (int i = 1; i <= 2; i++) {
                        messagesdata += result.getString(i) + ":";
                    }
                    System.out.println(messagesdata);
                }*/

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}