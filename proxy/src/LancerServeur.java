import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class LancerServeur {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        // Configuration des paramètres
        int rmiPort = (args.length > 0) ? Integer.parseInt(args[0]) : 1099;
        String ip = (args.length > 1) ? args[1] : "localhost";

        // Création du registre RMI
        try {
            LocateRegistry.createRegistry(rmiPort);
            System.out.println("Registre RMI créé sur le port " + rmiPort);
        } catch (Exception e) {
            System.out.println("Registre RMI déjà existant");
        }

        // Initialisation de HttpServeur
        Serveur httpServeur = new Serveur();
        ServiceCentrale serviceCentrale = (ServiceCentrale) UnicastRemoteObject.exportObject(httpServeur, 0);

        // Enregistrement dans le registre RMI
        Registry registry = LocateRegistry.getRegistry(ip, rmiPort);
        registry.rebind("Centrale", serviceCentrale);
        System.out.println("Service centrale enregistré dans RMI");

        // Initialisation du service BD
        ServiceBD serviceBD = new ServiceBD();
        ServiceData serviceBDExporte = (ServiceData) UnicastRemoteObject.exportObject(serviceBD, 0);
        httpServeur.setServiceBD(serviceBDExporte);

        // Initialisation du service d'événements
        ServiceEvenement serviceEvent = new ClientWeb();
        ServiceEvenement serviceEventExporte = (ServiceEvenement) UnicastRemoteObject.exportObject(serviceEvent, 0);
        httpServeur.setServiceEvent(serviceEventExporte);

        // Configuration et démarrage du serveur HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        HttpServerHandlerBD httpServeurHandler = new HttpServerHandlerBD(serviceBDExporte);
        server.createContext("/BD", httpServeurHandler);
        HttpServerHandlerEvent httpServeurHandlerEvent = new HttpServerHandlerEvent(serviceEventExporte);
        server.createContext("/evenement", httpServeurHandlerEvent);
        server.setExecutor(null);
        server.start();
        System.out.println("Serveur HTTP lancé sur http://localhost:8000/BD");
    }
}