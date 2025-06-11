import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceEvenement extends Remote {

    String getIncidents() throws RemoteException;
}
