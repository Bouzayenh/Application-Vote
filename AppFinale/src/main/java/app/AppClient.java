package app;

import controller.Client;
import dataobject.Vote;
import dataobject.exception.FeedbackException;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class AppClient {

    public static void main(String[] args) {
        try {
            // initialisation
            System.out.println("Connexion au serveur...");
            Client client = new Client();
            System.out.println("Connecté avec succès");

            Scanner sc = new Scanner(System.in);
            String login, motDePasse, idVote, choix;

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
                                    + "Login :\n"
                                    + "> ");
                            login = sc.nextLine();
                            if (login.equals("q"))
                                break;

                            System.out.print("Mot de passe :\n"
                                    + "> ");
                            motDePasse = sc.nextLine();
                            if (motDePasse.equals("q"))
                                break;

                            client.authentification(login, motDePasse);
                            System.out.println("Authentification réussie");
                            break;

                        case "2":
                            client.deconnexion();
                            System.out.println("Déconnexion réussie");
                            break;

                        case "3":
                            Set<Vote> votes = client.consulterVotes();
                            for (Vote vote : votes) {
                                System.out.print("[" + vote.getIdentifiant() + "] " + vote.getIntitule()
                                        + " (" + vote.getOption1() + " ou " + vote.getOption2() + ")");
                                if (vote.estFini())
                                    System.out.println(" - Terminé");
                                else
                                    System.out.println();
                            }
                            break;

                        case "4":
                            System.out.print("Entrez [q] à tout moment pour annuler le dépôt du bulletin :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            idVote = sc.nextLine();
                            if (idVote.equals("q"))
                                break;

                            System.out.print("Choix (1 ou 2) :\n"
                                    + "> ");
                            choix = sc.nextLine();
                            if (choix.equals("q"))
                                break;

                            client.voter(Integer.parseInt(choix), Integer.parseInt(idVote));
                            System.out.println("Bulletin déposé");
                            break;

                        case "5":
                            System.out.print("Entrez [q] à tout moment pour annuler la consultation des résultats du vote :\n"
                                    + "Identifiant du vote :\n"
                                    + "> ");
                            idVote = sc.nextLine();
                            if (idVote.equals("q"))
                                break;

                            Vote vote = client.consulterResultats(Integer.parseInt(idVote));
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

                        case "x":
                            break boucle;

                        default:
                            System.out.println("Commande non reconnue");
                    }
                } catch (FeedbackException e) {
                    System.out.println("Erreur : " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : Pas un nombre");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur critique : Impossible de se connecter au serveur. Arrêt du client");
        }
    }
}
