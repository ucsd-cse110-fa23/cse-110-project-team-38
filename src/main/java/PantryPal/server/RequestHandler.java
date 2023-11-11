package main.java.PantryPal.server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import main.java.PantryPal.client.RecipeItem;

public class RequestHandler implements HttpHandler {

    private final Map<String, RecipeItem> recipes;

    public RequestHandler(Map<String, RecipeItem> data) {
        this.recipes = data;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
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

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String year = data.get(value); // Retrieve data from hashmap
            if (year != null) {
                response = year;
                System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String language = postData.substring(
                0,
                postData.indexOf(",")), year = postData.substring(postData.indexOf(",") + 1); //TWO DECLARATIONS IN ONE LINE USING ','

        // Store data in hashmap
        data.put(language, year);

        String response = "Posted entry {" + language + ", " + year + "}";
        System.out.println(response);
        scanner.close();

        return response;
    }

    private String handlePut(HttpExchange httpExchange) throws IOException {
        // should update entry OR create
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        String language = putData.substring(0,putData.indexOf(",")); //split the declaration from POST
        String year = putData.substring(putData.indexOf(",") + 1);
        
        if(data.containsKey(language)){
            String prevYear = data.get(language);
            data.put(language,year);
            return "Updated entry {" + language + ", " + year + "} (previous year:" + prevYear + ")";
        } else{
            data.put(language,year);
            return "Added entry {" + language + ", " + year + "}";
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            if (data.containsKey(value)) {
                response = "Deleted entry {" + value + ", " + data.get(value) + "}";
                data.remove(value);
            } else {
                response = "No data found for " + value;
            }
        }
        return response;

        // URI uri = httpExchange.getRequestURI();
        // String deleteQuery = uri.getRawQuery();

        // String response;
        // if(deleteQuery != null && data.containsKey(deleteQuery)){
        //     response = "Deleted entry {" + deleteQuery + ", " + data.get(deleteQuery) + "}";
        //     data.remove(deleteQuery);
        // } else{
        //     response = "No data found for " + deleteQuery;
        // }
        // return response;


    }
}