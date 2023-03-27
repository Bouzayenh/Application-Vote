import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import controller.Serveur;
import controller.communication.EmetteurConnexion;
import controller.stockage.IStockageServeur;
import dataobject.Chiffre;
import dataobject.Utilisateur;
import dataobject.Vote;
import dataobject.exception.AucunUtilisateurException;
import dataobject.exception.AucunVoteException;
import dataobject.exception.FeedbackException;
import dataobject.paquet.CreerVotePaquet;
import dataobject.paquet.DechiffrerPaquet;
import dataobject.paquet.feedback.CreerVoteFeedbackPaquet;
import dataobject.paquet.feedback.DechiffrerFeedbackPaquet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class serveurTests {

    @Mock
    private EmetteurConnexion scrutateur;

    @Mock
    private Serveur serveur;

    @Mock
    private IStockageServeur stockageServeur;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        closeable = MockitoAnnotations.openMocks(this);
        serveur = Serveur.getInstance();
        serveur.setScrutateur(scrutateur);
        serveur.setStockageServeur(stockageServeur);
    }

    @AfterEach
    //Fermer les mocks
    public void closeMocks() throws Exception{
        closeable.close();
    }


    @Test
    public void testConsulterVotes() throws Exception {
        Set<Vote> votes = new HashSet<>();
        votes.add(new Vote(1, "Intitulé 1", "Option 1", "Option 2", LocalDateTime.now()));
        votes.add(new Vote(2, "Intitulé 2", "Option 1", "Option 2", LocalDateTime.now()));

        when(stockageServeur.getVotes()).thenReturn(votes);
        Set<Vote> result = serveur.consulterVotes();
        verify(stockageServeur).getVotes();
        assertEquals(votes, result);
    }

    @Test
    public void testConsulterVotesAucunVote() throws Exception {
        when(stockageServeur.getVotes()).thenReturn(Collections.emptySet());
        assertThrows(AucunVoteException.class, () -> serveur.consulterVotes());
        verify(stockageServeur).getVotes();
    }


    @Test
    public void testConsulterUtilisateurs() throws SQLException, FeedbackException {
        Set<Utilisateur> expectedUtilisateurs = new HashSet<>();
        expectedUtilisateurs.add(new Utilisateur("user1", "hashedPassword1", "user1@email.com"));
        expectedUtilisateurs.add(new Utilisateur("user2", "hashedPassword2", "user2@email.com"));


        when(stockageServeur.getUtilisateurs()).thenReturn(expectedUtilisateurs);


        Set<Utilisateur> actualUtilisateurs = serveur.consulterUtilisateurs();

        assertEquals(expectedUtilisateurs, actualUtilisateurs);
        verify(stockageServeur, times(1)).getUtilisateurs();
    }

    @Test
    public void testConsulterUtilisateursAucunUtilisateur() throws Exception {
        when(stockageServeur.getUtilisateurs()).thenReturn(Collections.emptySet());
        assertThrows(AucunUtilisateurException.class, () -> serveur.consulterUtilisateurs());
        verify(stockageServeur).getUtilisateurs();
    }

    @Test
    public void testSupprimerUtilisateur() throws Exception {
        String login = "user1";

        doNothing().when(stockageServeur).supprimerUtilisateur(login);
        serveur.supprimerUtilisateur(login);
        verify(stockageServeur).supprimerUtilisateur(login);
    }

    @Test
    public void testCreerVote() throws IOException, ClassNotFoundException, FeedbackException {
        String intitule = "Intitule du vote";
        String option1 = "Option 1";
        String option2 = "Option 2";
        LocalDateTime dateFin = LocalDateTime.now().plusDays(1);

        int idVote = 1;
        Chiffre chiffre = new Chiffre(new BigInteger("1"), new BigInteger("2"));
        CreerVoteFeedbackPaquet paquet = new CreerVoteFeedbackPaquet(idVote, chiffre);
        when(scrutateur.lireFeedback()).thenReturn(paquet);

        serveur.creerVote(intitule, option1, option2, dateFin);

        verify(scrutateur, times(1)).ecrirePaquet(any(CreerVotePaquet.class));
        verify(scrutateur, times(1)).lireFeedback();
        verify(stockageServeur, times(1)).creerVote(Mockito.any(Vote.class));
    }

    @Test
    public void testTerminerVote() throws FeedbackException, IOException, ClassNotFoundException {
        int idVote = 1;
        Vote vote = new Vote(idVote, "Intitule du vote", "Option 1", "Option 2", LocalDateTime.now().plusDays(1));
        when(stockageServeur.getVote(idVote)).thenReturn(vote);
        DechiffrerFeedbackPaquet paquet = new DechiffrerFeedbackPaquet(5);
        when(scrutateur.lireFeedback()).thenReturn(paquet);

        serveur.terminerVote(idVote);

        verify(scrutateur, times(1)).ecrirePaquet(any(DechiffrerPaquet.class));
        verify(scrutateur, times(1)).lireFeedback();
        verify(stockageServeur, times(1)).terminerVote(idVote, paquet.getResultat());
    }

    ///a finir
}