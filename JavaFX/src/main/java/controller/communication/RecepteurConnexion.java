package controller.communication;

import dataobject.exception.FeedbackException;
import dataobject.paquet.feedback.FeedbackPaquet;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.Socket;

public class RecepteurConnexion extends Connexion {

    public RecepteurConnexion(Socket socket) throws IOException {
        super(socket);
    }

    public RecepteurConnexion(Connexion connexion) {
        super(connexion);
    }

    public void ecrireException(FeedbackException exception) throws IOException {
        ecrirePaquet(new FeedbackPaquet(exception));
    }

    public void ecrireConfirmation() throws IOException {
        ecrireException(null);
    }
}
