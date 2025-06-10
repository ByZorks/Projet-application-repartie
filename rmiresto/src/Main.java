import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        Connection connection = DriverManager.getConnection(url, "gros43u", "geoffrey");
        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
        );

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
