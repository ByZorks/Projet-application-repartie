/**
 * La classe Restaurant représente un restaurant avec ses attributs principaux.
 * Elle contient des informations telles que l'identifiant unique du restaurant,
 * son nom, son adresse, ainsi que ses coordonnées géographiques (latitude et longitude).
 */
public class Restaurant {
    private int id;
    private String nom;
    private String adresse;
    private double latitude;
    private double longitude;

    /**
     * Constructeur par défaut de la classe Restaurant.
     */
    public Restaurant() {}

    /**
     * Constructeur de la classe Restaurant avec paramètres.
     *
     * @param id L'identifiant unique du restaurant.
     * @param nom Le nom du restaurant.
     * @param adresse L'adresse du restaurant.
     * @param latitude La latitude géographique du restaurant.
     * @param longitude La longitude géographique du restaurant.
     */
    public Restaurant(int id, String nom, String adresse, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Récupère l'identifiant du restaurant.
     *
     * @return L'identifiant du restaurant.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'identifiant du restaurant.
     *
     * @param id Le nouvel identifiant du restaurant.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Récupère le nom du restaurant.
     *
     * @return Le nom du restaurant.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du restaurant.
     *
     * @param nom Le nouveau nom du restaurant.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère l'adresse du restaurant.
     *
     * @return L'adresse du restaurant.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse du restaurant.
     *
     * @param adresse La nouvelle adresse du restaurant.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Récupère la latitude géographique du restaurant.
     *
     * @return La latitude du restaurant.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Définit la latitude géographique du restaurant.
     *
     * @param latitude La nouvelle latitude du restaurant.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Récupère la longitude géographique du restaurant.
     *
     * @return La longitude du restaurant.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Définit la longitude géographique du restaurant.
     *
     * @param longitude La nouvelle longitude du restaurant.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
