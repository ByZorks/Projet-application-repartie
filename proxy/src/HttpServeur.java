import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.rmi.RemoteException;


public class HttpServeur implements HttpHandler, ServiceCentrale {

    ServiceData serviceData;
    ServiceEvenement serviceEvent;

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

//Faut un serveur http avec les deux attibuts si dessus
//Creer deux classe handler un pour le service de bd et l'autre pour le service event
//Et quand tu new MyHandler() tu passes le service qu'il faut.

    @Override
    public void setServiceBD(ServiceData service) throws RemoteException {
        serviceData = service;
    }

    @Override
    public void setServiceEvent(ServiceEvenement service) throws RemoteException {
        serviceEvent = service;
    }
}



