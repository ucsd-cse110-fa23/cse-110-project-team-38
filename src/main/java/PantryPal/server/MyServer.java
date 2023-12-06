package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.RecipeItem;
import PantryPal.client.DatabaseConnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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

    addMocks(recipeList);

    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME,SERVER_PORT),0);

    //handles recipes at path '/recipe'
    HttpContext context = server.createContext("/recipe", new RecipeRequestHandler(recipes,recipeList));
    //handles api calls at path '/api'
    HttpContext recipeContext = server.createContext("/api",new APIRequestHandler());
    //handles sharing at path 'share'
    Map<String,HttpContext> contextMap = new HashMap<>();
    HttpContext shareContext = server.createContext("/share",new ShareHandler(contextMap, server));
    
    server.setExecutor(threadPoolExecutor);
    server.start();

    MongoDatabase database = DatabaseConnect.getDatabase();
    HttpContext loginContext = server.createContext("/login", new LoginHandler(database));

    HttpContext autoLoginContext = server.createContext("/autologin", new AutoLoginHandler(database));
    
    System.out.println("Server Started on port " + SERVER_PORT);
  }

  static boolean validateUser(MongoDatabase database, String username, String password) {
    MongoCollection<Document> usersCollection = database.getCollection("users");
    Document foundUser = usersCollection.find(and(eq("username", username), eq("password", password))).first();
    return foundUser != null;
  }

  static class LoginHandler implements HttpHandler {
    private MongoDatabase database;

    LoginHandler(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            //get and extract the request body
            InputStream requestBody = exchange.getRequestBody();
            String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            //parse the body to get username and password
            String[] params = body.split("&");
            String username = params[0].split("=")[1];
            String password = params[1].split("=")[1];

            //validate credentials
            boolean isValidUser = MyServer.validateUser(database, username, password);

            //send response back to client
            String response = isValidUser ? "Success" : "Failure";
            exchange.sendResponseHeaders(isValidUser ? 200 : 401, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

  }

  static class AutoLoginHandler implements HttpHandler {
    private MongoDatabase database;

    AutoLoginHandler(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            InputStream requestBody = exchange.getRequestBody();
            String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            String[] params = body.split("&");
            String username = params[0].split("=")[1];
            String password = params[1].split("=")[1];

            boolean isValidUser = MyServer.validateUser(database, username, password);

            String response = isValidUser ? "Success" : "Failure";
            exchange.sendResponseHeaders(isValidUser ? 200 : 401, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
  }

  public static void addMocks(ArrayList<RecipeData> list) {
    RecipeData mock1 = new RecipeData();
    mock1.setTitle("Bacon and Eggs");
    mock1.setDescription("You take the moon and you take the sun");

    RecipeData mock2 = new RecipeData();
    mock2.setTitle("Shrimp Fried Rice");
    mock2.setDescription("So you're telling me a SHRIMP fried this rice!?");

    RecipeData mock3 = new RecipeData();
    mock3.setTitle("Nothing burger");
    mock3.setDescription("Absolutely nothing");
    
    list.add(mock1);
    list.add(mock2);
    list.add(mock3);
    System.out.println("Mocks Added!");
}
}