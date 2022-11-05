import java.io.Serializable;

import oracle.jdbc.oracore.Util;

public class Utilisateur implements Serializable {
    
    private String identifiant;
    private String MotDePasse;

    public Utilisateur(String id, String mdp) {
        identifiant = id;
        MotDePasse = mdp;
    }


    public String getIdentifiant() {
        return identifiant;
    }

    public String getMotDePasse() {
        return MotDePasse;
    }
    
}
