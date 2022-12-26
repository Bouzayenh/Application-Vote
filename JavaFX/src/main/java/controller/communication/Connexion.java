package controller.communication;

import dataobject.paquet.Paquet;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connexion {
    protected Socket sslSocket;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    public enum Source {
        CLIENT,
        SCRUTATEUR
    }

    public Connexion(Socket sslSocket) throws IOException {
        this.sslSocket = sslSocket;
        output = new ObjectOutputStream(this.sslSocket.getOutputStream());
        input = new ObjectInputStream(this.sslSocket.getInputStream());
    }

    public Connexion(Connexion connexion) {
        this.sslSocket = connexion.sslSocket;
        this.output = connexion.output;
        this.input = connexion.input;
    }

    public void ecrirePaquet(Paquet paquet) throws IOException {
        output.writeObject(paquet);
    }

    public Paquet lirePaquet() throws IOException, ClassNotFoundException {
        return (Paquet) input.readObject();
    }
}
