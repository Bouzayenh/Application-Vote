import dataobject.Chiffre;
import dataobject.Chiffrement;
import dataobject.ClePublique;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Classe de teste pour les méthodes de chiffrement
 * Vérifie que les propriétés du chiffrement El-Gamal sont respectées
 */
public class TestChiffrement {

    private static ClePublique clePublique;
    private static BigInteger clePrivee;

    /**
     * Créé un jeu de clés pour l'ensemble des tests
     */
    @BeforeAll
    static void setUp(){
        BigInteger[] trousseauDeCle = Chiffrement.keygen(200); //taille de 200 par simplicité

        clePublique = new ClePublique(
                trousseauDeCle[0],
                trousseauDeCle[1],
                trousseauDeCle[2]
        );

        clePrivee = trousseauDeCle[3];
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5}) // différents messages à tester
    @DisplayName("Chiffrer deux fois la même valeur donne des chiffrés différents")
    void chiffresDifferents(int m){
        Chiffre c1 = Chiffrement.encrypt(m, clePublique);
        Chiffre c2 = Chiffrement.encrypt(m, clePublique);

        assertNotEquals(c1, c2);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 5})
    @DisplayName("Déchiffrer une valeur préalablement chiffrée redonne la même valeur")
    void chiffrementPuisDechiffrement(int m){

        Chiffre c = Chiffrement.encrypt(m, clePublique);

        int dechiffre = Chiffrement.decrypt(c, Integer.MAX_VALUE, clePublique, clePrivee);

        assertEquals(m, dechiffre);
    }

    @Test
    @DisplayName("Si un message clair n'est pas trouvé dans l'intervale précisée, le déchiffrement renvoi -1")
    void dechiffrementInvalide(){
        Chiffre c = Chiffrement.encrypt(50, clePublique);

        int dechiffre = Chiffrement.decrypt(c, 10, clePublique, clePrivee);

        assertEquals(-1, dechiffre);
    }

    @Test
    @DisplayName("L'aggregation donne un nouveau chiffré")
    void aggreagationDonneNouveauChiffre(){
        Chiffre c1 = Chiffrement.encrypt(0, clePublique);
        Chiffre c2 = Chiffrement.encrypt(0, clePublique);

        Chiffre aggrege = Chiffrement.agreger(c1, c2, clePublique);

        assertNotEquals(c1, aggrege);
        assertNotEquals(c2, aggrege);
    }


    @Test
    @DisplayName("Le déchiffrement d'une aggregation donne la somme des deux messages clairs")
    void aggregationAdditionne(){
        Chiffre c1 = Chiffrement.encrypt(5, clePublique);
        Chiffre c2 = Chiffrement.encrypt(7, clePublique);

        Chiffre aggrege = Chiffrement.agreger(c1, c2, clePublique);

        int dechiffre = Chiffrement.decrypt(aggrege, 20, clePublique, clePrivee);

        assertEquals(5 + 7, dechiffre);
    }
}
