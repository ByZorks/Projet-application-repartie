import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, RemoteException {
        ServiceServeur service = new ServiceBD();
        List<Restaurant> restaurants = service.getRestaurants();



        for (Restaurant restaurant : restaurants) {
            System.out.println("ID: " + restaurant.getId());
            System.out.println("Nom: " + restaurant.getNom());
            System.out.println("Adresse: " + restaurant.getAdresse());
            System.out.println("Latitude: " + restaurant.getLatitude());
            System.out.println("Longitude: " + restaurant.getLongitude());
            System.out.println("-----------------------------");
        }




    }
}
