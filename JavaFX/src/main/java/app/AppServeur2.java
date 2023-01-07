package app;

import controller.Serveur;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;
import dataobject.exception.MauvaisMotDePasseServeurException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.util.Scanner;
import java.util.Set;

public class AppServeur2 {

    static Serveur serveur;

    public static void main(String[] args) {

        try {
            // initialisation
            System.out.println("Ouverture du serveur...");
            serveur = Serveur.getInstance();
            System.out.println("Serveur ouvert. Lancement...");
            serveur.run();
            System.out.println("Serveur lancé. Connexions possibles");

            while (true) {
                while (serveur.estVerouille()){
                    try {
                        serveur.deverouiller(demanderString("Entrez le mot de passe afin de dévérouiller le serveur"));
                        System.out.println("Serveur dévérouillé.");

                    }catch (MauvaisMotDePasseServeurException e){
                        System.out.println(e.getMessage());
                    }
                }

                while (!serveur.estVerouille()){

                    try {

                        int choix = demanderInt(
                                "\nChoisissez l'une des options suivantes :\n"
                                        + " [1] Consulter la liste des votes\n"
                                        + " [2] Créer un vote\n"
                                        + " [3] Consulter les résultats d'un vote\n"
                                        + " [4] Consulter la liste des utilisateurs\n"
                                        + " [5] Créer un utilisateur\n"
                                        + " [6] Modifier un utilisateur\n"
                                        + " [7] Supprimer un utilisateur\n"
                                        + " [8] Changer le mot de passe du serveur\n"
                                        + " [9] Verrouiller le serveur\n"
                                        + " [0] Arrêter le serveur"
                        );

                        switch (choix){

                            case 1:
                                afficherVotes();
                                break;

                            case 2:
                                creerVote();
                                break;

                            case 3:
                                consulterResultat();
                                break;

                            case 4:
                                consulterListeUtilisateur();
                                break;

                            case 5:
                                creerUtilisateur();
                                break;

                            case 6:
                                modifierUtilisateur();
                                break;

                            case 7:
                                supprimerUtilisateur();
                                break;

                            case 8:
                                changerMotDePasse();
                                break;

                            case 9:
                                verrouillerServeur();
                                break;

                            case 0:
                                arreterServeur();
                                break;

                            case -1:
                                System.out.println("Commande non-reconnue.");
                        }
                    } catch (FeedbackException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    } catch (ClassNotFoundException | IOException | NullPointerException e){
                        System.out.println("Erreur : Impossible de se connecter au scrutateur.");
                    } catch (DateTimeException e){
                        System.out.println("Erreur : Date invalide.");
                    }
                }
            }

        } catch (IOException | SQLException e) {
            System.out.println("Erreur critique : Arrêt du serveur");
        }
    }

    public static String demanderString (String intitule){
        Scanner scanner = new Scanner(System.in);

        System.out.println(intitule);
        System.out.print(">");

        return scanner.nextLine();
    }

    public static int demanderInt (String intitule){
        Scanner scanner = new Scanner(System.in);

        System.out.println(intitule);
        System.out.print(">");

        try {
            return Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public static void afficherVotes() throws FeedbackException {
        Set<Vote> votes = serveur.consulterVotes();
        for (Vote vote : votes) {
            System.out.print("[" + vote.getIdentifiant() + "] " + vote.getIntitule()
                    + " (" + vote.getOption1() + " ou " + vote.getOption2() + ")");
            if (vote.estFini())
                System.out.println(" - Terminé");
            else
                System.out.println();
        }
    }

    public static void creerVote() throws FeedbackException, IOException, ClassNotFoundException, DateTimeException {
        serveur.creerVote(
                demanderString("Intitulé du vote :"),
                demanderString("Option 1 :"),
                demanderString("Option 2 :"),
                LocalDateTime.of(
                        LocalDate.of(
                                demanderInt("Annee de fin :"),
                                demanderInt("Mois de fin :"),
                                demanderInt("Jour de fin :")
                        ),
                        LocalTime.of(demanderInt("Heure de fin :"), 0)
                )
        );
    }

    public static void consulterResultat() throws FeedbackException {

        Vote vote = serveur.consulterResultats(demanderInt("Identifiant du vote : "));
        System.out.println(vote.getIntitule());
        if (vote.getNbBulletins() == 0)
            System.out.println("Personne n'a participé à ce vote");
        else {
            if (vote.getResultat() < 0.5)
                System.out.println("L'option « " + vote.getOption1() + " » gagne avec "
                        + ((double) (int) ((1 - vote.getResultat()) * 10000)) / 100 + "% des voix");
            else if (vote.getResultat() > 0.5)
                System.out.println("L'option « " + vote.getOption2() + " » gagne avec "
                        + ((double) (int) (vote.getResultat() * 10000)) / 100 + "% des voix");
            else
                System.out.println("Les options « " + vote.getOption1() +
                        " » et « " + vote.getOption2() + " » sont à égalité");
        }
    }

    public static void consulterListeUtilisateur() throws SQLException, FeedbackException {
        Set<Utilisateur> utilisateurs = serveur.consulterUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs)
            System.out.println(utilisateur.getLogin() + " (" + utilisateur.getEmail() + ")");
    }

    public static void creerUtilisateur() throws FeedbackException {
        serveur.creerUtilisateur(
                demanderString("Login : "),
                demanderString("Adresse E-Mail : "),
                10
        );
    }

    public static void modifierUtilisateur() throws SQLException, FeedbackException {

        serveur.modifierUtilisateurEmail(
                demanderString("Login : "),
                demanderString("Nouvelle adresse E-Mail : ")
        );
    }

    public static void supprimerUtilisateur() throws SQLException, FeedbackException {

        serveur.supprimerUtilisateur(demanderString("Login : "));
        System.out.println("Utilisateur supprimé");
    }

    public static void verrouillerServeur(){
        System.out.println("Verrouillage du serveur.");
        serveur.verouiller();
    }

    public static void arreterServeur(){
        System.out.println("Arrêt du serveyr");
        System.exit(0); //extrêmement irresponsable
    }

    public static void changerMotDePasse() throws FeedbackException {
        serveur.changerMotDePasseServeur(
                demanderString("Ancien mot de passe : "),
                demanderString("Nouveau mot de passe : ")
        );

        System.out.println("Le mot de passe du serveur a bien été changé.");
    }
}
