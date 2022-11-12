package controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import dataobject.Chiffre;
import dataobject.ClePublique;
import dataobject.Utilisateur;
import dataobject.Vote;
import datastatic.Chiffrement;
import org.mindrot.jbcrypt.BCrypt;
import datastatic.Requete;

public class Serveur {
    private Vote vote;
    private ArrayList<Utilisateur> listeConnectes;
    private Socket socketScrutateur;
    private ObjectOutputStream outputScrutateur;
    private ObjectInputStream inputScrutateur;
    private ServerSocket serverSocket;
    private Socket socketJCBDD;
    private ObjectOutputStream outputJCBDD;
    private ObjectInputStream inputJCBDD;

    public Serveur() {
        try {
            listeConnectes = new ArrayList<>();
            // ouvre le serveur
            serverSocket = new ServerSocket(2999);
 
            // attend la connexion du scrutateur
            System.out.println("En attente de la connexion du scrutateur");
            socketScrutateur = serverSocket.accept();
            outputScrutateur = new ObjectOutputStream(socketScrutateur.getOutputStream());
            inputScrutateur = new ObjectInputStream(socketScrutateur.getInputStream());
            System.out.println("Connexion du scrutateur réussi");

            // attend la connexion du Controller.JCBDD
            System.out.println("En attente de la connexion de la base de données");
            socketJCBDD = serverSocket.accept();
            outputJCBDD = new ObjectOutputStream(socketJCBDD.getOutputStream());
            inputJCBDD = new ObjectInputStream(socketJCBDD.getInputStream());
            System.out.println("Connexion de la base de données réussi");



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
            //enregistrer les info du vote avec le nature du requete en premier de l'array liste
            ArrayList<String> info = new ArrayList<String>();
            info.add(0,"creerVote");
            info.add(intitule);info.add(option1);info.add(option2);

            vote = new Vote(intitule, option1, option2);
            outputScrutateur.writeObject(Requete.SERVEUR_CREER_VOTE);

            outputJCBDD.writeObject(info);

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

    public boolean creerUtilisateur(String login, String mdp)  {
        try {

            ArrayList<String> info = new ArrayList<>();
            info.add("creerUtilisateur");
            info.add(login);
            info.add(BCrypt.hashpw(mdp, BCrypt.gensalt()));

            outputJCBDD.writeObject(info);
            boolean rep = (boolean)inputJCBDD.readObject();

            return rep;
        } catch (IOException | ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
    }

    private class ThreadConnexionVersClient implements Runnable {
        Socket socketClient;
        ObjectOutputStream outputClient;
        ObjectInputStream inputClient;
        Utilisateur utilisateur;

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

                    // attend une requête du client
                    Requete requete = (Requete) inputClient.readObject();
                    System.out.println(requete); // debug

                    //Lors d'un demande d'authentification (censé ne pas être connecté)
                    if(requete.equals(Requete.CLIENT_DEMANDER_CONNEXION)) {
                        utilisateur = (Utilisateur) inputClient.readObject();
                        //Si l'ulisateur qui demande à se connecter est déjà connecter
                        if(listeConnectes.contains(utilisateur)){
                            //on refuse l'accès
                            outputClient.writeObject(false);
                        }else{
                            //Sinon on vérifie les infos d'authentification
                            outputClient.writeObject(verifierAuthentification(utilisateur));
                        }
                    }
                    else{//Sinon on vérifie que l'utilisateur est connecté pour effectuer les autres actions
                        if(listeConnectes.contains(utilisateur)){
                            ClePublique clePublique;
                            // traîte la requête
                            switch (requete) {
                                case CLIENT_DEMANDER_CLE_PUBLIQUE:
                                    clePublique = demanderClePublique();
                                    outputClient.writeObject(clePublique);
                                    break;
                                case CLIENT_VOTER:
                                    clePublique = demanderClePublique();
                                    vote.setSomme(Chiffrement.agreger((Chiffre) inputClient.readObject(), vote.getSomme(), clePublique));
                                    vote.ajouterBulletin();
                                    break;
                                case CLIENT_DEMANDER_VOTE_EN_COURS:
                                    outputClient.writeObject(vote);
                                    break;
                                case CLIENT_DEMANDER_DECONNEXION:
                                    deconnexion();
                                    break;
                            }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                verifierConnexionClient();
                verifierConnexionServeur();
            }
        }
        public void deconnexion() {
            try {
                listeConnectes.remove(utilisateur);
                
                outputClient.close();
                inputClient.close();
                socketClient.close();    
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        public boolean verifierAuthentification(Utilisateur u) {
            String login = u.getIdentifiant();
            
                String mdpBDD ="";
                try {
                    ArrayList<String> info = new ArrayList<>();
                    info.add(0,"verifierMdp");
                    info.add(login);
                    outputJCBDD.writeObject(info);
                    mdpBDD = (String) inputJCBDD.readObject();
                    System.out.print("Le mot de passe est:");
                    System.out.println(mdpBDD);

                } catch (IOException | ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    System.out.println("Erreur lors de la récupération du mot de passe dans la base donnée");
                    return false;
                }

                if (BCrypt.checkpw(u.getMotDePasseHache(),mdpBDD)){
                    listeConnectes.add(u);
                    return true;
                }
                try {
                    outputClient.close();
                    inputClient.close();
                    socketClient.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
                deconnexion();
                System.out.println("Controller.Client " + socketClient.getPort() + " déconnecté");
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