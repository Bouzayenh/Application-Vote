package dataobject;

import org.mindrot.jbcrypt.BCrypt;

import java.io.Serializable;
import java.util.Objects;

public class Utilisateur implements Serializable {
    
    private String identifiant;
    private String motDePasseHache;

    public Utilisateur(String identifiant, String motDePasse) {
        this.identifiant = identifiant;
        this.motDePasseHache = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotDePasseHache() {
        return motDePasseHache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return identifiant.equals(that.identifiant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiant);
    }
}
