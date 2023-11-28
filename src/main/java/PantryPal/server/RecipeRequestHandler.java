package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.RecipeEncryptor;
import PantryPal.client.RecipeItem;

import java.io.*;
import java.net.*;
import java.util.*;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RecipeRequestHandler implements HttpHandler {

    // Recipe Info is encoded as UTF-8 Arrays as key value pairs [1,2,3],[1,2,3]
    // must convert to UTF-8 strings when accessing recipes
    private final Map<String, String> recipes;
    private final ArrayList<RecipeData> recipeList;

    public RecipeRequestHandler(Map<String, String> recipes, ArrayList<RecipeData> recipeList) {
        this.recipeList = recipeList;
        this.recipes = recipes;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.println(httpExchange.getRequestURI().toASCIIString());
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

    /*
     * GET request, given a title, return the value
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {

        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            // ALL REQUESTS ARE IN TERMS OF ENCRYPTED STRINGS
            String specificQuery = query.substring(query.indexOf("=") + 1);
            if (specificQuery != null) {
                if(specificQuery.equals("load")){
                    response = loadRecipes();
                }
            }
        }
        return response;
    }

    /*
     * Recipes sent in form
     * {[12,43,65]+[51,33,75]}\n
     * {R2 info}\n
     * {R3 info}
     * 
     * for every recipedata, package its export() into {} 
     * put it into the response
     * 
     * 
     */
    private String loadRecipes(){
        StringBuilder sb = new StringBuilder();
        for(RecipeData r : recipeList){
            String frame = "{";
            frame += r.export();
            frame += "}";
            sb.append(frame);
        }

        return sb.toString();
    }

    /*
     * POST MUST BE IN FORM:
     * "[1,2,3]/[4,5,6]"
     */
    private String handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();

        String[] splitData = RecipeEncryptor.comboDivider(postData);

        String encryptedTitle = splitData[0];
        String encryptedDescription = splitData[1];

        // Store recipes in hashmap
        recipes.put(encryptedTitle, encryptedDescription);

        String response = "Posted entry {" + RecipeEncryptor.decryptSingle(encryptedTitle) + ", "
                + RecipeEncryptor.decryptSingle(encryptedDescription) + "}";
        System.out.println(response);
        scanner.close();

        return response;
    }

    /*
     * PUT MUST BE IN FORM
     * "[1,2,3]/[4,5,6]"
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        // should update entry OR create
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();

        String[] splitData = RecipeEncryptor.comboDivider(putData);

        String encryptedTitle = splitData[0];
        String encryptedDescription = splitData[1];

        scanner.close();

        if (recipes.containsKey(encryptedTitle)) {
            String prevDescription = RecipeEncryptor.decryptSingle(recipes.get(encryptedTitle));
            recipes.put(encryptedTitle, encryptedDescription);

            return "Updated entry {" + RecipeEncryptor.decryptSingle(encryptedTitle) +
                    ", " + RecipeEncryptor.decryptSingle(encryptedDescription) +
                    "} (previous encryptedDescription:" + prevDescription + ")";
        } else {
            recipes.put(encryptedTitle, encryptedDescription);
            return "Added entry {" + encryptedTitle + ", " + encryptedDescription + "}";
        }
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String encryptedTitle = query.substring(query.indexOf("=") + 1);
            if (recipes.containsKey(encryptedTitle)) {
                response = "Deleted entry {" + RecipeEncryptor.decryptSingle(encryptedTitle) + ", " +
                        RecipeEncryptor.decryptSingle(recipes.get(encryptedTitle)) + "}";
                recipes.remove(encryptedTitle);
            } else {
                response = "No recipes found for " + RecipeEncryptor.decryptSingle(encryptedTitle);
            }
        }
        return response;

    }
}