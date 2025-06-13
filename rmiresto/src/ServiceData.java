import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

/**
 * L'interface ServiceData définit les méthodes distantes disponibles pour
 * interagir avec les données des restaurants et des réservations.
 * Ces méthodes permettent de récupérer des informations sur les restaurants
 * et les réservations, ainsi que d'ajouter de nouvelles réservations.
 */
public interface ServiceData extends Remote {

    /**
     * Récupère la liste des restaurants sous forme de chaîne JSON.
     *
     * @return une chaîne JSON représentant la liste des restaurants.
     * @throws RemoteException si une erreur RMI se produit lors de l'appel distant.
     * @throws SQLException si une erreur SQL se produit lors de l'accès à la base de données.
     */
    String getRestaurants() throws RemoteException, SQLException;

    /**
     * Ajoute une nouvelle réservation à la base de données.
     *
     * @param res l'objet Reservation contenant les détails de la réservation à ajouter.
     * @return true si la réservation a été ajoutée avec succès, false sinon.
     * @throws RemoteException si une erreur RMI se produit lors de l'appel distant.
     */
    boolean addReservation(Reservation res) throws RemoteException;

    /**
     * Récupère les détails de la première réservation trouvée pour une date et une heure spécifiques.
     *
     * @param date la date de la réservation à récupérer.
     * @param heure l'heure de la réservation à récupérer.
     * @return une chaîne JSON représentant les détails de la réservation.
     * @throws RemoteException si une erreur RMI se produit lors de l'appel distant.
     * @throws SQLException si une erreur SQL se produit lors de l'accès à la base de données.
     */
    String getTables(String date, String heure) throws RemoteException, SQLException;
}
