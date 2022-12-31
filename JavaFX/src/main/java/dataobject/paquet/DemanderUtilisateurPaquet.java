package dataobject.paquet;

import dataobject.paquet.feedback.UtilisateurFeedbackPaquet;

/**
 * Paquet servant au {@link controller.Client} à demander le mail d'un utilisateur.<br>
 * Il contient le login d'utilisateur dont on souhaite connaître son mail.<br>
 * En retour, l'émetteur attendra un {@link UtilisateurFeedbackPaquet}.
 */
public class DemanderUtilisateurPaquet extends Paquet {
    public DemanderUtilisateurPaquet() {
        super(Type.DEMANDER_Utilisateur);
    }


}

