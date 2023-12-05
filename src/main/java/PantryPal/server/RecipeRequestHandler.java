package PantryPal.server;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sun.net.httpserver.*;

import PantryPal.client.DatabaseConnect;
import PantryPal.client.RecipeEncryptor;
import PantryPal.client.RecipeItem;
import PantryPal.client.Whisper;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;

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
        //System.out.println(httpExchange.getRequestBody().toString());
        System.out.println(method);
        //System.out.println(httpExchange.getRequestURI().toASCIIString());
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
        //System.out.println(json.toString());
        String query = uri.getRawQuery();
        System.out.println(query);
        String username = query.split("=")[2];
        System.out.println("username: " + username);
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        // username is in the constructor for RecipeList in main, so look there
        List<Document> recipes = recipesCollection.find(eq("username", username)).into(new ArrayList<>());
        if (query != null) {
            String specificQuery = query.split("/")[0];
            System.out.println(specificQuery);
            if (specificQuery != null) {
                // do we really need to even be checking for the query?
                if (specificQuery.equals("=ALL")) {
                    return loadRecipes(recipes).toString();
                }
                else {
                    /*for (Document recipe : recipes) {
                        JSONObject jsonObject = new JSONObject(recipe.toJson());
                        if (jsonObject.getString("title").equals(specificQuery)) {
                            return jsonObject.toString();
                        }
                    }*/
                    try {
                        DallE dalle = new DallE();
                        String imageURL = dalle.processRequest(specificQuery);
                        JSONObject returnURL = new JSONObject();
                        returnURL.put("imageURL", imageURL);
                        return returnURL.toString();
                    }
                    catch (Exception err) {
                        System.out.println("Failed to generate image");
                    }
                }
            }
        }

        return "Does not exist";
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
    private JSONArray loadRecipes(List<Document> recipes) {
        // TODO: add the recipe info to the json
        JSONArray jsonArray = new JSONArray();
        for (Document recipeDoc : recipes) {
            JSONObject jsonObject = new JSONObject(recipeDoc.toJson());
            jsonArray.put(jsonObject);
            // TODO
            
        }

        return jsonArray;
    }

    /*
     * Assuming all POSTs are save requests given ONE recipe in json form
     */
    private String handlePost(HttpExchange httpExchange) {
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        String response = "got save POST";
        
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            for (int length; (length = httpExchange.getRequestBody().read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
        }
        catch (Exception err) {
            System.out.println("Error in getting request body");
        }
        String jsonBody = result.toString();
        JSONObject json = new JSONObject(jsonBody);
        System.out.println(json.toString());
        String username = json.getString("username");
        // TODO: extract recipe from json
        //RecipeItem recipe = new RecipeItem();
        //recipe.setRecipeDescription(json.getString("description"));
        //recipe.setGenerated(json.getBoolean("isGenerated"));
        //recipe.setRecipeTitle(json.getString("title"));
        //recipe.setRecipeId(json.getString("id"));
        
        // Username is from RecipeList in Main... see there for info i guess
        // TODO: send to db
        Document recipeDoc = new Document("username", username)
                .append("title", json.getString("title"))
                .append("description", json.getString("description"))
                .append("mealType", json.getString("mealType"));
        
        

        if (!json.getBoolean("isGenerated")) {
            // Insert new recipe only if it's generated and not yet saved
            recipeDoc.append("isGenerated", true);
            recipesCollection.insertOne(recipeDoc);
            //recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
            //recipe.setGenerated(false); // Reset the generated flag
        } else {
            // Update existing recipe
            //ObjectId id = new ObjectId(json.getString("id"));
            Bson filter = Filters.and(eq("title", json.getString("title")), 
                                     eq("username", json.getString("username")));
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

        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            for (int length; (length = httpExchange.getRequestBody().read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }
        }
        catch (Exception err) {
            System.out.println("Error in getting request body");
        }
        String jsonBody = result.toString();
        JSONObject json = new JSONObject(jsonBody);

        Document recipeDoc = new Document("username", json.getString("username"))
                .append("title", json.getString("title"))
                .append("description", json.getString("description"));

        Bson filter = Filters.eq("title", json.getString("title"));
        Bson updateOperation = set("description", json.getString("description"));
        recipesCollection.updateOne(filter, updateOperation);

        return response;
    }

    private String handleDelete(HttpExchange httpExchange) throws IOException {
        String response = "Invalid DELETE request";
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        // username is in the constructor for RecipeList in main, so look there

        URI uri = httpExchange.getRequestURI();
        //System.out.println(json.toString());
        String query = uri.getRawQuery();
        System.out.println(query);
        String username = query.split("=")[2];
        System.out.println("username: " + username);
        if (query != null) {
            String specificQuery = query.split("/")[0];
            specificQuery = specificQuery.replace("=", "");
            // username is in the constructor for RecipeList in main, so look there
            List<Document> recipes = recipesCollection.find(eq("username", username)).into(new ArrayList<>());
            Bson filter = Filters.and(
                    Filters.eq("username", username),
                    Filters.eq("title", specificQuery));
            recipesCollection.deleteOne(filter);
        }
        else {
            return "Recipe not in database";
        }
        return response;

    }
}