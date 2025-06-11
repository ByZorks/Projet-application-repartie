import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ServiceData extends Remote {
    // Méthode pour récupérer la liste des restaurants
    String getRestaurants() throws RemoteException, SQLException;

    // Méthode pour récupérer un restaurant par son ID
    boolean addReservation(Reservation res) throws RemoteException;

    // Méthode pour récupérer la première reservation par sa date et son heure
    String getTables(String date, String heure) throws RemoteException, SQLException;

}
