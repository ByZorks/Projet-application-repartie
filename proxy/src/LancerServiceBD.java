import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class LancerServiceBD {
    public static void main(String[] args) {
        try{
            String host = args[0] != null ? args[0] : "localhost";
            int port = args[1] != null ? Integer.parseInt(args[1]) : 1099;
            Registry registry = LocateRegistry.getRegistry(host, port);
            ServiceCentrale serveurHttp = (ServiceCentrale) registry.lookup("Centrale");
            ServiceBD serviceBD = new ServiceBD();
            ServiceData serviceData = (ServiceData) UnicastRemoteObject.exportObject(serviceBD, 0);
            serveurHttp.setServiceBD(serviceData);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
