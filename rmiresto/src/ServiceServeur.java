import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServiceServeur extends Remote {
    // Méthode pour récupérer la liste des restaurants
    List<Restaurant> getRestaurants() throws RemoteException;

    // Méthode pour récupérer un restaurant par son ID
    Restaurant addReservation(Reservation res) throws RemoteException;



}
