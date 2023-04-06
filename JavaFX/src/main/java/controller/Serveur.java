package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import controller.communication.RecepteurConnexion;
import controller.config.Conf;
import controller.stockage.IStockageServeur;
import controller.stockage.StockageServeurMySQL;
import controller.stockage.StockageServeurOracle;
import controller.thread.ThreadArretVotes;
import dataobject.*;
import dataobject.exception.*;
import dataobject.paquet.*;
import dataobject.paquet.feedback.*;
import org.mindrot.jbcrypt.BCrypt;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Serveur {

    private static Serveur instance;

    private IStockageServeur stockageServeur;
    private Set<String> utilisateursAuthentifies;
    private ServerSocket serverSocket;
    private EmetteurConnexion scrutateur;

    /**
     * Permet de limiter l'accès, le serveur sera toujours en marche et pourra traiter les demandes des clients, mais l'administrateur ne pourra pas accéder à ses fonctions.
     */
    // private boolean verouille = false;
    private boolean verouille = System.getenv("VEROUILLE") != null
            && Boolean.parseBoolean(System.getenv("VEROUILLE"));


    private Serveur() throws IOException, SQLException {

        utilisateursAuthentifies = new HashSet<>();

        //SGBD
        switch (Conf.BASE_DE_DONNEES){
            case ORACLE -> stockageServeur = new StockageServeurOracle();
            case MYSQL -> stockageServeur = new StockageServeurMySQL();
        }

        //SSL ou non-SSL
        if (Conf.UTILISE_SSL){
            /*
            try {
                InputStream keyStoreStream = getClass().getResourceAsStream("/ssl/saeKeyStore.jks");
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(keyStoreStream, "capybara".toCharArray());

                KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStore, "capybara".toCharArray());

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

                SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
                serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(Conf.PORT);
            }  catch (UnrecoverableKeyException | CertificateException | KeyStoreException | NoSuchAlgorithmException |
                      KeyManagementException e) {
                System.out.println("Certificat introuvable ... ");
                System.exit(1);
            }

             */
            System.setProperty("javax.net.ssl.keyStore", "JavaFX/src/main/resources/ssl/saeKeyStore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "capybara");
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            serverSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(Conf.PORT);
        }
        else {
            serverSocket = new ServerSocket(Conf.PORT);
        }
    }

    public static Serveur getInstance() throws SQLException, IOException {
        if (instance == null) instance = new Serveur();
        return instance;
    }

    public boolean estVerouille(){
        return verouille;
    }

    public void verouiller(){
        verouille = true;
    }

    public void deverouiller(String motDePasseServeur) throws MauvaisMotDePasseServeurException {
        if (BCrypt.checkpw(motDePasseServeur, getMotDePasseServeur())){
            verouille = false;
        }
        else {
            throw new MauvaisMotDePasseServeurException();
        }
    }

    public void changerMotDePasseServeur(String ancienMotDePasseServeur, String nouveauMotDePasseServeur) throws MauvaisMotDePasseServeurException {

        if(!verouille){

            if (BCrypt.checkpw(ancienMotDePasseServeur, getMotDePasseServeur())){
                setMotDePasseServeur(nouveauMotDePasseServeur);
            }
            else{
                throw new MauvaisMotDePasseServeurException();
            }

        }
    }

    private void setMotDePasseServeur(String motDePasseServeur){

        File pwd = new File("JavaFX/src/main/resources/serveur/pwd");

        try{
            //si le fichier n'existe pas, le créé (ce cas de figure ne devrait pas arriver lors d'une utilisation normale)
            pwd.createNewFile();

            FileWriter fileWriter = new FileWriter(pwd);
            fileWriter.write(BCrypt.hashpw(motDePasseServeur, BCrypt.gensalt()));
            fileWriter.close();

        } catch (IOException ignored) {}

    }
    private String getMotDePasseServeur(){

        String motDePasseServeur = "";

        File pwd = new File("JavaFX/src/main/resources/serveur/pwd");

        try {
            //si le fichier n'existe pas, le créé avec pour mot de passe "admin".
            if (pwd.createNewFile()){

                FileWriter fileWriter = new FileWriter(pwd);
                fileWriter.write(BCrypt.hashpw("admin", BCrypt.gensalt()));
                fileWriter.close();
            }

            Scanner scanner = new Scanner(pwd);
            motDePasseServeur = scanner.nextLine();
            scanner.close();

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }

        return motDePasseServeur;
    }

    public Set<Vote> consulterVotes() throws FeedbackException {
        Set<Vote> votes = stockageServeur.getVotes();
        if (votes.size() == 0)
            throw new AucunVoteException();
        return votes;
    }

    public Vote consulterResultats(int idVote) throws FeedbackException {
        Vote vote = stockageServeur.getVote(idVote);

        if (vote == null)
            throw new VoteInexistantException();
        if (!vote.estFini())
            throw new VoteNonTermineException();
        return vote;
    }

    public void creerVote(String intitule, String option1, String option2, LocalDateTime dateFin) throws FeedbackException, IOException, ClassNotFoundException {
        if (dateFin.isAfter(LocalDateTime.of(LocalDate.of(9999, 12, 31), LocalTime.of(23, 59)))
                || dateFin.isBefore(LocalDateTime.of(LocalDate.of(1, 1, 1), LocalTime.of(0, 0))))
            throw new DateTimeException("Date hors limites");
        scrutateur.ecrirePaquet(new CreerVotePaquet());
        CreerVoteFeedbackPaquet paquet = (CreerVoteFeedbackPaquet) scrutateur.lireFeedback();
        stockageServeur.creerVote(new Vote(paquet.getIdVote(), intitule, option1, option2, dateFin).setUrne(paquet.getChiffre()));
    }

    public void terminerVote(int idVote) throws FeedbackException, IOException, ClassNotFoundException {


        Vote vote = stockageServeur.getVote(idVote);
        int nbBulletins = vote.getNbBulletins();
        if (nbBulletins == 0)
            nbBulletins = 1;

        // récupère le résultat en clair
        scrutateur.ecrirePaquet(new DechiffrerPaquet(idVote, vote.getUrne(), nbBulletins));
        DechiffrerFeedbackPaquet paquet = (DechiffrerFeedbackPaquet) scrutateur.lireFeedback();
        stockageServeur.terminerVote(idVote, paquet.getResultat());

        // envoie un mail aux utilisateurs
        for (Utilisateur utilisateur : stockageServeur.getUtilisateurs()) {
            if (stockageServeur.aVote(utilisateur.getLogin(), idVote)) {
                try {
                    new Mail().envoyerMailFinVote(utilisateur.getEmail(),stockageServeur.getVote(idVote));
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Set<Utilisateur> consulterUtilisateurs() throws FeedbackException, SQLException {
        Set<Utilisateur> utilisateurs = stockageServeur.getUtilisateurs();
        if (utilisateurs.size() == 0)
            throw new AucunUtilisateurException();
        return utilisateurs;
    }

    public void creerUtilisateur(String login, String email, int tailleMotDePasse){
        String characteresValides = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890&é(-è_çà)=$*ù!;,?./§%£µ+°@^\\|[{#";
        Random random = new Random();

        String motDePasse = "";

        for (int i = 0; i < tailleMotDePasse; i++){
            motDePasse += characteresValides.charAt(random.nextInt(characteresValides.length()));
        }

        stockageServeur.creerUtilisateur(new Utilisateur(login, BCrypt.hashpw(motDePasse, BCrypt.gensalt()), email));

        try {
            new Mail().envoyerMailCreationCompte(email,stockageServeur.getUtilisateur(login),motDePasse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void supprimerUtilisateur(String login) throws SQLException {
        stockageServeur.supprimerUtilisateur(login);
        deconnecter(login);
    }

    public void modifierUtilisateurEmail(String login, String email) throws SQLException {
        stockageServeur.mettreAJourUtilisateurEmail(new Utilisateur(login, "", email));
    }

    private synchronized void deconnecter(String login) {
        utilisateursAuthentifies.remove(login);
    }

    public synchronized boolean estConnecteScrutateur() {
        try {
            scrutateur.ecrirePaquet(new HeartbeatPaquet());
            scrutateur.lirePaquet();
            return true;
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            return false;
        }
    }

    public void run() throws SQLException, IOException {

        //lance le thread d'arrêt des votes, il s'occupe de vérifier chaque heure si les votes sont terminés ou non, et de demander leur terminaison s'il le faut.
        new ThreadArretVotes().start();

        new Thread(() -> {
            while (true) {
                try {
                    // attend une connexion et la traite séparément afin d'écouter de nouveau
                    //new ThreadGestionConnexion(new Connexion(sslServerSocket.accept())).start();
                    new ThreadGestionConnexion(new Connexion((Socket) serverSocket.accept())).start();
                } catch (IOException ignored) {}
            }
        }).start();
    }

    public void setStockageServeur(IStockageServeur stockageServeur) {
        this.stockageServeur = stockageServeur;
    }

    public void setScrutateur(EmetteurConnexion scrutateur) {
        this.scrutateur= scrutateur;
    }


    private class ThreadGestionConnexion extends Thread {
        Connexion connexion;

        public ThreadGestionConnexion(Connexion connexion) {
            this.connexion = connexion;
        }

        @Override
        public void run() {
            try {
                // gère la connexion selon l'identité de l'auteur de la connexion
                switch (((IdentificationPaquet) connexion.lirePaquet()).getIdentification()) {

                    case CLIENT:
                        new ThreadConnexionClient(new RecepteurConnexion(connexion)).start();
                        break;

                    case SCRUTATEUR:
                        // attribue la connexion s'il n'y pas déjà une connexion scrutateur
                        if (estConnecteScrutateur())
                            connexion.ecrirePaquet(new DeconnexionPaquet());
                        else
                            scrutateur = new EmetteurConnexion(connexion);
                        break;
                }
            } catch (IOException | ClassNotFoundException ignored) {}
        }
    }

    private class ThreadConnexionClient extends Thread {
        private RecepteurConnexion client;
        private String idUtilisateurCourant;

        public ThreadConnexionClient(RecepteurConnexion client) throws IOException {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // attend un paquet du client
                    Paquet paquet = client.lirePaquet();
                    try {
                        // traîte le paquet
                        switch (paquet.getType()) {
                            // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                            case AUTHENTIFICATION:
                                AuthentificationPaquet authPaquet = (AuthentificationPaquet) paquet;
                                if (estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new SessionDejaAuthentifieException());
                                else if (estAuthentifie(authPaquet.getUtilisateur().getLogin()))
                                    client.ecrireException(new UtilisateurDejaAuthentifieException());
                                else {
                                    Utilisateur utilisateur = stockageServeur.getUtilisateur(authPaquet.getUtilisateur().getLogin());
                                    if (utilisateur == null || !BCrypt.checkpw(authPaquet.getUtilisateur().getMotDePasse(), utilisateur.getMotDePasse())){
                                        client.ecrireException(new AuthentificationException());
                                    }
                                    else {
                                        idUtilisateurCourant = authPaquet.getUtilisateur().getLogin();
                                        authentifier();
                                        client.ecrireConfirmation();
                                    }
                                }
                                break;

                            case DECONNEXION:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    deconnecter(idUtilisateurCourant);
                                    client.ecrireConfirmation();
                                }
                                break;

                            case DEMANDER_CLE_PUBLIQUE:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else
                                    client.ecrirePaquet(demanderClePublique(((DemanderClePubliquePaquet) paquet).getIdVote()));
                                break;

                            case DEMANDER_VOTES:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Set<Vote> votes = stockageServeur.getVotes();
                                    if (votes.size() == 0)
                                        client.ecrireException(new AucunVoteException());
                                    else
                                        client.ecrirePaquet(new VotesPaquet(votes));
                                }
                                break;

                            case DEMANDER_RESULTAT:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Vote vote = stockageServeur.getVote(((DemanderResultatPaquet) paquet).getIdVote());
                                    if (!vote.estFini())
                                        client.ecrireException(new VoteNonTermineException());
                                    else
                                        client.ecrirePaquet(new ResultatFeedbackPaquet(vote));
                                }
                                break;

                            case DEMANDER_Utilisateur:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    Utilisateur utilisateur = stockageServeur.getUtilisateur(idUtilisateurCourant);
                                    client.ecrirePaquet(new UtilisateurFeedbackPaquet(utilisateur));
                                }
                                break;

                            case BULLETIN:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());
                                else {
                                    BulletinPaquet bulPaquet = (BulletinPaquet) paquet;
                                    if (stockageServeur.aVote(idUtilisateurCourant, bulPaquet.getIdVote()))
                                        client.ecrireException(new DejaVoteException());
                                    else {
                                        Vote vote = stockageServeur.getVote(bulPaquet.getIdVote());
                                        if (vote == null)
                                            client.ecrireException(new VoteInexistantException());
                                        else if (vote.estFini())
                                            client.ecrireException(new VoteTermineException());
                                        else {
                                            System.out.println("1");
                                            scrutateur.ecrirePaquet(new DemanderClePubliquePaquet(bulPaquet.getIdVote()));
                                            System.out.println("2");
                                            try {
                                                // agrège le bulletin dans l'urne
                                                ClePubliqueFeedbackPaquet clePaquet = (ClePubliqueFeedbackPaquet) scrutateur.lireFeedback();
                                                Chiffre bulletin = bulPaquet.getBulletin();
                                                System.out.println("3");
                                                stockageServeur.updateUrne(
                                                        bulPaquet.getIdVote(),
                                                        Chiffrement.agreger(bulletin, vote.getUrne(), clePaquet.getClePublique())
                                                );
                                                System.out.println("4");
                                                stockageServeur.voter(idUtilisateurCourant, bulPaquet.getIdVote());
                                                System.out.println("5");
                                                client.ecrireConfirmation();
                                                System.out.println("6");
                                                new Mail().envoyerMailDepotBulletin(stockageServeur.getUtilisateur(idUtilisateurCourant).getEmail(),stockageServeur.getVote(bulPaquet.getIdVote()));
                                                System.out.println("7");
                                            } catch (FeedbackException e) {
                                                client.ecrireException(e);
                                            } catch (Exception e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    }
                                }
                                break;

                            case CHANGER_MOT_DE_PASSE:
                                if (!estAuthentifie(idUtilisateurCourant))
                                    client.ecrireException(new UtilisateurDeconnecteException());

                                ChangerMotDePassePaquet changerMotDePassePaquet = (ChangerMotDePassePaquet) paquet;

                                //vérifie que le mot de passe corresponde

                                if (!BCrypt.checkpw(changerMotDePassePaquet.getAncienMotDePasse(), stockageServeur.getUtilisateur(changerMotDePassePaquet.getLogin()).getMotDePasse())){
                                    client.ecrireException(new AuthentificationException());
                                }
                                else {
                                    stockageServeur.mettreAJourUtilisateurMotDePasse(new Utilisateur(
                                            changerMotDePassePaquet.getLogin(),
                                            BCrypt.hashpw(changerMotDePassePaquet.getNouveauMotDePasse(), BCrypt.gensalt())
                                    ));
                                    client.ecrireConfirmation();
                                }
                                break;
                        }
                    } catch (IOException | ClassNotFoundException | NullPointerException e) {
                        client.ecrireException(new ScrutateurDeconnecteException());
                        e.printStackTrace();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                deconnecter(idUtilisateurCourant);
            }
        }

        private synchronized ClePubliqueFeedbackPaquet demanderClePublique(int idVote) throws IOException, ClassNotFoundException {
            scrutateur.ecrirePaquet(new DemanderClePubliquePaquet(idVote));
            return (ClePubliqueFeedbackPaquet) scrutateur.transfererFeedback();
        }

        private synchronized void authentifier() {
            utilisateursAuthentifies.add(idUtilisateurCourant);
        }

        private synchronized boolean estAuthentifie(String login) {
            return login != null && utilisateursAuthentifies.contains(login);
        }
    }
}
