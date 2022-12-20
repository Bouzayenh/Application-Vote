package controller.database;

import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.AucunUtilisateurException;
import dataobject.exception.FeedbackException;

import java.math.BigInteger;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class StockageServeurMySQL implements IStockageServeur{

    private Connection connection;

    public StockageServeurMySQL() throws SQLException {
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/sae",
                "ServeurSAE",
                "jesuisleserveurdelasae"
        );
    }

    @Override
    public void creerVote(Vote vote) {

        try {

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SAE_VOTES(IDVOTE, INTITULE, OPTION1, OPTION2, URNE_U, URNE_V, RESULTAT)" +
                            " VALUES (?, ?, ?, ?, ?, ?, -1)"
            );

            statement.setInt(1, vote.getIdentifiant());
            statement.setString(2, vote.getIntitule());
            statement.setString(3, vote.getOption1());
            statement.setString(4, vote.getOption2());
            statement.setString(5, vote.getUrne().getU().toString());
            statement.setString(6, vote.getUrne().getV().toString());

            statement.executeUpdate();

        }catch (SQLException ignored){
            ignored.printStackTrace();
        }
    }

    @Override
    public Set<Vote> getVotes() {
        try {
            ResultSet result = connection.createStatement().executeQuery(
                    "SELECT IDVOTE, INTITULE, OPTION1, OPTION2, RESULTAT FROM SAE_VOTES ORDER BY IDVOTE"
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

    @Override
    public Vote getVote(int idVote) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT INTITULE, OPTION1, OPTION2, URNE_U, URNE_V, RESULTAT FROM SAE_VOTES" +
                            " WHERE IDVOTE = ?"
            );

            statement.setInt(1, idVote);
            ResultSet result = statement.executeQuery();

            if (result.next()){
                return new Vote(
                        idVote,
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        new Chiffre(
                                new BigInteger(result.getString(4)),
                                new BigInteger(result.getString(5))
                        ),
                        this.getNbVotants(idVote),
                        result.getDouble(6));
            }

            return null;
        }catch (SQLException e){
            return null;
        }
    }

    @Override
    public void updateUrne(int idVote, Chiffre urne) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE SAE_VOTES SET URNE_U = ?, URNE_V = ?, NBBULLETINS = NBBULLETINS + 1 WHERE IDVOTE = ?"
            );

            statement.setString(1, urne.getU().toString());
            statement.setString(2, urne.getV().toString());
            statement.setInt(3, idVote);

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public void voter(String login, int idVote) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SAE_VOTER(LOGIN, IDVOTE) VALUES (?, ?)"
            );

            statement.setString(1, login);
            statement.setInt(2, idVote);
            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public boolean aVote(String login, int idVote) {
        try {

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM SAE_VOTER WHERE LOGIN = ? AND IDVOTE = ?"
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

    @Override
    public void terminerVote(int idVote, double resultat) {
        try {

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE SAE_VOTES SET RESULTAT = ? WHERE IDVOTE = ?"
            );

            statement.setDouble(1, resultat);
            statement.setInt(2, idVote);
            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public boolean verifierMotDePasse(Utilisateur utilisateur) {

        try {

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(LOGIN) FROM SAE_UTILISATEURS WHERE LOGIN = ? AND MOTDEPASSE = ?"
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

    @Override
    public Set<Utilisateur> getUtilisateurs() {
        try {

            ResultSet result = connection.createStatement().executeQuery(
                    "SELECT LOGIN, MOTDEPASSE, EMAIL FROM SAE_UTILISATEURS"
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

    @Override
    public void creerUtilisateur(Utilisateur utilisateur) {
        try {

            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO SAE_UTILISATEURS(LOGIN, MOTDEPASSE, EMAIL) VALUES (?, ?, ?)"
            );

            statement.setString(1, utilisateur.getLogin());
            statement.setString(2, utilisateur.getMotDePasse());
            statement.setString(3, utilisateur.getEmail());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public void supprimerUtilisateur(String login) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM SAE_UTILISATEURS WHERE LOGIN = ?"
            );

            statement.setString(1, login);

            statement.executeUpdate();

        }catch (SQLException ignored){

        }

    }

    @Override
    public void mettreAJourUtilisateurMotDePasse(Utilisateur utilisateur) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE SAE_UTILISATEURS SET MOTDEPASSE = ? WHERE LOGIN = ?"
            );

            statement.setString(1, utilisateur.getMotDePasse());
            statement.setString(2, utilisateur.getLogin());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public void mettreAJourUtilisateurEmail(Utilisateur utilisateur) {

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE SAE_UTILISATEURS SET EMAIL = ? WHERE LOGIN = ?"
            );

            statement.setString(1, utilisateur.getEmail());
            statement.setString(2, utilisateur.getLogin());

            statement.executeUpdate();

        }catch (SQLException ignored){

        }
    }

    @Override
    public String getUtilisateurEmail(String login) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT EMAIL FROM SAE_UTILISATEURS WHERE LOGIN = ?"
            );

            statement.setString(1, login);

            ResultSet result = statement.executeQuery();
            result.next();

            return result.getString(1);

        }catch (SQLException e){
            return "";
        }
    }

    @Override
    public int getNbVotants(int idVote) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM SAE_VOTER WHERE IDVOTE = ?"
            );

            statement.setInt(1, idVote);

            ResultSet result = statement.executeQuery();
            result.next();

            return result.getInt(1);

        }catch (SQLException e){
            return 0;
        }
    }

    public Utilisateur getUtilisateur(String idUtilisateur){
        Set<Utilisateur> utilisateurs = this.getUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getLogin().equals(idUtilisateur))
                return utilisateur;
        }
        return null;
    }
}
