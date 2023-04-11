import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


import controller.Scrutateur;
import controller.Serveur;
import controller.communication.EmetteurConnexion;
import controller.communication.RecepteurConnexion;
import controller.stockage.IStockageScrutateur;
import controller.stockage.IStockageServeur;
import dataobject.*;
import dataobject.exception.AucunUtilisateurException;
import dataobject.exception.AucunVoteException;
import dataobject.exception.FeedbackException;
import dataobject.exception.VoteInexistantException;
import dataobject.paquet.*;
import dataobject.paquet.feedback.ClePubliqueFeedbackPaquet;
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
import static org.mockito.ArgumentMatchers.any;

public class ScrutateurTests {

    private Scrutateur scrutateur;

    @Mock
    private RecepteurConnexion serveur;
    @Mock
    private IStockageScrutateur stockageScrutateur;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws IOException, ClassNotFoundException, SQLException {
        MockitoAnnotations.openMocks(this);
        closeable = MockitoAnnotations.openMocks(this);
        int l = 2048;
        stockageScrutateur = Mockito.mock(IStockageScrutateur.class);
        serveur = Mockito.mock(RecepteurConnexion.class);
        scrutateur = new Scrutateur(l, stockageScrutateur, serveur);

    }

    @AfterEach
    //Fermer les mocks
    public void closeMocks() throws Exception{
        closeable.close();
    }

    @Test
    public void testHeartbeatPacket() throws IOException, ClassNotFoundException {
        Paquet heartbeatPacket = new HeartbeatPaquet();
        scrutateur.processPacket(heartbeatPacket);
        verify(serveur, times(1)).ecrireConfirmation();
    }

    @Test
    public void testDemanderClePublique() throws IOException, ClassNotFoundException, SQLException, VoteInexistantException {
        int idVote = 1;
        ClePublique clePublique = new ClePublique(BigInteger.ONE, BigInteger.TWO, BigInteger.TEN);
        when(stockageScrutateur.getClePublique(idVote)).thenReturn(clePublique);

        DemanderClePubliquePaquet demanderClePubliquePaquet = new DemanderClePubliquePaquet(idVote);
        scrutateur.processPacket(demanderClePubliquePaquet);

        verify(stockageScrutateur, times(1)).getClePublique(idVote);
        verify(serveur,times(1)).ecrirePaquet(any(ClePubliqueFeedbackPaquet.class));
    }

    @Test
    // ce test prend du temps pour s'executer a cause de la methode keygen
    public void testCreerVoteScrutateur() throws IOException, ClassNotFoundException, SQLException {
        Paquet creerVotePacket = new CreerVotePaquet();
        BigInteger[] cles = Chiffrement.keygen(1024);
        int idVote = 1;
        when(stockageScrutateur.insererVote(cles[0], cles[1], cles[2], cles[3])).thenReturn(idVote);

        scrutateur.processPacket(creerVotePacket);

        verify(stockageScrutateur, times(1)).insererVote(any(BigInteger.class), any(BigInteger.class), any(BigInteger.class), any(BigInteger.class));
        verify(serveur, times(1)).ecrirePaquet(any(CreerVoteFeedbackPaquet.class));
    }
}

