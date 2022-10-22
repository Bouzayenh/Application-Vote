import java.math.BigInteger;

public class Serveur {
    private Scrutateur scrutateur;
    private Vote vote;

    public Serveur() {
    }

    /*
     * utilisé pour les tests
     */
    public Scrutateur getScrutateur() {
        return scrutateur;
    }

    public Vote getVote() {
        return vote;
    }

    public Chiffre agreger(Chiffre c1, Chiffre c2) {
        ClePublique clePublique = scrutateur.getClePublique();
        BigInteger p = clePublique.getP();

        // def Chiffré agrégé
        return new Chiffre(c1.getU().multiply(c2.getU()).mod(p), c1.getV().multiply(c2.getV()).mod(p));
    }

    public void creerVote(int l, String intitule, String option1, String option2) {
        vote = new Vote(intitule, option1, option2);
        scrutateur = new Scrutateur(l);
    }

    public void consulterVoteEnCours() {
        
    }
}