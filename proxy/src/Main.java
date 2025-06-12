
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, RemoteException {
        ServiceData service = new ServiceBD();
        service.getTables("1", "20/06/2025", "12:30");
    }
}
