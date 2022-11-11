import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class JCBDD {

    private String url = "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT";
    private String uname = "bouazzatiy"; //votre login
    private String password = "Azertyuiop";  // votre mdp
    private Socket serveurSocket;
    private ObjectOutputStream outputServeur;
    private ObjectInputStream inputServeur;

    public JCBDD() {
        try {
            serveurSocket = new Socket("localhost", 2999);
            outputServeur = new ObjectOutputStream(serveurSocket.getOutputStream());
            inputServeur = new ObjectInputStream(serveurSocket.getInputStream());

        } catch (IOException ignored) {
        }
    }


    public boolean estConnecte() {
        return serveurSocket != null && !serveurSocket.isClosed();
    }

    public void run() {
        try {
            while (true) {
                //connection au oracle isql
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection connectionBDD = DriverManager.getConnection(url, uname, password);

                    // attend une requête du serveur
                    ArrayList<String> info = (ArrayList<String>) inputServeur.readObject();
                    System.out.println("Demande "+info.get(0));

                    switch (info.get(0)) {
                        case "creerVote":
                            stockerVote(info.get(1), info.get(2), info.get(3), connectionBDD);
                            break;
                        case "autre":
                            break;
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }


        } catch (IOException e) {
            System.out.println("Serveur déconnecté");
        }
    }



    public void stockerVote(String intitule, String option1, String option2,Connection con) throws SQLException {
        Statement s = con.createStatement();
        ResultSet r = s.executeQuery("SELECT COUNT(*) AS recordCount FROM Votes");
        r.next();
        int count = r.getInt("recordCount")+1;
        r.close();
        String rsql= "Insert Into Votes (IDVOTE,INTITULE,OPTION1,OPTION2) VALUES (?,?,?,?)";

        //remplacement des ? dans la requete rsql avec un les parametre du fonction
        PreparedStatement statement = con.prepareStatement(rsql);
        if (count < 10) {
            statement.setString(1,"V0"+count);
        }
        else {
            statement.setString(1,"V"+count);
        }
        statement.setString(2,intitule);
        statement.setString(3,option1);
        statement.setString(4,option2);
        statement.executeUpdate();
    }
}