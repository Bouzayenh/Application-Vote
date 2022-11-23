package controller;

import controller.communication.Connexion;
import controller.communication.EmetteurConnexion;
import controller.database.ScrutateurCBDD;
import dataobject.ClePublique;
import dataobject.exception.ConnexionBDDException;
import dataobject.exception.VoteInexistantException;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
import dataobject.paquet.feedback.DechiffrerFeedbackPaquet;
import datastatic.Chiffrement;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.SQLException;

public class Scrutateur {
    private int l;

    private EmetteurConnexion serveur;
    private ScrutateurCBDD connexionBDD;

    public Scrutateur(int l) throws IOException, ClassNotFoundException, SQLException {
        this.l = l;

        serveur = new EmetteurConnexion(new Socket("localhost", 2999));
        serveur.ecrirePaquet(new IdentificationPaquet(Connexion.Source.SCRUTATEUR));
        connexionBDD = new ScrutateurCBDD();
    }

    public void run() {
        try {
            boucle:while (true) {
                try {
                    // attend un paquet du serveur
                    Paquet paquet = serveur.lirePaquet();
                    ClePublique clePublique;

                    // traîte le paquet
                    switch (paquet.getType()) {
                        // TODO on pourra externaliser chaque cas comme méthode pour que ce soit plus clair

                        case HEARTBEAT:
                            serveur.ecrireConfirmation();
                            break;

                        case DECONNEXION:
                            break boucle;

                        case DEMANDER_CLE_PUBLIQUE:
                            clePublique = connexionBDD.selectClePublique(((DemanderClePubliquePaquet) paquet).getIdVote());
                            if (clePublique == null)
                                serveur.ecrireException(new VoteInexistantException());
                            else
                                serveur.ecrirePaquet(new ClePubliqueFeedbackPaquet(clePublique));
                            break;

                        case CREER_VOTE:
                            BigInteger[] cles = Chiffrement.keygen(l);
                            connexionBDD.insertVote(((CreerVotePaquet) paquet).getIdVote(), cles[0], cles[1], cles[2], cles[3]);
                            serveur.ecrireConfirmation();
                            break;

                        case DECHIFFRER:
                            DechiffrerPaquet dechiffrerPaquet = (DechiffrerPaquet) paquet;
                            clePublique = connexionBDD.selectClePublique(dechiffrerPaquet.getIdVote());
                            BigInteger clePrivee = connexionBDD.selectClePrivee(dechiffrerPaquet.getIdVote());
                            if (clePublique == null || clePrivee == null)
                                serveur.ecrireException(new VoteInexistantException());
                            else
                                serveur.ecrirePaquet(new DechiffrerFeedbackPaquet(
                                        Chiffrement.decrypt(dechiffrerPaquet.getChiffre(), clePublique, clePrivee)
                                ));
                            break;
                    }
                } catch (SQLException e) {
                    serveur.ecrireException(new ConnexionBDDException());
                }
            }
        } catch (IOException | ClassNotFoundException ignored) {}
    }
}