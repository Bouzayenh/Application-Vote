package dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

public class Vote implements Serializable {

    private int identifiant;
    private String intitule;
    private String option1;
    private String option2;
    private Chiffre somme;
    private int nbBulletins;
    private double resultat;
    private boolean fini;

    public Vote(int identifiant, String intitule, String option1, String option2) {
        this.identifiant = identifiant;
        this.intitule = intitule;
        this.option1 = option1;
        this.option2 = option2;

        somme = new Chiffre(BigInteger.ONE, BigInteger.ONE);
        nbBulletins = 0;
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

    public Chiffre getSomme() {
        return somme;
    }

    public int getNbBulletins() {
        return nbBulletins;
    }

    public double getResultat() {
        return resultat;
    }

    public void setSomme(Chiffre somme) {
        this.somme = somme;
    }

    public void setResultat(double resultat) {
        if (!fini) {
            this.resultat = resultat;
            fini = true;
        }
    }

    public void ajouterBulletin() {
        nbBulletins++;
    }

    public boolean estFini() {
        return fini;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return vote.getIdentifiant() == identifiant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiant);
    }
}
