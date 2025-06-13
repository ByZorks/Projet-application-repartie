import java.rmi.RemoteException;

public class Serveur implements ServiceCentrale {

    public ServiceData serviceData;
    public ServiceEvenement serviceEvent;

    @Override
    public void setServiceBD(ServiceData service) throws RemoteException {
        serviceData = service;
    }

    @Override
    public void setServiceEvent(ServiceEvenement service) throws RemoteException {
        serviceEvent = service;
    }

}