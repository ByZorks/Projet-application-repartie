import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HttpServerHandlerBD implements HttpHandler {

    private Serveur serveur;

    public HttpServerHandlerBD(Serveur serviceData) {
        this.serveur = serviceData;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Passer dans le handle BD");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        String response = "";

        if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
            try {
                if (serveur.serviceData != null) {
                    response = serveur.serviceData.getRestaurants();
                } else {
                    response = "{\"error\": \"Service de données non initialisé\"}";
                }
            } catch (RemoteException | SQLException e) {
                e.printStackTrace();
                response = "{\"error\": \"Erreur serveur : " + e.getMessage().replace("\"", "'") + "\"}";
            }
        } else if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {
            try {
                if (serveur != null) {
                    InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    String restoId = json.get("restaurantId").getAsString();
                    String date = json.get("date").getAsString();
                    String heure = json.get("heure").getAsString();
                    int nbr = json.get("nbPersonnes").getAsInt();
                    String fullDate = date + " " + heure + ":00";

                    try{
                        String nom = sanitize(json.get("nom").getAsString());
                        String prenom = sanitize(json.get("prenom").getAsString());
                        String phone = sanitize(json.get("telephone").getAsString());

                        serveur.serviceData.addReservation(new Reservation(-1, Integer.parseInt(restoId), nom, prenom, nbr, phone, fullDate));
                        response = "{\"message\": \"Réservation ajoutée avec succès.\"}";
                    }catch (NullPointerException e){
                        ArrayList<Integer> tables = serveur.serviceData.getTables(Integer.parseInt(restoId), fullDate, nbr);
                        if(!tables.isEmpty()){
                            response = "{\"message\": \"Le restaurant peut vous accueillir.\"}";
                        }else{
                            response = "{\"error\": \"Il n'y a plus de places.\"}";
                        }
                    }
                } else {
                    response = "{\"error\": \"Service de données non initialisé\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "{\"error\": \"Erreur lors du traitement : " + e.getMessage().replace("\"", "'") + "\"}";
            }
        } else {
            response = "{\"error\": \"Méthode HTTP non supportée.\"}";
        }

        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }



    public static String sanitize(String input) {
        return input == null ? null : input.replaceAll("[<>\"']", ""); // simple filtre XSS
    }
}