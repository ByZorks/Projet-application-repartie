import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ClientWeb implements ServiceEvenement {

    @Override
    public String getIncidents() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://carto.g-ny.org/data/cifs/cifs_waze_v2.json"))
                .header("User-Agent", "Java HttpClient")
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
 //                .proxy(ProxySelector.of(new InetSocketAddress("www-cache", 3128)))
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                return response.body();
            } else {
                System.err.println("Erreur HTTP : " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException : " + e.getMessage());// bonne pratique
        } catch (Exception e) {
            System.err.println("Exception inattendue : " + e.getMessage());
        }

        // Retour fallback JSON vide
        return "{\"error\": \"Impossible de récupérer les incidents\"}";
    }
}

