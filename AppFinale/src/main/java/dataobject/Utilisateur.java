package dataobject;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;

public class Utilisateur implements Serializable {
    private String login;
    private String motDePasse;
    private String email;

    public Utilisateur(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String login, String motDePasse, String email) {
        this(login, motDePasse);
        this.email = email;
    }

    public Utilisateur hasherMotdePasse() {
        this.motDePasse = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        return this;
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
