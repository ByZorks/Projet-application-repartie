import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class LancerServiceBD {
    public static void main(String[] args) {
        try{
            Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
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
