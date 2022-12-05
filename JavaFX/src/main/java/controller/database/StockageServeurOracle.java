package controller.database;

import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;

import java.math.BigInteger;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class StockageServeurOracle implements IStockageServeur{

    private Connection connexion;

    public StockageServeurOracle() throws SQLException {
        connexion = DriverManager.getConnection(
                "jdbc:oracle:thin:@orainfo.iutmontp.univ-montp2.fr:1521:IUT",
                "bouazzatiy",
                "Azertyuiop"
        );
    }

    private Connection getConnexion(){
        return connexion;
    }

    public Set<Vote> getVotes(){

        try {
            ResultSet result = getConnexion().createStatement().executeQuery(
                    "SELECT IDVOTE, INTITULE, OPTION1, OPTION2, RESULTAT FROM SAEVOTES"
            );

            Set<Vote> votes = new HashSet<>();
            while (result.next()) {
                Vote vote = new Vote(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4)
                ).setResultat(result.getInt(5));
                votes.add(vote);
            }
            return votes;
        }catch (SQLException e){
            return new HashSet<>();
        }
    }

    public Vote getVote(int idVote){

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "SELECT INTITULE, OPTION1, OPTION2, URNE_U, URNE_V, NBBULLETINS, RESULTAT FROM SAEVOTES" +
                            " WHERE IDVOTE = ?"
            );
            statement.setInt(1, idVote);
            ResultSet result = statement.executeQuery();
            if (result.next())
                return new Vote(
                        idVote,
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        new Chiffre(
                                new BigInteger(result.getString(4)),
                                new BigInteger(result.getString(5))
                        ),
                        result.getInt(6),
                        result.getDouble(7)
                );
            return null;
        }catch (SQLException e){
            return null;
        }
    }

    public void updateUrne(int idVote, Chiffre urne) {

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "UPDATE SAEVOTES SET URNE_U = ?, URNE_V = ?, NBBULLETINS = NBBULLETINS + 1 WHERE IDVOTE = ?"
            );
            statement.setString(1, urne.getU().toString());
            statement.setString(2, urne.getV().toString());
            statement.setInt(3, idVote);
            statement.executeUpdate();
        }catch (SQLException ignored){

        }
    }

    public void voter(String login, int idVote){

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "INSERT INTO SAEVOTER(LOGIN, IDVOTE) VALUES (?, ?)"
            );
            statement.setString(1, login);
            statement.setInt(2, idVote);
            statement.executeUpdate();
        }catch (SQLException ignored){

        }
    }

    public void creerVote(Vote vote) {

        try {

            PreparedStatement statement = getConnexion().prepareStatement(
                    "INSERT INTO SAEVOTES(IDVOTE, INTITULE, OPTION1, OPTION2, URNE_U, URNE_V, NBBULLETINS, RESULTAT)" +
                            " VALUES (?, ?, ?, ?, ?, ?, 0, -1)"
            );
            statement.setInt(1, vote.getIdentifiant());
            statement.setString(2, vote.getIntitule());
            statement.setString(3, vote.getOption1());
            statement.setString(4, vote.getOption2());
            statement.setString(5, vote.getUrne().getU().toString());
            statement.setString(6, vote.getUrne().getV().toString());
            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    public void terminerVote(int idVote, double resultat) {

        try {

            PreparedStatement statement = getConnexion().prepareStatement(
                    "UPDATE SAEVOTES SET RESULTAT = ? WHERE IDVOTE = ?"
            );
            statement.setDouble(1, resultat);
            statement.setInt(2, idVote);
            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    public boolean aVote(String login, int idVote){

        try {

            PreparedStatement statement = getConnexion().prepareStatement(
                    "SELECT COUNT(*) FROM SAEVOTER WHERE LOGIN = ? AND IDVOTE = ?"
            );

            statement.setString(1, login);
            statement.setInt(2, idVote);

            ResultSet result = statement.executeQuery();
            result.next();

            return result.getInt(1) != 0;

        }catch (SQLException e){
            return true;
        }
    }

    public boolean verifierMotDePasse(Utilisateur utilisateur) {

        try {

            PreparedStatement statement = getConnexion().prepareStatement(
                    "SELECT COUNT(LOGIN) FROM SAEUTILISATEURS WHERE LOGIN = ? AND MOTDEPASSE = ?"
            );

            statement.setString(1, utilisateur.getLogin());
            statement.setString(2, utilisateur.getMotDePasse());

            ResultSet result = statement.executeQuery();
            result.next();

            return result.getInt(1) != 0;

        }catch (SQLException e){
            return false;
        }
    }

    public Set<Utilisateur> getUtilisateurs() {

        try {

            ResultSet result = getConnexion().createStatement().executeQuery(
                    "SELECT LOGIN, MOTDEPASSE, EMAIL FROM SAEUTILISATEURS"
            );

            Set<Utilisateur> utilisateurs = new HashSet<>();
            while (result.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3)
                );
                utilisateurs.add(utilisateur);
            }

            return utilisateurs;

        }catch (SQLException e){
            return new HashSet<>();
        }
    }

    public void creerUtilisateur(Utilisateur utilisateur) {

        try {

            PreparedStatement statement = getConnexion().prepareStatement(
                    "INSERT INTO SAEUTILISATEURS(LOGIN, MOTDEPASSE, EMAIL) VALUES (?, ?, ?)"
            );

            statement.setString(1, utilisateur.getLogin());
            statement.setString(2, utilisateur.getMotDePasse());
            statement.setString(3, utilisateur.getEmail());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    public void supprimerUtilisateur(String login) {

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "DELETE FROM SAEUTILISATEURS WHERE LOGIN = ?"
            );

            statement.setString(1, login);

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    public void mettreAJourUtilisateurMotDePasse(Utilisateur utilisateur) {

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "UPDATE SAEUTILISATEURS SET MOTDEPASSE = ? WHERE LOGIN = ?"
            );

            statement.setString(1, utilisateur.getMotDePasse());
            statement.setString(2, utilisateur.getLogin());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    public void mettreAJourUtilisateurEmail(Utilisateur utilisateur) {

        try {
            PreparedStatement statement = getConnexion().prepareStatement(
                    "UPDATE SAEUTILISATEURS SET EMAIL = ? WHERE LOGIN = ?"
            );

            statement.setString(1, utilisateur.getEmail());
            statement.setString(2, utilisateur.getLogin());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }
}
