/**
 * La classe Reservation représente une réservation effectuée dans un restaurant.
 * Elle contient des informations sur l'identifiant de la réservation, l'identifiant du restaurant,
 * le nom et le prénom de la personne ayant effectué la réservation, le nombre de convives,
 * le numéro de téléphone pour la confirmation, ainsi que la date de la réservation.
 */

public class Reservation {
    private int id;
    private int restaurantId;
    private String nom;
    private String prenom;
    private int nombreConvives;
    private String telephone;
    private String dateReservation;

    /**
    * Constructeur par défaut de la classe Reservation.
    */
    public Reservation() {
    }

    /**
    * Constructeur de la classe Reservation avec paramètres.
    *
    * @param id L'identifiant unique de la réservation.
    * @param restaurantId L'identifiant du restaurant pour lequel la réservation est faite.
    * @param nom Le nom de la personne ayant effectué la réservation.
    * @param prenom Le prénom de la personne ayant effectué la réservation.
    * @param nombreConvives Le nombre de convives pour la réservation.
    * @param telephone Le numéro de téléphone pour contacter la personne ayant effectué la réservation.
    * @param dateReservation La date à laquelle la réservation est prévue.
    */
    public Reservation(int id, int restaurantId, String nom, String prenom, int nombreConvives, String telephone, String dateReservation) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.nom = nom;
        this.prenom = prenom;
        this.nombreConvives = nombreConvives;
        this.telephone = telephone;
        this.dateReservation = dateReservation;
    }

    /**
    * Récupère l'identifiant de la réservation.
    *
    * @return L'identifiant de la réservation.
    */
    public int getId() {
        return id;
    }

    /**
    * Définit l'identifiant de la réservation.
    *
    * @param id Le nouvel identifiant de la réservation.
    */
    public void setId(int id) {
        this.id = id;
    }

    /**
    * Récupère l'identifiant du restaurant.
    *
    * @return L'identifiant du restaurant.
    */
    public int getRestaurantId() {
        return restaurantId;
    }

    /**
    * Définit l'identifiant du restaurant.
    *
    * @param restaurantId Le nouvel identifiant du restaurant.
    */
    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * Récupère le nom de la personne ayant effectué la réservation.
     *
     * @return Le nom de la personne.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la personne ayant effectué la réservation.
     *
     * @param nom Le nouveau nom de la personne.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Récupère le prénom de la personne ayant effectué la réservation.
     *
     * @return Le prénom de la personne.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de la personne ayant effectué la réservation.
     *
     * @param prenom Le nouveau prénom de la personne.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Récupère le nombre de convives pour la réservation.
     *
     * @return Le nombre de convives.
     */
    public int getNombreConvives() {
        return nombreConvives;
    }

    /**
     * Définit le nombre de convives pour la réservation.
     *
     * @param nombreConvives Le nouveau nombre de convives.
     */
    public void setNombreConvives(int nombreConvives) {
        this.nombreConvives = nombreConvives;
    }

    /**
     * Récupère le numéro de téléphone pour contacter la personne ayant effectué la réservation.
     *
     * @return Le numéro de téléphone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone pour contacter la personne ayant effectué la réservation.
     *
     * @param telephone Le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Récupère la date de la réservation.
     *
     * @return La date de la réservation.
     */
    public String getDateReservation() {
        return dateReservation;
    }
}
