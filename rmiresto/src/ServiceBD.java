import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe ServiceBD implémente l'interface ServiceData pour fournir des services
 * de gestion de base de données pour les restaurants et les réservations.
 * Elle se connecte à une base de données Oracle et offre des méthodes pour récupérer
 * et ajouter des informations sur les restaurants et les réservations.
 */
public class ServiceBD implements ServiceData {
    private Statement statement;

    /**
     * Constructeur de la classe ServiceBD.
     * Initialise la connexion à la base de données Oracle et prépare un Statement pour exécuter des requêtes SQL.
     *
     * @throws ClassNotFoundException si le pilote JDBC n'est pas trouvé.
     * @throws SQLException si une erreur SQL se produit lors de la connexion à la base de données.
     */
    public ServiceBD() throws ClassNotFoundException, SQLException {
        // Chargement du pilote JDBC Oracle
        Class.forName("oracle.jdbc.driver.OracleDriver");
        // URL de connexion à la base de données
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        // Connexion à la base de données avec les informations d'identification
        Connection connection = DriverManager.getConnection(url, "gros43u", "geoffrey");
        // Création d'un Statement pour exécuter des requêtes SQL
        statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY
        );
    }

    /**
     * Récupère la liste des restaurants depuis la base de données et la retourne sous forme de chaîne JSON.
     *
     * @return une chaîne JSON représentant la liste des restaurants.
     * @throws RemoteException si une erreur RMI se produit.
     * @throws SQLException si une erreur SQL se produit lors de l'exécution de la requête.
     */
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

    /**
     * Récupère les réservations pour une date et une heure spécifiques et les retourne sous forme de chaîne JSON.
     *
     * @param date la date des réservations à récupérer.
     * @param heure l'heure des réservations à récupérer.
     * @return une chaîne JSON représentant la liste des réservations.
     * @throws RemoteException si une erreur RMI se produit.
     * @throws SQLException si une erreur SQL se produit lors de l'exécution de la requête.
     */
    @Override
    public String getTables(String date, String heure) throws RemoteException, SQLException {
        String query = "select * from reservations\n" +
                "where dateres = to_date('" + date + " " + heure + "','DD/MM/YY HH24:MI')\n" +
                "FETCH FIRST row only;";
        ResultSet resultSet = statement.executeQuery(query);
        System.out.println(resultSet);
        List<Reservation> reservations = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            int restaurantId = resultSet.getInt("RESTAURANT_ID");
            String nom = resultSet.getString("NOM");
            String prenom = resultSet.getString("PRENOM");
            int nombreConvives = resultSet.getInt("NOMBRE_CONVIVES");
            String telephone = resultSet.getString("TELEPHONE");
            String dateReservation = resultSet.getString("DATERES");

            Reservation reservation = new Reservation(id, restaurantId, nom, prenom, nombreConvives, telephone, dateReservation);
            reservations.add(reservation);
        }
        return resToJson(reservations);
    }

    /**
     * Convertit une liste de restaurants en une chaîne JSON.
     *
     * @param restaurants la liste des restaurants à convertir.
     * @return une chaîne JSON représentant la liste des restaurants.
     */
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

    /**
     * Convertit une liste de réservations en une chaîne JSON.
     *
     * @param reservations la liste des réservations à convertir.
     * @return une chaîne JSON représentant la liste des réservations.
     */
    private String resToJson(List<Reservation> reservations) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < reservations.size(); i++) {
            Reservation r = reservations.get(i);
            json.append("{")
                    .append("\"id\":").append(r.getId()).append(",")
                    .append("\"restaurantId\":\"").append(r.getRestaurantId()).append("\",")
                    .append("\"nom\":\"").append(r.getNom()).append("\",")
                    .append("\"prenom\":\"").append(r.getPrenom()).append("\",")
                    .append("\"nombreConvives\":\"").append(r.getNombreConvives()).append("\",")
                    .append("\"telephone\":").append(r.getTelephone()).append(",")
                    .append("\"dateReservation\":").append(r.getDateReservation())
                    .append("}");
            if (i < reservations.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * Ajoute une nouvelle réservation à la base de données.
     *
     * @param res la réservation à ajouter.
     * @return true si la réservation a été ajoutée avec succès, false sinon.
     * @throws RemoteException si une erreur RMI se produit.
     */
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
