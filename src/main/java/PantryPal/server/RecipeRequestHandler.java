package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.DatabaseConnect;
import PantryPal.client.RecipeEncryptor;
import PantryPal.client.RecipeItem;
import PantryPal.client.Whisper;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONObject;

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
            System.out.println("RecipeRequest Handler got an Exception:");
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
     * all GET requests should be for loading recipes from the DB
     */
    private String handleGet(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String specificQuery = query.substring(query.indexOf("=") + 1);
            if (specificQuery != null) {
                // do we really need to even be checking for the query?
            }
        }

        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        // username is in the constructor for RecipeList in main, so look there
        FindIterable<Document> recipes = recipesCollection.find(eq("username", username));
        JSONObject json = new JSONObject();

        // TODO: add the recipe info to the json
        for (Document recipeDoc : recipes) {
            RecipeItem recipe = new RecipeItem();
            recipe.setRecipeTitle(recipeDoc.getString("title"));
            recipe.setRecipeDescription(recipeDoc.getString("description"));
            recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
            // TODO
        }

        return json.toString();
    }

    /*
     * METHOD TO RETURN FROM VERSION SAVED ON SERVER!!!!!
     * IS UNUSED 
     * 
     * Recipes sent in form
     * {[12,43,65]+[51,33,75]}\n
     * {R2 info}\n
     * {R3 info}
     * 
     * for every recipedata, package its export() into {}
     * put it into the response
     */
    private String loadRecipes() {
        StringBuilder sb = new StringBuilder();
        for (RecipeData r : recipeList) {
            String frame = "{";
            frame += r.export();
            frame += "}";
            sb.append(frame);
        }
        return sb.toString();
    }

    /*
     * Assuming all POSTs are save requests given ONE recipe in json form
     */
    private String handlePost(HttpExchange httpExchange) {
        String response = "got save POST";
        JSONObject json = new JSONObject(httpExchange.getRequestBody());
        // TODO: extract recipe from json

        // Username is from RecipeList in Main... see there for info i guess
        // TODO: send to db
        Document recipeDoc = new Document("username", username)
                .append("title", recipe.getFullRecipeTitle())
                .append("description", recipe.getFullRecipeDescription());

        if (recipe.getRecipeId() == null || recipe.getRecipeId().isEmpty() || recipe.isGenerated()) {
            // Insert new recipe only if it's generated and not yet saved
            recipesCollection.insertOne(recipeDoc);
            recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
            recipe.setGenerated(false); // Reset the generated flag
        } else {
            // Update existing recipe
            ObjectId id = new ObjectId(recipe.getRecipeId());
            Bson filter = Filters.eq("_id", id);
            recipesCollection.updateOne(filter, new Document("$set", recipeDoc));
        }

        return response;
    }

    /*
     * PUT MUST BE IN FORM
     * "[1,2,3]/[4,5,6]"
     */
    private String handlePut(HttpExchange httpExchange) throws IOException {
        String response = "got PUT";

        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";

        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String encryptedTitle = query.substring(query.indexOf("=") + 1);
            // do we really need anything here??
        }
        //TODO: I dont actually know if you can send a request body in a DELETE or GET, so this method may not work
        JSONObject json = new JSONObject(httpExchange.getRequestBody());

        //TODO: encorporate the code!
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("title", recipeItem.getFullRecipeTitle()),
                Filters.eq("description", recipeItem.getFullRecipeDescription()));
        recipesCollection.deleteOne(filter);

        return response;

    }
}