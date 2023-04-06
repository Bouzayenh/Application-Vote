package controller.config;

/**
 * Contient des constantes permettant de définir le comportement des contrôleurs
 */
public class Conf {

    public enum BasesDeDonnees{
        /**
         * {@link controller.stockage.StockageServeurOracle}
         */
        ORACLE,

        /**
         * {@link controller.stockage.StockageServeurMySQL}
         */
        MYSQL
    }

    /**
     * Définie le type de base de données que le serveur doit utiliser pour stocker les informations sur les votes
     */
    public final static BasesDeDonnees BASE_DE_DONNEES =
            System.getenv("BDD") == null
                    ? BasesDeDonnees.ORACLE
                    : System.getenv("BDD").equals("MYSQL")
                        ? BasesDeDonnees.MYSQL
                        : BasesDeDonnees.ORACLE;


    /**
     * true si les sockets utilisés doivent être SSL
     */
    public final static boolean UTILISE_SSL =
            System.getenv("UTILISE_SSL") != null && Boolean.parseBoolean(System.getenv("UTILISE_SSL"));

    /**
     * Défini le numéro de port à utiliser pour les sockets
     */
    public final static int PORT = System.getenv("PORT") == null
            ? 15000
            : Integer.parseInt(System.getenv("PORT"));

    /**
     * true si le déchiffrement doit tester toutes les valeurs entières possibles (retire une couche de sécurité)
     * Risque de rechercher infiniment.
     * Permet de simuler les attaques
     */
    public final static boolean DECHIFFREMENT_EXHAUSTIF = true;

    /**
     * true si le client peut voter une valeur différente de 0 ou 1
     * Permet de simuler les attaques
     */
    public final static boolean CLIENT_MALVEILLANT = true;
}
