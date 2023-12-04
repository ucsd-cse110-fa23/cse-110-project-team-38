package PantryPal.server;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockDallE implements IDallE {
    
    public String processRequest(String prompt) throws IOException, InterruptedException, URISyntaxException {
        return "image.jpg";
    }
}
