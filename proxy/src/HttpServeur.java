import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.rmi.Remote;
import java.util.*;


public class HttpServeur implements HttpHandler, Remote {


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            System.out.println("Méthode non autorisée: " + exchange.getRequestMethod());
            return;
        }if(!exchange.getRequestURI().getPath().startsWith("/BD")) {
            exchange.sendResponseHeaders(404, -1);
            System.out.println("Chemin non trouvé: " + exchange.getRequestURI().getPath());
            return;
        }
        else {
            System.out.println("Requête reçue: " + exchange.getRequestURI());
        }
    }


}



