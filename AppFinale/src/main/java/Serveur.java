import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class Serveur {
    private Vote vote;

    private ObjectOutputStream outputScrutateur;
    private ObjectInputStream inputScrutateur;

    private ObjectOutputStream outputClient;
    private ObjectInputStream inputClient;

    public Serveur() {
        try {
            // ouvre le serveur
            ServerSocket serverSocket = new ServerSocket(2999);

            // attend la connexion du scrutateur
            Socket scrutateur = serverSocket.accept();
            outputScrutateur = new ObjectOutputStream(scrutateur.getOutputStream());
            inputScrutateur = new ObjectInputStream(scrutateur.getInputStream());

            // attend la connexion du client
            Socket client = serverSocket.accept();
            outputClient = new ObjectOutputStream(client.getOutputStream());
            inputClient = new ObjectInputStream(client.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * utilisé pour les tests sur la création de nouveaux votes
     */
    public Vote getVote() {
        return vote;
    }

    public void run() {
        try {
            // pas de boucle pour l'instant

            // attend une requête du client
            Requete requete = (Requete) inputClient.readObject();

            // traîte la requête
            switch (requete) {
                case CLIENT_DEMANDER_CLE_PUBLIQUE:
                    ClePublique clePublique = demanderClePublique();
                    outputClient.writeObject(clePublique);
                    break;
                // plus de types de requêtes à l'avenir
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ClePublique demanderClePublique() throws IOException, ClassNotFoundException {
        outputScrutateur.writeObject(Requete.SERVEUR_DEMANDER_CLE_PUBLIQUE);
        return (ClePublique) inputScrutateur.readObject();
    }

    public void creerVote(String intitule, String option1, String option2) {
        try {
            vote = new Vote(intitule, option1, option2);
            outputScrutateur.writeObject(Requete.SERVEUR_CREER_VOTE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Chiffre agreger(Chiffre c1, Chiffre c2) {
        try {
            ClePublique clePublique = demanderClePublique();
            BigInteger p = clePublique.getP();

            // def Chiffré agrégé
            return new Chiffre(c1.getU().multiply(c2.getU()).mod(p), c1.getV().multiply(c2.getV()).mod(p));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}