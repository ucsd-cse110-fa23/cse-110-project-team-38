package PantryPal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.sun.net.httpserver.*;

public class APIRequestHandler implements HttpHandler{
    
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.println(httpExchange.getRequestURI().toASCIIString());
        try {
            switch (method) {
                case "GET":
                    response = "cannot handle GET";

                    break;
                case "POST":
                    response = handlePost(httpExchange);

                    break;
                case "PUT":
                    response = "cannot handle PUT";

                    break;
                case "DELETE":
                    response = "cannot handle DELETE";

                    break;

                default:
                    throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

    }


    private String handlePost(HttpExchange httpExchange) throws IOException, URISyntaxException, InterruptedException {
        String response = "got API post";
        String mealType = "";
        
        // forward our request to whisper
        String whisperResponse = forwardToWhisper(httpExchange);
        if (whisperResponse.contains("Breakfast") || whisperResponse.contains("breakfast")) {
            mealType = "Breakfast";
        }
        else if (whisperResponse.contains("Lunch") || whisperResponse.contains("lunch")) {
            mealType = "Lunch";
        }
        else {
            mealType = "Dinner";
        }
        System.out.println("Whisper Response: " + whisperResponse);
        //send to GPT
        String gptResponse = sendToGPT(whisperResponse);
        System.out.println("GPT response: " + gptResponse);
        response = StringPacker.encrypt(mealType + "\n" + gptResponse);
        return response;
    }

    private String forwardToWhisper(HttpExchange httpExchange) throws URISyntaxException, IOException {
        final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
        final String TOKEN = "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";
        final String MODEL = "whisper-1";

        System.out.println("Opening connection to Whisper");

        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Reuse headers from the original client request
        for (Map.Entry<String, List<String>> header : httpExchange.getRequestHeaders().entrySet()) {
            String headerKey = header.getKey();
            List<String> headerValues = header.getValue();
            for (String value : headerValues) {
                connection.addRequestProperty(headerKey, value);
            }
        }

        // forward our body to WhisperAPI
        httpExchange.getRequestBody().transferTo(connection.getOutputStream());

        // read response from API
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = in.readLine();
        String itr = in.readLine();
        while (itr != null) {
            response += itr;
            itr = in.readLine();
        }
        JSONObject obj = new JSONObject(response);
        response = obj.getString("text");
        in.close();
        return response;
    }

    public String sendToGPT(String prompt) throws IOException, InterruptedException, URISyntaxException {
        System.out.println("SEND TO GPT prompt: " + prompt);
        String response = "";
        // //chatGPT call used to get back chatGPT output
        ChatGPT chatGPT = new ChatGPT();
        System.out.println("Processing request...");
        response = chatGPT.processRequest(prompt + " generate a recipe");
        return response;
    }
}
