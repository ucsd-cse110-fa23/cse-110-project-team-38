package PantryPal.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import java.net.URI;

public class RequestSender {
    public String performRequest(String method,String path, JSONObject json, String query, String username) {
        // Implement your HTTP request logic here and return the response

        try {
            String urlString = "http://localhost:8100/";
            //used to add "api" or something if necessary
            if(path != null) {urlString += path;}

            if (query != null) {
                urlString += "title/?=" + query;
                urlString += "/username/?=" + username;
            }
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod(method);
            conn.setDoOutput(true);

            System.out.println(method);

            // writes the json to the request body
            if (method.equals("POST") || method.equals("PUT")) {
                OutputStream out = conn.getOutputStream();
                System.out.println(json.toString());
                out.write(json.toString().getBytes());
                out.flush();
                out.close();
            }


            //reads the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error: " + ex.getMessage();
        }
    }

    public static void writeFileToOutputStream(
            OutputStream outputStream,
            File file,
            String boundary) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                ("Content-Disposition: form-data; name=\"file\"; filename=\"" +
                        file.getName() +
                        "\"\r\n").getBytes());
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }
}