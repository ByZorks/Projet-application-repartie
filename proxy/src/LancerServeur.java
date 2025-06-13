import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class LancerServeur {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        // Configuration des paramètres
        int rmiPort = (args.length > 0) ? Integer.parseInt(args[0]) : 1099;
        String ip = (args.length > 1) ? args[1] : "localhost";

        // Création du registre RMI
        try {
            LocateRegistry.createRegistry(rmiPort);
            System.out.println("Registre RMI créé sur le port " + rmiPort);
        } catch (Exception e) {
            System.out.println("Registre RMI déjà existant");
        }

        // Initialisation de HttpServeur
        Serveur httpServeur = new Serveur();
        ServiceCentrale serviceCentrale = (ServiceCentrale) UnicastRemoteObject.exportObject(httpServeur, 0);

        // Enregistrement dans le registre RMI
        Registry registry = LocateRegistry.getRegistry(ip, rmiPort);
        registry.rebind("Centrale", serviceCentrale);
        System.out.println("Service centrale enregistré dans RMI");

        // Charger le keystore
        char[] password = "motdepasse".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("keystore.jks"), password);

        // Initialiser KeyManagerFactory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);

        // Créer SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        // Créer le serveur HTTPS
        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(8000), 0);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters params) {
                try {
                    SSLContext context = getSSLContext();
                    SSLEngine engine = context.createSSLEngine();
                    params.setNeedClientAuth(false);
                    params.setCipherSuites(engine.getEnabledCipherSuites());
                    params.setProtocols(engine.getEnabledProtocols());
                    params.setSSLParameters(context.getDefaultSSLParameters());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        HttpServerHandlerBD httpServeurHandler = new HttpServerHandlerBD(httpServeur);
        httpsServer.createContext("/BD", httpServeurHandler);
        HttpServerHandlerEvent httpServeurHandlerEvent = new HttpServerHandlerEvent(httpServeur);
        httpsServer.createContext("/evenement", httpServeurHandlerEvent);
        httpsServer.setExecutor(null);
        httpsServer.start();
        System.out.println("Serveur HTTP lancé sur le port 8000");
    }
}