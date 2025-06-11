import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceBD implements ServiceData{
    private Statement statement;
    // Constructeur
    public ServiceBD() throws ClassNotFoundException, SQLException {
        // Initialisation ou configuration si nécessaire
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        Connection connection = DriverManager.getConnection(url, "gros43u", "geoffrey");
        statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        System.out.println("ServiceBD initialisé avec succès");
    }

    @Override
    public String getRestaurants() throws RemoteException, SQLException {
        String query = "SELECT * FROM RESTAURANTS";
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println(resultSet);
        List<Restaurant> restaurants = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String nom = resultSet.getString("NOM");
            String adresse = resultSet.getString("ADRESSE");
            double latitude = resultSet.getDouble("LATITUDE");
            double longitude = resultSet.getDouble("LONGITUDE");

            Restaurant restaurant = new Restaurant(id, nom, adresse, latitude, longitude);
            restaurants.add(restaurant);
        }
        return toJson(restaurants);
    }

    private String toJson(List<Restaurant> restaurants) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant r = restaurants.get(i);
            json.append("{")
                    .append("\"id\":").append(r.getId()).append(",")
                    .append("\"nom\":\"").append(r.getNom()).append("\",")
                    .append("\"adresse\":\"").append(r.getAdresse()).append("\",")
                    .append("\"latitude\":").append(r.getLatitude()).append(",")
                    .append("\"longitude\":").append(r.getLongitude())
                    .append("}");
            if (i < restaurants.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }


    @Override
    public boolean addReservation(Reservation res) throws RemoteException {
        try {
            String query = "INSERT INTO RESERVATIONS (ID, RESTAURANT_ID, DATE_RESERVATION, NOMBRE_PERSONNES) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, res.getId());
            preparedStatement.setInt(2, res.getRestaurantId());
            preparedStatement.setDate(3, Date.valueOf(res.getDateReservation()));
            preparedStatement.setInt(4, res.getNombreConvives());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
