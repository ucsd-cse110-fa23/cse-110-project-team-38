package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPT implements IGPT {
    public String processRequest(String prompt) throws IOException, InterruptedException, URISyntaxException {
        return "Title\nIngredients:...\nInstructions:...";
    }
}
