package PantryPal.client;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IDallE {
    //will send request to api, will return the created image filepath
    public String processRequest(String prompt) throws IOException, InterruptedException, URISyntaxException;
}
