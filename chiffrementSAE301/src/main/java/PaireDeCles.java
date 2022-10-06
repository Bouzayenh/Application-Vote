public class PaireDeCles {
    private ClePrivee clePrivee;
    private ClePublique clePublique;

    public PaireDeCles(ClePrivee clePrivee, ClePublique clePublique) {
        this.clePrivee = clePrivee;
        this.clePublique = clePublique;
    }

    public ClePrivee getClePrivee() {
        return clePrivee;
    }

    public ClePublique getClePublique() {
        return clePublique;
    }

    @Override
    public String toString() {
        return "PaireDeCles{" +
                "clePrivee=" + clePrivee +
                ", clePublique=" + clePublique +
                '}';
    }
}
