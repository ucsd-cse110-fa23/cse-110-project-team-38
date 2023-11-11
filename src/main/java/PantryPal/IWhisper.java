package PantryPal;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public interface IWhisper { 
    public String sendRequest() throws IOException, URISyntaxException;
}
