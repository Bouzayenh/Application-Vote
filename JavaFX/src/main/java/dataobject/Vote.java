package dataobject;

import java.io.Serializable;

public class Vote implements Serializable {

    private int identifiant;
    private String intitule;
    private String option1;
    private String option2;

    private Chiffre urne;
    private int nbBulletins;
    private double resultat;

    public Vote(int identifiant, String intitule, String option1, String option2, Chiffre urne, int nbBulletins, double resultat) {
        this.identifiant = identifiant;
        this.intitule = intitule;
        this.option1 = option1;
        this.option2 = option2;
        this.urne = urne;
        this.nbBulletins = nbBulletins;
        this.resultat = resultat;
    }

    public Vote(int identifiant, String intitule, String option1, String option2) {
        this(identifiant, intitule, option1, option2, null, 0, -1);
    }

    public int getIdentifiant() {
        return identifiant;
    }

    public String getIntitule() {
        return intitule;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public Chiffre getUrne() {
        return urne;
    }

    public int getNbBulletins() {
        return nbBulletins;
    }

    public boolean estFini() {
        return resultat != -1;
    }

    public double getResultat() {
        return resultat;
    }

    public Vote setResultat(double resultat) {
        this.resultat = resultat;
        return this;
    }

    public Vote setUrne(Chiffre urne) {
        this.urne = urne;
        return this;
    }
}
