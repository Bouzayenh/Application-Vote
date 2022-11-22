package controller.database;

import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

public class CBDServeur extends AbstractCBD{

    public CBDServeur() throws SQLException {
        super("bouazzatiy", "Azertyuiop");
    }

    /**
     *
     * @param vote Le vote à insérer dans la BD (intitulé, option1, option2).
     * @return L'id du vote créé.
     */
    public int creerVote(Vote vote) throws SQLException {

        PreparedStatement statement = super.getConnection().prepareStatement(
                "CALL SAECREERVOTE(?, ?, ?)"
        );

        statement.setString(1, vote.getIntitule());
        statement.setString(2, vote.getOption1());
        statement.setString(3, vote.getOption2());

        statement.executeQuery();

        //choppe le dernier vote créer, c'est sale, mais j'ai pas d'autre solution pour le moment
        ResultSet result = super.getConnection().createStatement().executeQuery(
                "SELECT MAX(IDVOTE)" +
                        "FROM SAEVOTES"
        );
        result.next();

        return result.getInt(1);
    }

    /**
     * @param utilisateur L'utilisateur à insérer dans la BD (login, mdp)
     */
    public boolean creerUtilisateur(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "INSERT INTO SAEUTILISATEURS (LOGIN,MOTDEPASSE) VALUES(?,?)"
        );

        statement.setString(1, utilisateur.getIdentifiant());
        statement.setString(2, utilisateur.getMotDePasseHache());

        statement.executeQuery();
        return true;
    }

    public boolean supprimerUtilisateur(String identifiant) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "DELETE FROM SAEUTILISATEURS WHERE LOGIN=?"
        );

        statement.setString(1, identifiant);

        statement.executeQuery();
        return true;
    }

    public boolean modifierUtilisateur(String identifiant, String newIdentifiant, String newMDP, String newEmail) throws SQLException {
        
        if(!newEmail.equals("null")){
            PreparedStatement statement = super.getConnection().prepareStatement(
                "UPDATE SAEUTILISATEURS SET MAIL = ? WHERE LOGIN = ?"
            );

            statement.setString(1, newEmail);
            statement.setString(2, identifiant);

            statement.executeUpdate();
        }

        if(!newMDP.equals("null")){
            PreparedStatement statement = super.getConnection().prepareStatement(
                "UPDATE SAEUTILISATEURS SET MOTDEPASSE = ? WHERE LOGIN = ?"
            );

            statement.setString(1, BCrypt.hashpw(newMDP, BCrypt.gensalt()));
            statement.setString(2, identifiant);

            statement.executeUpdate();
        }

        if(!newIdentifiant.equals("null")){
            PreparedStatement statement = super.getConnection().prepareStatement(
                "UPDATE SAEUTILISATEURS SET LOGIN = ? WHERE LOGIN = ?"
            );

            statement.setString(1, newIdentifiant);
            statement.setString(2, identifiant);

            statement.executeUpdate();
        }
        return true;
    }
    
    

    /**
     * @param utilisateur L'utilisateur dont on souhaite le mot de passe.
     * @return Le mot de passe haché de l'utilisateur.
     */
    public String getMotDePasseUtilisateur(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "SELECT SAEGETMOTDEPASSEUTILISATEUR(?)" +
                        "FROM DUAL"
        );

        statement.setString(1, utilisateur.getIdentifiant());

        ResultSet result = statement.executeQuery();
        result.next();

        return result.getString(1);
    }

    /**
     *
     * @param idVote : L'identifiant du vote.
     * @return Le nombre de personnes ayant voté.
     * @throws SQLException
     */
    public int getNbBulletinsVote(int idVote) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "SELECT SAEGETNBBULLETINSVOTE(?)" +
                        "FROM DUAL"
        );

        statement.setInt(1, idVote);

        ResultSet result = statement.executeQuery();
        result.next();

        return result.getInt(1);
    }

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return Le résultat chiffré du vote passé en paramêtre.
     * @throws SQLException
     */
    public Chiffre getResultatsChiffresVote(int idVote) throws SQLException {
        BigInteger u, v;

        //On choppe u
        PreparedStatement statement = super.getConnection().prepareStatement(
                "SELECT SAEGETUVVOTE(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        ResultSet result = statement.executeQuery();
        result.next();

        u = new BigInteger(result.getString(1));

        //Similairement, on choppe v
        statement = super.getConnection().prepareStatement(
                "SELECT SAEGETVVVOTE(?)" +
                        "FROM DUAL"
        );
        statement.setInt(1, idVote);
        result = statement.executeQuery();
        result.next();

        v = new BigInteger(result.getString(1));

        return new Chiffre(u, v);
    }

    /**
     *
     * @param idVote L'identifiant du vote.
     * @param chiffre Le chiffré correspondant au nouveau résultat du vote.
     * @throws SQLException
     */
    public void mettreAJourResultatChiffre(int idVote, Chiffre chiffre) throws SQLException {

        PreparedStatement statement = super.getConnection().prepareStatement(
                "CALL SAEMAJRESULTATCHIFFRE(?, ?, ?)"
        );

        statement.setInt(1, idVote);
        statement.setString(2, chiffre.getU().toString());
        statement.setString(3, chiffre.getV().toString());

        statement.executeQuery();
    }


    /**
     *
     * @param idVote L'identifiant du vote.
     * @param resultat Résultat claire du vote.
     * @throws SQLException
     */
    // a faire en propre avec une procedure
    public void mettreAJourResultatclaire(int idVote,int resultat) throws SQLException {
        PreparedStatement statement = super.getConnection().prepareStatement(
                "Insert into SAEVOTES (resultatclair) values (?) where idvote=?"
        );
        statement.setInt(1,resultat);
        statement.setInt(1,idVote);
    }

    public Map<Integer, Vote> consulterVotes() throws SQLException {
        Map<Integer, Vote> listeVotes = new HashMap<Integer, Vote>();

        Statement statement = super.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(
            "SELECT idvote, intitule, option1, option2 FROM SAEVOTES"
        );
        
        int i = 1;
        while(rs.next()){
            
            Vote v = new Vote.VoteBuilder()
            .identifiant(rs.getInt(1))
            .informations(  rs.getString(2),
                            rs.getString(3), 
                            rs.getString(4))
            .build();
            listeVotes.put(i, v);
            i++;
        }
        return listeVotes;
    }

    public Vote consulterResultat(int idVote) throws SQLException {

        PreparedStatement statement = super.getConnection().prepareStatement(
            "SELECT intitule, option1, option2  FROM SAEVOTES WHERE IDVOTE = ?"
        );
        statement.setInt(1, idVote);
        ResultSet rs = statement.executeQuery();
        
        rs.next();

        Vote v = new Vote.VoteBuilder()
        .identifiant(idVote)
        .informations(  rs.getString(1),
                        rs.getString(2), 
                        rs.getString(3))
        .nbBulletins(this.getNbBulletinsVote(idVote))
        .build();

        return v;

    }

}
