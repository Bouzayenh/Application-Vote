import static org.junit.jupiter.api.Assertions.*;

class ScrutateurTest {

    Scrutateur scrutateur; //scrutateur
    int nbMessages = 10; //le nombre de messages à tester
    int nbBitScrutateur = 50; //le nombre de bits que le scrutateur utilisera pour le chiffrement
    int[] listMessage; //liste de messages, de 0 à nbMessages
    Chiffre[] listChiffre; //liste de chiffrés
    int[] listDechiffre; //liste de messages une fois qu'ils seront déchiffrés

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        scrutateur = new Scrutateur(nbBitScrutateur);

        //on remplie listMessage d'entiers de 0 à nbMessages
        listMessage = new int[nbMessages];
        for (int i = 0; i != nbMessages; i++){
            listMessage[i] = i;
        }

        listChiffre = new Chiffre[nbMessages];
        listDechiffre = new int[nbMessages];

    }

    @org.junit.jupiter.api.Test
    void testEncryptPuisDecrypt() {
        //on chiffre tous les messages de listMessage, puis on les déchiffre
        for (int i = 0; i != nbMessages; i++){
            listChiffre[i] = scrutateur.encrypt(listMessage[i]);
            listDechiffre[i] = scrutateur.decrypt(listChiffre[i]);
        }

        //on compare la liste de messages initiale avec la liste chiffrée, puis déchifrée
        assertArrayEquals(listMessage, listDechiffre);
    }

    //prérequis : encrypt() et decrypt() fonctionnent correctement
    @org.junit.jupiter.api.Test
    void testAgreger() {
        //on chiffre tous les messages de listMessage
        for (int i = 0; i != nbMessages; i++){
            listChiffre[i] = scrutateur.encrypt(listMessage[i]);
        }

        //on agrège tout les chiffrés en un seul
        for (int i = 0; i != nbMessages-1; i++){
            listChiffre[0] = scrutateur.agreger(listChiffre[0], listChiffre[i+1]);
        }
        //on déchiffre le message agrégé
        int messageDechiffre = scrutateur.decrypt(listChiffre[0]);

        //on vérifie que la somme des messages déchiffrés est bien égale à la somme des n-1 premiers entiers
        assertEquals((nbMessages-1)*(nbMessages)/2, messageDechiffre);

    }
}