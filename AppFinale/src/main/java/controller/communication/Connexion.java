package controller.communication;

import dataobject.paquet.Paquet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connexion {
    protected Socket socket;
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    public enum Source {
        CLIENT,
        SCRUTATEUR
    }

    public Connexion(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(this.socket.getOutputStream());
        input = new ObjectInputStream(this.socket.getInputStream());
    }

    public Connexion(Connexion connexion) {
        this.socket = connexion.socket;
        this.output = connexion.output;
        this.input = connexion.input;
    }

    public void ecrirePaquet(Paquet paquet) throws IOException {
        output.writeObject(paquet);
    }

    public Paquet lirePaquet() throws IOException, ClassNotFoundException {
        return (Paquet) input.readObject();
    }

    public int getTimeout() throws IOException {
        return socket.getSoTimeout();
    }

    public void setTimeout(int delai) throws IOException {
        socket.setSoTimeout(delai);
    }
}
