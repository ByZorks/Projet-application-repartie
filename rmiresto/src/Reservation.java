
public class Reservation {
    private int id;
    private int restaurantId;
    private String nom;
    private String prenom;
    private int nombreConvives;
    private String telephone;
    private String dateReservation;

    public Reservation() {
    }

    public Reservation(int id, int restaurantId, String nom, String prenom, int nombreConvives, String telephone) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.nom = nom;
        this.prenom = prenom;
        this.nombreConvives = nombreConvives;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNombreConvives() {
        return nombreConvives;
    }

    public void setNombreConvives(int nombreConvives) {
        this.nombreConvives = nombreConvives;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDateReservation() {
        return dateReservation;
    }
}
