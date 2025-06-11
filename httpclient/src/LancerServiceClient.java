import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceClient {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
            ServiceCentral event = (ServiceCentral) reg.lookup("ServiceCentral");

            ClientWeb client = new ClientWeb();
            ServiceEvenement serviceEvent = (ServiceEvenement) UnicastRemoteObject.exportObject(client, 0);
            event.enregistrerService(serviceEvent);
            System.out.println("Service client enregistré avec succès.");
        } catch (RemoteException e) {
            System.out.println("RemoteException LancerServiceCentral: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("NotBoundException LancerServiceCentral: " + e.getMessage());
        }
    }
}
