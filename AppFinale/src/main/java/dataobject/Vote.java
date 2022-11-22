package dataobject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

public class Vote implements Serializable {

    private int identifiant;
    private String intitule;
    private String option1;
    private String option2;
    private Date dateDeFin;
    private int urne;
    private int nbBulletins;
    private boolean fini;

    private Vote(VoteBuilder voteBuilder) {
        this.identifiant = voteBuilder.identifiant;
        this.intitule = voteBuilder.intitule;
        this.option1 = voteBuilder.option1;
        this.option2 = voteBuilder.option2;
        this.dateDeFin = voteBuilder.dateDeFin;
        this.urne = voteBuilder.urne;
        this.nbBulletins = voteBuilder.nbBulletins;
        this.fini = voteBuilder.fini;
    }

    public void setUrne(int urne) {
        this.urne = urne;
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

    public int getUrne() {
        return urne;
    }

    public int getNbBulletins() {
        return nbBulletins;
    }

    public boolean estFini() {
        return fini;
    }

    public static class VoteBuilder {

        private int identifiant;
        private String intitule;
        private String option1;
        private String option2;
        private Date dateDeFin;
        private int urne;
        private int nbBulletins;
        private boolean fini;

        public VoteBuilder() {}

        public VoteBuilder identifiant(int identifiant) {
            this.identifiant = identifiant;
            return this;
        }

        public VoteBuilder informations(String intitule, String option1, String option2) {
            this.intitule = intitule;
            this.option1 = option1;
            this.option2 = option2;
            return this;
        }

        public VoteBuilder dateDeFin(Date dateDeFin){
            this.dateDeFin = dateDeFin;
            return this;
        }

        public VoteBuilder urne(int urne) {
            this.urne = urne;
            return this;
        }

        public VoteBuilder nbBulletins(int nbBulletins) {
            this.nbBulletins = nbBulletins;
            return this;
        }

        public VoteBuilder estFini(boolean fini) {
            this.fini = fini;
            return this;
        }

        public Vote build() {
            return new Vote(this);
        }
    }
}
