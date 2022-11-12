package app;

import controller.Serveur;

import java.util.Scanner;

public class AppServeur {

    public static void main(String[] args) {
        
        System.out.println("Création du serveur... ");
        Serveur serveur = new Serveur();

        System.out.println("Controller.Serveur lancé, connexions client possibles");
        serveur.run();

        //gestion des choix possibles de l'administrateur
        Scanner sc = new Scanner(System.in);
        String input;
        System.out.println("Entrez : 1 pour ajouter un utilisateur\n         2 pour supprimer un utilisateur\n         3 pour modifier un utilisateur\n         4 pour créer un vote");

        do {
            String rep;
            input = sc.nextLine();
            switch (input) {
                case "1":
                    do{
                        //demande des informations du nouveau utilisateur
                        System.out.print("Veuillez entre le login: ");
                        String login = sc.nextLine();
                        System.out.print("Veuillez entre le mot de passe: ");
                        String mdp = sc.nextLine();
                        //confirmation des données saisies
                        System.out.println("Login: "+login+", mot de passe: "+mdp);

                        System.out.println("Voulez-vous créer l'utlisiteur ("+login+"): tapez 1");
                        System.out.println("Voulez-vous modifier les données saisies: tapez 2");
                        System.out.println("Voulez-vous annuler la création d'un nouveau utilisateur: tapez 3");

                        rep = sc.nextLine();
                        if ( rep.equals("1")){
                            if(serveur.creerUtilisateur(login, mdp)){
                                System.out.println("L'utilisateur "+login+" a été créé avec succès");
                            }else{
                                System.out.println("L'enregistrement a échoué");
                            }
                            rep = "3";
                        }
                    }while(!rep .equals("3"));
                    break;
                case "2":
                    
                    break;
                case "3":
                    
                    break;
                case "4":
                    System.out.print("Veuillez l\'intitulé du vote: ");
                    String intitule = sc.nextLine();
                    System.out.print("Veuillez entre le chois n°1: ");
                    String c1 = sc.nextLine();
                    System.out.print("Veuillez entre le chois n°2: ");
                    String c2 = sc.nextLine();

                    serveur.creerVote(intitule, c1, c2);
                break;
                default:
                    System.out.println("Commande non reconnue");
            }
        } while (!input.equals("x"));

        System.out.println("DataObject.Vote terminé");
        serveur.arreterVote();

        System.out.println(serveur.consulterResultats());
    }
}
