import controller.Client;
import controller.communication.EmetteurConnexion;

import dataobject.Vote;
import dataobject.exception.FeedbackException;
import dataobject.paquet.AuthentificationPaquet;
import dataobject.paquet.DeconnexionPaquet;

import dataobject.paquet.feedback.VotesPaquet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class ClientTests {

    private Client client;

    @Mock
    private EmetteurConnexion emetteurConnexion;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        closeable = MockitoAnnotations.openMocks(this);
        emetteurConnexion = Mockito.mock(EmetteurConnexion.class);

        client = new Client(emetteurConnexion);
    }

    @AfterEach
    //Fermer les mocks
    public void closeMocks() throws Exception{
        closeable.close();
    }

    @Test
    public void testAuthentification() throws IOException, ClassNotFoundException, FeedbackException {
        String identifiant = "user1";
        String motDePasse = "password123";

        client.authentification(identifiant, motDePasse);

        verify(emetteurConnexion, times(1)).ecrirePaquet(argThat(p -> p instanceof AuthentificationPaquet && ((AuthentificationPaquet) p).getUtilisateur().getLogin().equals(identifiant) && ((AuthentificationPaquet) p).getUtilisateur().getMotDePasse().equals(motDePasse)));
        verify(emetteurConnexion, times(1)).lireFeedback();
    }

    @Test
    public void testDeconnexion() throws IOException, ClassNotFoundException, FeedbackException {
        client.deconnexion();

        verify(emetteurConnexion, times(1)).ecrirePaquet(argThat(p -> p instanceof DeconnexionPaquet));
        verify(emetteurConnexion, times(1)).lireFeedback();
    }


    @Test
    public void testConsulterVotesEnCours() throws FeedbackException, IOException, ClassNotFoundException {
        Set<Vote> mockVotes = new HashSet<>();
        mockVotes.add(new Vote(1, "Intitulé 1", "Option 1", "Option 2", LocalDateTime.now()));
        mockVotes.add(new Vote(2, "Intitulé 2", "Option 1", "Option 2", LocalDateTime.now()));
        mockVotes.add(new Vote(3, "Intitulé 3", "Option 1", "Option 2", LocalDateTime.now()));

        when(emetteurConnexion.lireFeedback()).thenReturn(new VotesPaquet(mockVotes));

        List<Vote> votesEnCours = client.consulterVotesEnCours();
        assertEquals(3, votesEnCours.size());
        assertEquals(1, votesEnCours.get(0).getIdentifiant());
        assertEquals(2, votesEnCours.get(1).getIdentifiant());
    }
}