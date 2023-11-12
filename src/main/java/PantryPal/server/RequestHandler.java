package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.RecipeItem;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {

    private final Map<String, String> recipes;

    public RequestHandler(Map<String, String> recipes) {
        this.recipes = recipes;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            switch (method) {
                case "GET":
                    response = handleGet(httpExchange);
                    
                    break;
                case "POST":
                    response = handlePost(httpExchange);

                    break;
                case "PUT":
                    response = handlePut(httpExchange);

                    break;
                case "DELETE":
                    response = handleDelete(httpExchange);

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

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            String year = recipes.get(value); // Retrieve recipes from hashmap
            if (year != null) {
                response = year;
                System.out.println("Queried for " + value + " and found " + year);
            } else {
                response = "No recipes found for " + value;
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
                postData.indexOf(",")), year = postData.substring(postData.indexOf(",") + 1); // TWO DECLARATIONS IN ONE
                                                                                              // LINE USING ','

        // Store recipes in hashmap
        recipes.put(language, year);

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
        String language = putData.substring(0, putData.indexOf(",")); // split the declaration from POST
        String year = putData.substring(putData.indexOf(",") + 1);

        if (recipes.containsKey(language)) {
            String prevYear = recipes.get(language);
            recipes.put(language, year);
            return "Updated entry {" + language + ", " + year + "} (previous year:" + prevYear + ")";
        } else {
            recipes.put(language, year);
            return "Added entry {" + language + ", " + year + "}";
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String value = query.substring(query.indexOf("=") + 1);
            if (recipes.containsKey(value)) {
                response = "Deleted entry {" + value + ", " + recipes.get(value) + "}";
                recipes.remove(value);
            } else {
                response = "No recipes found for " + value;
            }
        }
        return response;

        // URI uri = httpExchange.getRequestURI();
        // String deleteQuery = uri.getRawQuery();

        // String response;
        // if(deleteQuery != null && recipes.containsKey(deleteQuery)){
        // response = "Deleted entry {" + deleteQuery + ", " + recipes.get(deleteQuery)
        // + "}";
        // recipes.remove(deleteQuery);
        // } else{
        // response = "No recipes found for " + deleteQuery;
        // }
        // return response;

    }
}