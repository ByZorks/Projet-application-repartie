import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceBD implements ServiceData {
    private Statement statement;

    // Constructeur
    public ServiceBD() throws ClassNotFoundException, SQLException {
        lireENV l = new lireENV("./src/jojo.config");
        // Initialisation ou configuration si nécessaire
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection(l.getURL(), l.getUser(), l.getPassword());
        statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
        System.out.println("ServiceBD initialisé avec succès");
    }

    @Override
    public String getRestaurants() throws RemoteException, SQLException {
        System.out.println("getRestaurants");
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
        System.out.println("addReservation");
        try {
            ArrayList<Integer> tables = getTables(res.getRestaurantId(), res.getDateReservation(), res.getNombreConvives());

            if (tables.isEmpty()) throw new SQLException();

            String query = "INSERT INTO RESERVATIONS (RESTAURANT_ID, NOMBRE_CONVIVES, TELEPHONE, DATERES, NOM, PRENOM) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, res.getRestaurantId());
            preparedStatement.setInt(2, res.getNombreConvives());
            preparedStatement.setString(3, res.getTelephone());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(res.getDateReservation()));
            preparedStatement.setString(5, res.getNom());
            preparedStatement.setString(6, res.getPrenom());

            preparedStatement.executeUpdate();

            query = "SELECT * FROM RESERVATIONS WHERE RESTAURANT_ID = ? AND NOMBRE_CONVIVES = ? AND TELEPHONE = ? AND DATERES = ? AND NOM = ? AND PRENOM = ?";
            preparedStatement = statement.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, res.getRestaurantId());
            preparedStatement.setInt(2, res.getNombreConvives());
            preparedStatement.setString(3, res.getTelephone());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(res.getDateReservation()));
            preparedStatement.setString(5, res.getNom());
            preparedStatement.setString(6, res.getPrenom());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                int id = resultSet.getInt("ID");
                for (int i : tables) {
                    query = "INSERT INTO RESERVATIONTABLES (RESERVATION_ID, TABLE_ID) VALUES (?, ?)";
                    preparedStatement = statement.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setInt(1, id);
                    preparedStatement.setInt(2, i);
                    preparedStatement.executeUpdate();
                }
            }





            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Integer> getTables(int idresto, String date, int nb) throws RemoteException {
        System.out.println("date : " + date);
        String query = "SELECT ID,CAPACITE " +
                "FROM TABLES " +
                "WHERE RESTAURANT_ID = ? " +
                "AND ID NOT IN ( " +
                "    SELECT TABLE_ID " +
                "    FROM reservations " +
                "    NATURAL JOIN reservationtables " +
                "    WHERE dateres BETWEEN TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') - INTERVAL '1' HOUR " +
                "                     AND TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') + INTERVAL '1' HOUR " +
                ")";


        try (
                PreparedStatement preparedStatement = statement.getConnection().prepareStatement(query)
        ) {
            preparedStatement.setInt(1, idresto);
            preparedStatement.setString(2, date);
            preparedStatement.setString(3, date);

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Integer> tables = new ArrayList<Integer>();
            int sum = 0;
            while (resultSet.next()) {
                tables.add(resultSet.getInt("ID"));
                sum += resultSet.getInt("CAPACITE");
            }
            System.out.println(sum);
            if (sum < nb) {
                throw new SQLException();
            }
            resultSet.close();
            return tables;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
