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

public class HttpServerHandlerBD implements HttpHandler {

    private ServiceData serviceData;

    public HttpServerHandlerBD(ServiceData serviceData) {
        this.serviceData = serviceData;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        String response = "";

        // Répondre aux requêtes OPTIONS (pré-vol)
        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            httpExchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        if(httpExchange.getRequestMethod().equals("GET")) {
            try {
                if (serviceData != null) {
                    response = serviceData.getRestaurants();
                } else {
                    response = "Service de données non initialisé";
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                response = "Erreur: " + e.getMessage();
            }catch (SQLException e2){
                response = "Erreur: " + e2.getMessage();
            }
        } else if (httpExchange.getRequestMethod().equals("POST")) {
            try {
                if (serviceData != null) {
                    InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                    String postData = sb.toString();
                    try {
                        JsonObject json = JsonParser.parseString(postData).getAsJsonObject();
                        String restoId = json.get("restaurantId").getAsString();
                        String date = json.get("date").getAsString();
                        String heure = json.get("heure").getAsString();
                        try{
                            String nom = sanitize(json.get("nom").getAsString());
                            String prenom = sanitize(json.get("prenom").getAsString());
                            String phone = sanitize(json.get("telephone").getAsString());
                            int idtable = json.get("table").getAsInt();
                            int nbr = json.get("nbPersonnes").getAsInt();
                            serviceData.addReservation(new Reservation(-1,Integer.parseInt(restoId), nom, prenom, nbr, phone, date + " " + heure + ":00"));
                        }catch (Exception e){
                            e.printStackTrace();
                            int table = serviceData.getTables(restoId, date, heure);
                            response = String.valueOf(table);
                        }


                    } catch (Exception e) {

                    }


                } else {
                    response = "Service de données non initialisé";
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                response = "Erreur: " + e.getMessage();
            }

        }


        // Correction de l'encodage pour la réponse
        byte[] responseBytes = response.getBytes("UTF-8");
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