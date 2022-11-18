package app;

import controller.Serveur;
import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

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
            String intitule, option1, option2, identifiant, motDePasse;

            // interface de l'administrateur
            boucle:while (true) {
                System.out.print("\nChoisissez l'une des options suivantes :\n"
                        + " [1] Créer un vote\n"
                        + " [2] Consulter les votes\n"
                        + " [3] Terminer un vote\n"
                        + " [4] Consulter les résultats d'un vote\n"
                        + " [5] Créer un utilisateur\n"
                        + " [x] Arrêter le serveur\n"
                        + "> ");
                String input = sc.nextLine().toLowerCase();

                try {
                    switch (input) {
                        // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                        case "1":
                            System.out.print("Entrez [q] à tout moment pour annuler la création du vote :\n"
                                    + "Intitulé du vote :\n"
                                    + "> ");
                            intitule = sc.nextLine();
                            if (intitule.equals("q")) break;

                            System.out.print("Première option :\n"
                                    + "> ");
                            option1 = sc.nextLine();
                            if (option1.equals("q")) break;

                            System.out.print("Deuxième option :\n"
                                    + "> ");
                            option2 = sc.nextLine();
                            if (option2.equals("q")) break;

                            serveur.creerVote(intitule, option1, option2);
                            System.out.println("Vote créé");
                            break;

                        case "2":
                            Map<Integer, Vote> votes = serveur.consulterVotes();
                            System.out.println("Identifiant | Intitulé | Option 1 | Option 2");
                            for (Vote vote : votes.values())
                                System.out.println(vote.getIdentifiant() + " | "
                                        + vote.getIntitule() + " | "
                                        + vote.getOption1() + " | "
                                        + vote.getOption2());
                            break;

                        case "3":
                            System.out.print("Entrez [q] à tout moment pour annuler la terminaison du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            serveur.terminerVote(Integer.parseInt(identifiant));
                            System.out.println("Vote terminé");
                            break;

                        case "4":
                            System.out.print("Entrez [q] à tout moment pour annuler la consultation des résultats du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            Vote vote = serveur.consulterResultats(Integer.parseInt(identifiant));
                            System.out.println(vote.getIntitule());
                            double resultat = vote.getUrne() / (double) vote.getNbBulletins();
                            if (resultat > 0.5) System.out.println("L'option « " + vote.getOption1() +
                                    " » gagne avec " + ((double) (int) (resultat * 10000)) / 100 + "% des voix");
                            else if (resultat < 0.5) System.out.println("L'option « " + vote.getOption2() +
                                    " » gagne avec " + ((double) (int) ((1 - resultat) * 10000)) / 100 + "% des voix");
                            else System.out.println("Les options « " + vote.getOption1() +
                                        " » et « " + vote.getOption2() + " » sont à égalité");
                            break;

                        case "5":
                            System.out.print("Entrez [q] à tout moment pour annuler la création de l'utilisateur :\n"
                                    + "Identifiant de l'utilisateur :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            System.out.print("Mot de passe :\n"
                                    + "> ");
                            motDePasse = sc.nextLine();
                            if (motDePasse.equals("q")) break;

                            serveur.creerUtilisateur(identifiant, motDePasse);
                            break;

                        case "x":
                            break boucle;

                        default:
                            System.out.println("Commande non reconnue");
                    }

                } catch (FeedbackException e) {
                    System.out.println("Erreur : " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : Pas un nombre");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    if (!serveur.estConnecteScrutateur())
                        System.out.println("Erreur : Impossible de se connecter au scrutateur");
                    else
                        throw e;
                }
            }

        } catch (IOException | ClassNotFoundException | SQLException e) {
            System.out.println("Erreur critique : Arrêt du serveur");
        }
    }
}
