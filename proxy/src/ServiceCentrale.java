
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceCentrale extends Remote {
    public void setServiceBD(ServiceData service) throws RemoteException;
    public void setServiceEvent(ServiceEvenement serviceEvent) throws RemoteException;
}
