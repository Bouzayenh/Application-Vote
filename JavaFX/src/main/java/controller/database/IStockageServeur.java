package controller.database;

import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;

import java.util.Set;

public interface IStockageServeur {

    /**
     * Stocke les informations du vote passé en paramètre
     * (identifiant, intitulé, option 1, option 2, U, et V).
     * @param vote
     */
    void creerVote(Vote vote);

    /**
     *
     * @return L'ensemble des votes stockés.
     */
    Set<Vote> getVotes();

    /**
     *
     * @param idVote L'identifiant du vote.
     * @return Le vote s'il existe, null sinon.
     */
    Vote getVote(int idVote);

    /**
     * Met à jour le résultat chiffré du vote passé en paramètre.
     * @param idVote L'identifiant du vote.
     * @param urne Le nouveau résultat chiffré.
     */
    void updateUrne(int idVote, Chiffre urne);

    /**
     * Stocke l'information selon laquelle l'utilisateur a participé au vote.
     * @param login L'identifiant de l'utilisateur.
     * @param idVote L'identifiant du vote.
     */
    void voter(String login, int idVote);

    /**
     * Vérifie si l'utilisateur a participé au vote.
     * @param login L'identifiant de l'utilisateur.
     * @param idVote L'identifiant du vote.
     * @return true si l'utilisateur a voté, false sinon.
     */
    boolean aVote(String login, int idVote);

    /**
     * Met à jour le résultat clair du vote,
     * le vote est dorénavant terminé car resultat != -1.
     * @param idVote L'identifiant du vote.
     * @param resultat
     */
    void terminerVote(int idVote, double resultat);

    /**
     * Vérifie si le mot de passe de l'utilisateur est le bon.
     * @param utilisateur L'objet Utilisateur (login, mot de passe haché)
     * dont on souhaite vérifier l'authenticité du mot de passe.
     * @return true si le mot de passe est correcte, false sinon.
     */
    boolean verifierMotDePasse(Utilisateur utilisateur);

    /**
     *
     * @return L'ensemble des utilisateurs stockés.
     */
    Set<Utilisateur> getUtilisateurs();

    /**
     * Stocke les informations de l'utilisateur passé en paramètre
     * (login, mot de passe haché, adresse e-mail)
     * @param utilisateur
     */
    void creerUtilisateur(Utilisateur utilisateur);

    /**
     * Supprime l'utilisateur.
     * @param login L'identifiant de l'utilisateur.
     */
    void supprimerUtilisateur(String login);

    /**
     * Met à jour le mot de passe de l'utilisateur.
     * @param utilisateur
     */
    void mettreAJourUtilisateurMotDePasse(Utilisateur utilisateur);

    /**
     * Met à jour l'adresse e-mail de l'utilisateur.
     * @param utilisateur
     */
    void mettreAJourUtilisateurEmail(Utilisateur utilisateur);
}
