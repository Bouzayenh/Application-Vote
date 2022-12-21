package dataobject.paquet.feedback;

import dataobject.exception.FeedbackException;
import dataobject.paquet.Paquet;

/**
 * Paquet envoyé en retour d'une demande.<br>
 * Il contient une {@link FeedbackException} si un problème a eu lieu lors du traitement de la demande. Elle devra être traitée par le récepteur.<br>
 */
public class FeedbackPaquet extends Paquet {

    /**
     * Si un problème a eu lieu lors du traitement de la demande, décrit celui-ci.<br>
     * null si tout a eu lieu sans accroc.
     */
    private FeedbackException exception;

    public FeedbackPaquet(FeedbackException exception) {
        super(Type.FEEDBACK);
        this.exception = exception;
    }

    public FeedbackException getException() {
        return exception;
    }
}
