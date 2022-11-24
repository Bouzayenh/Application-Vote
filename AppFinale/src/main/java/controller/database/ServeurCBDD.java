package controller.database;

import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServeurCBDD extends AbstractCBDD {

    public ServeurCBDD() throws SQLException {
        super("bouazzatiy", "Azertyuiop");
    }

    public synchronized Set<Vote> selectAllVotes() throws SQLException {
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
    }

    public synchronized Vote selectVote(int idVote) throws SQLException {
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
    }

    public synchronized void updateUrneEtNbBulletins(int idVote, Chiffre urne) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "UPDATE SAEVOTES SET URNE_U = ?, URNE_V = ?, NBBULLETINS = NBBULLETINS + 1 WHERE IDVOTE = ?"
        );
        statement.setString(1, urne.getU().toString());
        statement.setString(2, urne.getV().toString());
        statement.setInt(3, idVote);
        statement.executeUpdate();
    }

    public synchronized void insertVoter(String login, int idVote) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "INSERT INTO SAEVOTER(LOGIN, IDVOTE) VALUES (?, ?)"
        );
        statement.setString(1, login);
        statement.setInt(2, idVote);
        statement.executeUpdate();
    }

    public synchronized void insertVote(Vote vote) throws SQLException {
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
    }

    public void terminerVote(int idVote, double resultat) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "UPDATE SAEVOTES SET RESULTAT = ? WHERE IDVOTE = ?"
        );
        statement.setDouble(1, resultat);
        statement.setInt(2, idVote);
        statement.executeUpdate();
    }

    public synchronized boolean voteEstUnique(String login, int idVote) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "SELECT COUNT(*) FROM SAEVOTER WHERE LOGIN = ? AND IDVOTE = ?"
        );
        statement.setString(1, login);
        statement.setInt(2, idVote);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1) == 0;
    }

    public synchronized boolean authentifier(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "SELECT COUNT(LOGIN) FROM SAEUTILISATEURS WHERE LOGIN = ? AND MOTDEPASSE = ?"
        );
        statement.setString(1, utilisateur.getLogin());
        statement.setString(2, utilisateur.getMotDePasse());
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1) != 0;
    }

    public Set<Utilisateur> selectUtilisateurs() throws SQLException {
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
    }

    public void insertUtilisateur(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "INSERT INTO SAEUTILISATEURS(LOGIN, MOTDEPASSE, EMAIL) VALUES (?, ?, ?)"
        );
        statement.setString(1, utilisateur.getLogin());
        statement.setString(2, utilisateur.getMotDePasse());
        statement.setString(3, utilisateur.getEmail());
        statement.executeUpdate();
    }

    public void deleteUtilisateur(String login) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "DELETE FROM SAEUTILISATEURS WHERE LOGIN = ?"
        );
        statement.setString(1, login);
        statement.executeUpdate();
    }

    public void updateUtilisateurMotDePasse(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                 "UPDATE SAEUTILISATEURS SET MOTDEPASSE = ? WHERE LOGIN = ?"
        );
        statement.setString(1, utilisateur.getMotDePasse());
        statement.setString(2, utilisateur.getLogin());
        statement.executeUpdate();
    }

    public void updateUtilisateurEmail(Utilisateur utilisateur) throws SQLException {
        PreparedStatement statement = getConnexion().prepareStatement(
                "UPDATE SAEUTILISATEURS SET EMAIL = ? WHERE LOGIN = ?"
        );
        statement.setString(1, utilisateur.getEmail());
        statement.setString(2, utilisateur.getLogin());
        statement.executeUpdate();
    }
}
