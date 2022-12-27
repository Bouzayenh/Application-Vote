package dataobject;

import controller.Serveur;
import dataobject.exception.FeedbackException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ThreadArretVotes extends Thread{

    private Serveur serveur = Serveur.getInstance();

    public ThreadArretVotes() throws SQLException, IOException {
    }

    /**
     * Parcours l'entièreté des votes du serveur et demande la terminaison de ceux qui doivent se terminer.
     */
    public void verifierVotes(){
        try {
            for (Vote vote : serveur.consulterVotes()) {
                if (serveur.estConnecteScrutateur() && vote.getResultat() == -1 && vote.getDateFin().isBefore(LocalDateTime.now()))
                    serveur.terminerVote(vote.getIdentifiant());
            }
        }
        catch (FeedbackException | IOException | ClassNotFoundException ignored){}
    }

    @Override
    public void run() {

        while (true) {

            verifierVotes();

            try {
                //attends jusqu'à la prochaine heure.
                Thread.sleep(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS)).toMillis());
            } catch (InterruptedException ignored) {
                //le thread ne devrait pas être interrompu
            }
        }
    }
}
