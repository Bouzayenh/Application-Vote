package dataobject;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

/**
 * Représente un utilisateur.
 */
public class Utilisateur implements Serializable {
    private String login;

    /**
     * Le mot de passe, en clair.
     */
    private String motDePasse;

    /**
     * L'adresse mail correspondant à l'utilisateur, utilisée pour le notifier :
     * <ul>
     *     <li>à la création de son compte</li>
     *     <li>lorsqu'il vote</li>
     *     <li>lorsqu'un vote se termine</li>
     * </ul>
     */
    private String email;

    public Utilisateur(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String login, String motDePasse, String email) {
        this(login, motDePasse);
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public String getEmail() {
        return email;
    }
}
