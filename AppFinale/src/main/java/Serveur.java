import java.io.*;
import java.net.*;
import java.util.HashMap;

import org.mindrot.jbcrypt.BCrypt;

import java.math.BigInteger;

public class Serveur {
    private Vote vote;
    private HashMap<String, String> listeUtilisateurs;
    private Socket socketScrutateur;
    private ObjectOutputStream outputScrutateur;
    private ObjectInputStream inputScrutateur;
    private ServerSocket serverSocket;


    public Serveur() {
        try {
            //Ajout d'un utilisateur + mdp
            listeUtilisateurs = new HashMap<>();
            listeUtilisateurs.put("b", BCrypt.hashpw("a", BCrypt.gensalt()));

            //Si vous voulez vérifiez le hashage
            //System.out.println("Mot de passe hashé: " + listeUtilisateurs.get("b"));

            // ouvre le serveur
            serverSocket = new ServerSocket(2999);

            // attend la connexion du scrutateur
            socketScrutateur = serverSocket.accept();
            outputScrutateur = new ObjectOutputStream(socketScrutateur.getOutputStream());
            inputScrutateur = new ObjectInputStream(socketScrutateur.getInputStream());

        } catch (IOException ignored) {}
    }

    public void run() {
        new Thread(() -> {
            try {
                while (true) {
                    // attend la connexion du client
                    Socket socketClient = serverSocket.accept();

                    //traite la connexion client dans un autre thread
                    new Thread(new ThreadConnexionVersClient(socketClient)).start();
                }
            } catch (IOException ignored) {}
        }).start();
    }

    public ClePublique demanderClePublique() throws IOException, ClassNotFoundException {
        outputScrutateur.writeObject(Requete.SERVEUR_DEMANDER_CLE_PUBLIQUE);
        return (ClePublique) inputScrutateur.readObject();
    }

    public void creerVote(String intitule, String option1, String option2) {
        try {
            vote = new Vote(intitule, option1, option2);
            outputScrutateur.writeObject(Requete.SERVEUR_CREER_VOTE);

        } catch (IOException ignored) {}
    }

    public void arreterVote() {
        try {
            outputScrutateur.writeObject(Requete.SERVEUR_DEMANDER_DECHIFFREMENT);
            outputScrutateur.writeObject(vote.getSomme());
            vote.setResultat((double) (int) inputScrutateur.readObject() / vote.getNbBulletins());

        } catch (IOException | ClassNotFoundException ignored) {}
    }

    public String consulterResultats() {
        double res = (double) ((int) (vote.getResultat() * 10000)) / 100;
        if (vote.getNbBulletins() == 0) return vote.getIntitule() + "Personne n'a voté";
        else if (res > 50) return vote.getIntitule() + " " + vote.getOption1() + " gagne avec " + res + " % des voix";
        else if (res < 50) return vote.getIntitule() + " " + vote.getOption2() + " gagne avec " + (100 - res) + " % des voix";
        else return vote.getIntitule() + " Egalité entre " + vote.getOption1() + vote.getOption2();
    }
    

    public synchronized void agreger(Chiffre c) {
        try {
            ClePublique clePublique = demanderClePublique();
            BigInteger p = clePublique.getP();

            // def Chiffré agrégé
            vote.setSomme(new Chiffre(vote.getSomme().getU().multiply(c.getU()).mod(p), vote.getSomme().getV().multiply(c.getV()).mod(p)));

        } catch (IOException | ClassNotFoundException e) {
            // e.printStackTrace();
        }
    }

    private class ThreadConnexionVersClient implements Runnable {
        Socket socketClient;
        ObjectOutputStream outputClient;
        ObjectInputStream inputClient;

        public ThreadConnexionVersClient(Socket socket) throws IOException {
            this.socketClient = socket;
            this.inputClient = new ObjectInputStream(socket.getInputStream());
            this.outputClient = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Client " + socketClient.getPort() + " connecté");
        }

        @Override
        public void run() {
            try {
                while (true) {

                   if(true) {
                        // attend une requête du client
                        Requete requete = (Requete) inputClient.readObject();
                        System.out.println(requete); // debug

                        // traîte la requête
                        switch (requete) {
                            case CLIENT_DEMANDER_CLE_PUBLIQUE:
                                ClePublique clePublique = demanderClePublique();
                                outputClient.writeObject(clePublique);
                                break;
                            case CLIENT_VOTER:
                                agreger((Chiffre) inputClient.readObject());
                                vote.ajouterBulletin();
                                break;
                            case CLIENT_DEMANDER_VOTE_EN_COURS:
                                outputClient.writeObject(vote);
                                break;
                            case CLIENT_DEMANDER_CONNEXION:
                                outputClient.writeObject(verifierAuthentification((Utilisateur) inputClient.readObject()));
                                break;
                    }
                   }
                    
                }
            } catch (IOException | ClassNotFoundException e) {
                verifierConnexionClient();
                verifierConnexionServeur();
            }
        }

        public boolean verifierAuthentification(Utilisateur u) {
            if(listeUtilisateurs.containsKey(u.getIdentifiant())){
                if (BCrypt.checkpw(u.getMotDePasse(),listeUtilisateurs.get(u.getIdentifiant()))){
                    return true;
                }
                try {
                    inputClient.close();
                    socketClient.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
            }
            return false;
        }

        public synchronized void verifierConnexionClient() {
            try {
                socketClient.setSoTimeout(1);
                inputClient.readObject();
            } catch (SocketTimeoutException e2) {
                try {
                    socketClient.setSoTimeout(0);
                } catch (SocketException ignored) {}
            } catch (IOException | ClassNotFoundException e2) {
                System.out.println("Client " + socketClient.getPort() + " déconnecté");
            }
        }

        public synchronized void verifierConnexionServeur() {
            try {
                socketScrutateur.setSoTimeout(1);
                inputScrutateur.readObject();
            } catch (SocketTimeoutException e2) {
                try {
                    socketScrutateur.setSoTimeout(0);
                } catch (SocketException ignored) {}
            } catch (IOException | ClassNotFoundException e2) {
                System.out.println("Erreur: le scrutateur déconnecté");
            }
        }
    }
}