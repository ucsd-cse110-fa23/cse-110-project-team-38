package main.java.PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockWhisper implements IWhisper {
    public String sendRequest() throws IOException, URISyntaxException {
        return "Test Prompt";
    }
}
