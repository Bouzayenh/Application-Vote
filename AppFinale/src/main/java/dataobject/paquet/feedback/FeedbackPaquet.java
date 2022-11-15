package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;
import dataobject.paquet.Paquet;

public class FeedbackPaquet extends Paquet {

    private FeedbackException exception;

    public FeedbackPaquet() {
        super(Type.FEEDBACK);
    }

    public FeedbackPaquet(FeedbackException exception) {
        this();
        this.exception = exception;
    }

    public boolean estPositif() {
        return exception == null;
    }

    public void throwException() throws FeedbackException {
        if (!estPositif())
            throw exception;
    }
}
