import java.io.Serializable;
import java.math.BigInteger;

public class Vote implements Serializable {
    private String intitule;
    private String option1;
    private String option2;
    private Chiffre somme;
    private int nbBulletins;
    private double resultat;
    private boolean fini;

    public Vote(String intitule, String option1, String option2) {
        this.intitule = intitule;
        this.option1 = option1;
        this.option2 = option2;

        somme = new Chiffre(BigInteger.ONE, BigInteger.ONE);
        nbBulletins = 0;
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
}
