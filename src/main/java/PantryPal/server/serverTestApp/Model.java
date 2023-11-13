package PantryPal.server.serverTestApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import PantryPal.client.RecipeEncryptor;

import java.net.URI;


public class Model {
    public String performRequest(String method, String title, String description, String query) {
        // Implement your HTTP request logic here and return the response

        //encrypt all fields before sending requests
        title = RecipeEncryptor.encryptSingle(title);
        description = RecipeEncryptor.encryptSingle(description);
        try {
            String urlString = "http://localhost:8100/";
            if (query != null) {
                urlString += "?=" + query;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            if (method.equals("POST") || method.equals("PUT")) {
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write(title + "/" + description);
                out.flush();
                out.close();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }
}