package PantryPal.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/*
 * interface for Whisper, for mock and real whisper
 */
public interface IWhisper { 
    public String sendRequest() throws IOException, URISyntaxException;
}
