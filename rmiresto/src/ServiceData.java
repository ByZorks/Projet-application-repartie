

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ServiceData extends Remote {
    // Méthode pour récupérer la liste des restaurants
    String getRestaurants() throws RemoteException, SQLException;

    // Méthode pour récupérer un restaurant par son ID
    boolean addReservation(Reservation res) throws RemoteException;
    public ArrayList<Integer> getTables(int idresto, String date, int nb) throws RemoteException;


}
