package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.DatabaseConnect;
import PantryPal.client.RecipeItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MyServer {
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";

  
  public static void main(String[] args) throws IOException {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10); //10 thread executor

    //recipes stored as [1,2,3],[1,2,3] string keypair values
    Map<String, String> recipes = new HashMap<>();
    ArrayList<RecipeData> recipeList = new ArrayList();

    //addMocks(recipeList);

    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME,SERVER_PORT),0);

    // HttpContext context = server.createContext("/", new RecipeRequestHandler(recipes,recipeList));
    // HttpContext recipeContext = server.createContext("/api",new RecipeRequestHandler(recipes, recipeList));
    
    
    server.setExecutor(threadPoolExecutor);
    server.start();
    MongoDatabase database = DatabaseConnect.getDatabase();
    HttpContext loginContext = server.createContext("/login", new LoginHandler(database));

    System.out.println("Server Started on port " + SERVER_PORT);
  }

  static class LoginHandler implements HttpHandler {
      private MongoDatabase database;

      LoginHandler(MongoDatabase database) {
          this.database = database;
      }
      
      @Override
      public void handle(HttpExchange exchange) throws IOException {
          if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
              // Extract the request body
              InputStream requestBody = exchange.getRequestBody();
              String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

              // Here, parse the body to get username and password
              // For simplicity, assuming the body contains "username=xxx&password=yyy"
              String[] params = body.split("&");
              String username = params[0].split("=")[1];
              String password = params[1].split("=")[1];

              // Validate credentials (pseudo code, replace with actual validation)
              boolean isValidUser = validateUser(username, password);

              // Send response
              String response = isValidUser ? "Success" : "Failure";
              exchange.sendResponseHeaders(isValidUser ? 200 : 401, response.length());
              OutputStream os = exchange.getResponseBody();
              os.write(response.getBytes());
              os.close();
          }
      }

      private boolean validateUser(String username, String password) {
        MongoCollection<Document> usersCollection = database.getCollection("users");
        Document foundUser = usersCollection.find(and(eq("username", username), eq("password", password))).first();
        return foundUser != null;
    }

  }

    // public static void addMocks(ArrayList<RecipeData> list) {
    // RecipeData mock1 = new RecipeData();
    // mock1.setTitle("Bacon and Eggs");
    // mock1.setDescription("You take the moon and you take the sun");

    // RecipeData mock2 = new RecipeData();
    // mock2.setTitle("Shrimp Fried Rice");
    // mock2.setDescription("So you're telling me a SHRIMP fried this rice!?");

    // RecipeData mock3 = new RecipeData();
    // mock3.setTitle("Nothing burger");
    // mock3.setDescription("Absolutely nothing");
    
    // list.add(mock1);
    // list.add(mock2);
    // list.add(mock3);
    // System.out.println("Mocks Added!");
    // }
}