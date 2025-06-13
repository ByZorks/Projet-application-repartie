import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HttpServerHandlerEvent implements HttpHandler {
    
    private ServiceEvenement serviceEvent;

    public HttpServerHandlerEvent(ServiceEvenement serviceEvent) {
        this.serviceEvent = serviceEvent;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        String response;
        try {
            if (serviceEvent != null) {
                response = serviceEvent.getIncidents();
            } else {
                response = "Service de données non initialisé";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = "Erreur: " + e.getMessage();
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
