import java.rmi.RemoteException;

public class Serveur implements ServiceCentrale {

    public ServiceData serviceData;
    public ServiceEvenement serviceEvent;

    @Override
    public void setServiceBD(ServiceData service) throws RemoteException {
        System.out.println("Initialisation d'un nouveau service de BD");
        serviceData = service;
    }

    @Override
    public void setServiceEvent(ServiceEvenement service) throws RemoteException {
        System.out.println("Initialisation d'un nouveau service d'évenements");
        serviceEvent = service;
    }

}