import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

/**
 * La classe Main est le point d'entrée de l'application.
 * Elle initialise un service de base de données et le rend accessible via RMI (Remote Method Invocation).
 * Cela permet à d'autres applications de se connecter et d'utiliser les services de la base de données à distance.
 */
public class Main {

    /**
    * Méthode principale qui initialise et enregistre le service de base de données via RMI.
    *
    * @param args les arguments de la ligne de commande.
    *             args[0] peut spécifier le port sur lequel le registre RMI sera créé.
    *             args[1] peut spécifier l'adresse IP du registre RMI.
    * @throws SQLException si une erreur de base de données se produit.
    * @throws ClassNotFoundException si la classe du pilote JDBC n'est pas trouvée.
    * @throws RemoteException si une erreur RMI se produit.
    */
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
        LocateRegistry.createRegistry(port);
        Registry reg = LocateRegistry.getRegistry(ip, port);
        reg.rebind("BD", serviceBD);
    }
}
