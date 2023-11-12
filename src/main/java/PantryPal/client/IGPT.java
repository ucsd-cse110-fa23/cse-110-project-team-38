package PantryPal.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface IGPT {
    public String processRequest(String prompt) throws IOException, InterruptedException, URISyntaxException;
}
