public class Vote {
    private String intitule;
    private String option1;
    private String option2;

    public Vote(String intitule, String option1, String option2) {
        this.intitule = intitule;
        this.option1 = option1;
        this.option2 = option2;
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
}
