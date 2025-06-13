import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HttpServerHandlerEvent implements HttpHandler {
    
    private Serveur serveur;

    public HttpServerHandlerEvent(Serveur serviceEvent) {
        this.serveur = serviceEvent;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        String response;
        try {
            if (serveur.serviceEvent != null) {
                response = serveur.serviceEvent.getIncidents();
            } else {
                response = "{\"error\": \"Erreur lors du chargement des incidents.\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\": \"Erreur lors du traitement : " + e.getMessage().replace("\"", "'") + "\"}";
        }

        // Correction de l'encodage pour la réponse
        byte[] responseBytes = response.getBytes("UTF-8");
        httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBytes);
        os.close();

    }
}
