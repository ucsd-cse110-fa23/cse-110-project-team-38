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

public class ShareRecipeHandler implements HttpHandler {
    private String title;
    private String description;

    public ShareRecipeHandler(String title, String description){
        this.title = title;
        this.description = description;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
                System.out.println("ShareRecipe Get");
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
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

    public String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            //String name = query.substring(query.indexOf("=") + 1);
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder
                    .append("<html>")
                    .append("<body>")
                    .append("<h1>")
                    .append("Hello ")
                    .append(this.title)
                    .append("</h1>")
                    .append("</body>")
                    .append("</html>");
            // encode HTML content
            response = htmlBuilder.toString();
        }

        return response;
    }
    
}
