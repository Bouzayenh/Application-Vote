package app;

import controller.Serveur;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class AppServeur {

    public static void main(String[] args) {
        try {
            // initialisation
            System.out.println("Ouverture du serveur...");
            Serveur serveur = new Serveur();
            System.out.println("Serveur ouvert. Lancement...");
            serveur.run();
            System.out.println("Serveur lancé. Connexions possibles");

            Scanner sc = new Scanner(System.in);
            String intitule, option1, option2, idVote, login, motDePasse, email;

            // interface de l'administrateur
            while (true) {
                System.out.print("\nChoisissez l'une des options suivantes :\n"
                        + " [1] Consulter la liste des votes\n"
                        + " [2] Créer un vote\n"
                        + " [3] Terminer un vote\n"
                        + " [4] Consulter les résultats d'un vote\n"
                        + " [5] Consulter la liste des utilisateurs\n"
                        + " [6] Créer un utilisateur\n"
                        + " [7] Supprimer un utilisateur\n"
                        + " [8] Modifier un utilisateur\n"
                        + " [x] Arrêter le serveur\n"
                        + "> ");
                String input = sc.nextLine().toLowerCase();

                try {
                    switch (input) {
                        // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                        case "1":
                            Set<Vote> votes = serveur.consulterVotes();
                            for (Vote vote : votes) {
                                System.out.print("[" + vote.getIdentifiant() + "] " + vote.getIntitule()
                                        + " (" + vote.getOption1() + " ou " + vote.getOption2() + ")");
                                if (vote.estFini())
                                    System.out.println(" - Terminé");
                                else
                                    System.out.println();
                            }
                            break;

                        case "2":
                            System.out.print("Entrez [q] à tout moment pour annuler la création du vote :\n"
                                    + "Intitulé du vote :\n"
                                    + "> ");
                            intitule = sc.nextLine();
                            if (intitule.equals("q"))
                                break;

                            System.out.print("Première option :\n"
                                    + "> ");
                            option1 = sc.nextLine();
                            if (option1.equals("q"))
                                break;

                            System.out.print("Deuxième option :\n"
                                    + "> ");
                            option2 = sc.nextLine();
                            if (option2.equals("q"))
                                break;

                            serveur.creerVote(intitule, option1, option2);
                            System.out.println("Vote créé");
                            break;

                        case "3":
                            System.out.print("Entrez [q] à tout moment pour annuler la terminaison du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            idVote = sc.nextLine();
                            if (idVote.equals("q"))
                                break;

                            serveur.terminerVote(Integer.parseInt(idVote));
                            System.out.println("Vote terminé");
                            break;

                        case "4":
                            System.out.print("Entrez [q] à tout moment pour annuler la consultation des résultats du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            idVote = sc.nextLine();
                            if (idVote.equals("q"))
                                break;

                            Vote vote = serveur.consulterResultats(Integer.parseInt(idVote));
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
                            break;

                        case "5":
                            Set<Utilisateur> utilisateurs = serveur.consulterUtilisateurs();
                            for (Utilisateur utilisateur : utilisateurs)
                                System.out.println(utilisateur.getLogin() + " (" + utilisateur.getEmail() + ")");
                            break;

                        case "6":
                            System.out.print("Entrez [q] à tout moment pour annuler la création de l'utilisateur :\n"
                                    + "Login :\n"
                                    + "> ");
                            login = sc.nextLine();
                            if (login.equals("q"))
                                break;

                            System.out.print("Adresse email :\n"
                                    + "> ");
                            email = sc.nextLine();
                            if (email.equals("q"))
                                break;

                            serveur.creerUtilisateur(login, email, 10);
                            System.out.println("Utilisateur créé");
                            break;

                        case "7":
                            System.out.print("Entrez [q] à tout moment pour annuler la suppression de l'utilisateur :\n"
                                    + "Login :\n"
                                    + "> ");
                            login = sc.nextLine();
                            if (login.equals("q"))
                                break;

                            serveur.supprimerUtilisateur(login);
                            System.out.println("Utilisateur supprimé");
                            break;

                        case "8":
                            System.out.print("Entrez [q] à tout moment pour annuler la modification de l'utilisateur :\n"
                                    + "Login :\n"
                                    + "> ");
                            login = sc.nextLine();
                            if (login.equals("q"))
                                break;

                            System.out.print("Nouvelle adresse email (rien pour ne pas changer)\n" +
                                    "> ");
                            email = sc.nextLine();
                            if (email.equals("q"))
                                break;

                            serveur.modifierUtilisateurEmail(login, email);
                            System.out.println("Utilisateur modifié");
                            break;

                        case "x":
                            System.exit(0); // TODO Extrêmement irresponsable

                        default:
                            System.out.println("Commande non reconnue");
                    }
                } catch (FeedbackException e) {
                    System.out.println("Erreur : " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : Pas un nombre");
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    if (!serveur.estConnecteScrutateur())
                        System.out.println("Erreur : Impossible de se connecter au scrutateur");
                    else
                        throw e;
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.out.println("Erreur critique : Arrêt du serveur");
            e.printStackTrace();
        }
    }
}


