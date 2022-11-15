package dataobject.paquet;

public class IdentificationPaquet extends Paquet {

    public enum Source {
        CLIENT,
        SCRUTATEUR,
        DATABASE
    }

    private Source source;

    public IdentificationPaquet(Source source) {
        super(Type.IDENTIFICATION);
        this.source = source;
    }

    public Source getSource() {
        return source;
    }
}
