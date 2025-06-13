import io.github.cdimascio.dotenv.Dotenv;

/**
 * La classe lireENV permet de charger et d'accéder aux variables d'environnement
 * stockées dans un fichier .env. Elle utilise la bibliothèque Dotenv pour lire
 * les variables et fournir des méthodes pour accéder aux informations de
 * configuration de la base de données.
 */

public class lireENV {

    Dotenv dotenv;

    /**
     * Constructeur de la classe lireENV.
     * Charge les variables d'environnement à partir du fichier .env.
     */
    lireENV(){
        dotenv = Dotenv.load();
    }

    /**
    * Récupère le nom d'utilisateur de la base de données à partir des variables d'environnement.
    *
    * @return une chaîne de caractères représentant le nom d'utilisateur de la base de données.
    */
    public String getUser(){
        return dotenv.get("DB_USER");
    }

    /**
    * Récupère le mot de passe de la base de données à partir des variables d'environnement.
    *
    * @return une chaîne de caractères représentant le mot de passe de la base de données.
    */
    public String getPassword(){
        return dotenv.get("DB_PASSWORD");
    }

    /**
    * Récupère l'URL de la base de données à partir des variables d'environnement.
    *
    * @return une chaîne de caractères représentant l'URL de la base de données.
    */
    public String getURL(){
        return dotenv.get("DB_URL");
    }
}
