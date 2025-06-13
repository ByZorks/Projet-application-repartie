import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class LancerServiceEvent {
    public static void main(String[] args) {
        try{
            String host = args[0] != null ? args[0] : "localhost";
            int port = args[1] != null ? Integer.parseInt(args[1]) : 1099;
            Registry registry = LocateRegistry.getRegistry(host, port);
            ServiceCentrale serveurHttp = (ServiceCentrale) registry.lookup("Centrale");
            ClientWeb clientWeb = new ClientWeb();
            ServiceEvenement serviceEvenement = (ServiceEvenement) UnicastRemoteObject.exportObject(clientWeb, 0);
            serveurHttp.setServiceEvent(serviceEvenement);
        } catch (RemoteException e) {
            System.out.println("Ce n'est peut etre pas la bonne ip ou le bon port ou alors l'annuaire n'existe pas sur cette ip");;
        } catch (NotBoundException e) {
            System.out.println("Le serveur n'est pas dans l'annuaire");;
        }
    }
}
