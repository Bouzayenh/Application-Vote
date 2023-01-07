package controller.config;

/**
 * Contient des constantes permettant de définir le comportement des controleurs
 */
public class Conf {

    public enum BasesDeDonnees{
        /**
         * {@link controller.database.StockageServeurOracle}
         */
        ORACLE,

        /**
         * {@link controller.database.StockageServeurMySQL}
         */
        MYSQL
    }

    /**
     * Définie le type de base de données que le serveur doit utiliser pour stocker les informations sur les votes.
     */
    public final static BasesDeDonnees BASE_DE_DONNEES = BasesDeDonnees.ORACLE;

    /**
     * true si les sockets utilisés doivent être SSL
     */
    public final static boolean UTILISE_SSL = true;

    /**
     * Défini le numéro de port à utiliser pour les sockets
     */
    public final static int PORT = 3615;
}
