package app;

import controller.Client;
import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class AppClient {

    public static void main(String[] args) {
        try {
            // initialisation
            System.out.println("Connexion au serveur...");
            Client client = new Client();
            System.out.println("Connecté avec succès");

            Scanner sc = new Scanner(System.in);
            String identifiant, motDePasse, choix;

            // interface de l'utilisateur
            boucle:while (true) {
                System.out.print("\nChoisissez l'une des options suivantes :\n"
                        + " [1] S'authentifier\n"
                        + " [2] Se déconnecter\n"
                        + " [3] Consulter les votes\n"
                        + " [4] Voter\n"
                        + " [5] Consulter les résultats d'un vote\n"
                        + " [x] Arrêter le client\n"
                        + "> ");
                String input = sc.nextLine().toLowerCase();

                try {
                    switch (input) {
                        // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                        case "1":
                            System.out.print("Entrez [q] à tout moment pour annuler l'authentification :\n"
                                    + "Identifiant :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            System.out.print("Mot de passe :\n"
                                    + "> ");
                            motDePasse = sc.nextLine();
                            if (motDePasse.equals("q")) break;

                            client.authentification(identifiant, motDePasse);
                            System.out.println("Authentification réussie");
                            break;

                        case "2":
                            client.deconnexion();
                            System.out.println("Déconnexion réussie");
                            break;

                        case "3":
                            Map<Integer, Vote> votes = client.consulterVotes();
                            System.out.println("Identifiant | Intitulé | Option 1 | Option 2");
                            for (Vote vote : votes.values())
                                System.out.println(vote.getIdentifiant() + " | "
                                        + vote.getIntitule() + " | "
                                        + vote.getOption1() + " | "
                                        + vote.getOption2());
                            break;

                        case "4":
                            System.out.print("Entrez [q] à tout moment pour annuler le dépôt du bulletin :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            System.out.print("Choix (1 ou 2) :\n"
                                    + "> ");
                            choix = sc.nextLine();
                            if (choix.equals("q")) break;

                            client.voter(Integer.parseInt(choix), Integer.parseInt(identifiant));
                            break;

                        case "5":
                            System.out.print("Entrez [q] à tout moment pour annuler la consultation des résultats du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            identifiant = sc.nextLine();
                            if (identifiant.equals("q")) break;

                            Vote vote = client.consulterResultats(Integer.parseInt(identifiant));
                            System.out.println(vote.getIntitule());
                            double resultat = vote.getUrne() / (double) vote.getNbBulletins();
                            if (resultat > 0.5) System.out.println("L'option « " + vote.getOption1() +
                                    " » gagne avec " + ((double) (int) (resultat * 10000)) / 100 + "% des voix");
                            else if (resultat < 0.5) System.out.println("L'option « " + vote.getOption2() +
                                    " » gagne avec " + ((double) (int) ((1 - resultat) * 10000)) / 100 + "% des voix");
                            else System.out.println("Les options « " + vote.getOption1() +
                                        " » et « " + vote.getOption2() + " » sont à égalité");
                            break;

                        case "x":
                            break boucle;

                        default:
                            System.out.println("Commande non reconnue");
                    }

                } catch (FeedbackException | NumberFormatException e) {
                    System.out.println("Erreur : " + e.getMessage());
                }
            }

        } catch (IOException | ClassNotFoundException | FeedbackException e) {
            System.out.println("Erreur critique : Impossible de se connecter au serveur. Arrêt du client");
        }
    }
}
