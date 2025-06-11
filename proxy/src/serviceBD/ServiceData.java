package serviceBD;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ServiceData extends Remote {
    // Méthode pour récupérer la liste des restaurants
    String getRestaurants() throws RemoteException, SQLException;

    // Méthode pour récupérer un restaurant par son ID
    boolean addReservation(Reservation res) throws RemoteException;



}
