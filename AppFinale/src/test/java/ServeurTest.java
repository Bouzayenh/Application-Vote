import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.nio.charset.StandardCharsets;

class ServeurTest {
    Serveur serveur; // serveur

    int longueur; // longueur des Strings générés dans les tests

    String randomString() {
        byte[] array = new byte[longueur];
        Random random = new Random();
        random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    @BeforeEach
    void initialize() {
        serveur = new Serveur();
    }

    @Test
    void testCreerVote() {
        // génération d'un intitulé et d'options aléatoirement
        String intitule = randomString();
        String option1 = randomString();
        String option2 = randomString();

        // création du vote
        serveur.creerVote(100, intitule, option1, option2);

        // vérification que le vote a été créé correctement
        assertNotNull(serveur.getVote());
        assertEquals(intitule, serveur.getVote().getIntitule());
        assertEquals(option1, serveur.getVote().getOption1());
        assertEquals(option2, serveur.getVote().getOption2());
    }
}