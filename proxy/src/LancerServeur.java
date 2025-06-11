import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LancerServeur {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/BD", new HttpServeur());
        server.setExecutor(null); // Executor par défaut
        server.start();
        System.out.println("Serveur lancé sur http://localhost:8000/BD");
    }
}
