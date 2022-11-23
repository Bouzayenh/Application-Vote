package controller.communication;

import dataobject.paquet.Paquet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connexion {
    protected ObjectOutputStream output;
    protected ObjectInputStream input;

    public enum Source {
        CLIENT,
        SCRUTATEUR
    }

    public Connexion(Socket socket) throws IOException {
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        socket.setSoTimeout(1000);
    }

    public void ecrirePaquet(Paquet paquet) throws IOException {
        output.writeObject(paquet);
    }

    public Paquet lirePaquet() throws IOException, ClassNotFoundException {
        return (Paquet) input.readObject();
    }
}
