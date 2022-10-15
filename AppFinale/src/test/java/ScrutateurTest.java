import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.stream.IntStream;

class ScrutateurTest {
    Serveur serveur; // serveur
    Scrutateur scrutateur; // scrutateur
    Client client; // client

    int[] messages; // liste des messages
    int l = 100; // nombre de bits utilisés par le scrutateur lors de la génération des clés
    int nbMessages = 100; // nombre de messages à tester
    int messageMax = 1000; // valeur maximale d'un message

    @BeforeEach
    void initialize() {
        Random random = new Random();

        serveur = new Serveur();
        serveur.creerVote(l, "", "", "");
        scrutateur = serveur.getScrutateur();
        client = new Client();

        // remplissage de la liste des messages à tester
        messages = new int[nbMessages];
        for (int i = 0; i < nbMessages; i++){
            messages[i] = random.nextInt(messageMax);
        }
    }

    @Test
    void testEncryptPuisDecrypt() {
        // chiffrement puis déchiffrement de tous les messages
        Chiffre[] chiffres = new Chiffre[nbMessages];
        int[] dechiffres = new int[nbMessages];
        Client client = new Client();
        for (int i = 0; i < nbMessages; i++){
            chiffres[i] = client.encrypt(messages[i], scrutateur.getClePublique());
            dechiffres[i] = scrutateur.decrypt(chiffres[i]);
        }

        // comparaison des messages initiaux avec les messages chiffrés puis déchiffrés
        assertArrayEquals(messages, dechiffres);
    }

    /*
     * prérequis : encrypt et decrypt fonctionnent correctement
     */
    @Test
    void testAgreger() {
        // chiffrement de tous les messages
        Chiffre[] chiffres = new Chiffre[nbMessages];
        for (int i = 0; i < nbMessages; i++){
            chiffres[i] = client.encrypt(messages[i], scrutateur.getClePublique());
        }

        // agrégation de tous les messages
        for (int i = 0; i < nbMessages - 1; i++){
            chiffres[0] = serveur.agreger(chiffres[0], chiffres[i+1]);
        }

        // déchiffrement du message agrégé
        int messageDechiffre = scrutateur.decrypt(chiffres[0]);

        // comparaison de la somme des messages initiaux avec la somme des messages chiffrés puis déchiffrés
        assertEquals(IntStream.of(messages).sum(), messageDechiffre);
    }
}