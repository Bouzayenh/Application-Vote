package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;
import dataobject.paquet.Paquet;

public class FeedbackPaquet extends Paquet {
    private FeedbackException exception;

    public FeedbackPaquet(FeedbackException exception) {
        super(Type.FEEDBACK);
        this.exception = exception;
    }

    public FeedbackException getException() {
        return exception;
    }
}
