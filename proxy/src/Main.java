
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, RemoteException {
        ServiceData service = new ServiceBD();

        ServiceData serviceBD = (ServiceData) UnicastRemoteObject.exportObject(service, 0);
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
        Registry reg = LocateRegistry.getRegistry(ip, port);
        try {
            ServiceCentrale s = (ServiceCentrale) reg.lookup("Centrale");
            s.setServiceBD(serviceBD);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
