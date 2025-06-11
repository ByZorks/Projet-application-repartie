import java.rmi.Remote;
import java.rmi.RemoteException;
import serviceBD.ServiceData;
public interface ServiceCentrale extends Remote {
    public void setServiceBD(ServiceData service) throws RemoteException;
    public void setServiceEvent(ServiceEvent serviceEvent) throws RemoteException;
}
