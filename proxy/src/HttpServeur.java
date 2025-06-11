import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.rmi.RemoteException;
import java.sql.SQLException;


public class HttpServeur implements HttpHandler, ServiceCentrale {

    ServiceData serviceData;
    ServiceEvenement serviceEvent;

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "This is the response";
        System.out.println(t.getRequestURI().getPath());
        if(t.getRequestURI().getPath().equals("/BD")) {
            try {
                response = serviceData.getRestaurants();
                System.out.println("ouiiiii");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }catch (RemoteException e) {
                System.out.println("RemoteException: " + e.getMessage());
            }
        }else if(t.getRequestURI().getPath().equals("/service/event")) {
            try {
                response = serviceEvent.getIncidents();
            } catch (RemoteException e) {
                System.out.println("RemoteException: " + e.getMessage());
            }
        } else {
            response = "Invalid endpoint";
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

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



