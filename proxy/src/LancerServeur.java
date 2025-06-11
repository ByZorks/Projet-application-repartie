
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServeur {
    public static void main(String[] args) throws IOException {

        HttpServeur s = new HttpServeur();
        ServiceCentrale service = (ServiceCentrale) UnicastRemoteObject.exportObject(s, 0);
        int port = 1099; // Port par défaut pour RMI
        if(args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Port invalide, utilisation du port par défaut 1099");
            }
        }
        String ip = "localhost"; // Adresse IP par défaut
        if(args.length > 1) {
            ip = args[1];
        }

        // Enregistrement de la centrale dans le registre RMI
        LocateRegistry.createRegistry(port);
        Registry reg = LocateRegistry.getRegistry(ip, port);
        reg.rebind("Centrale", service);
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/BD", s);
        server.setExecutor(null); // Executor par défaut
        server.start();
        System.out.println("Serveur lancé sur http://localhost:8000/BD");
    }
}
