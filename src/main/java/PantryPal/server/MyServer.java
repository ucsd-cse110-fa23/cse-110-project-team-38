package PantryPal.server;

import com.sun.net.httpserver.*;

import PantryPal.client.RecipeItem;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


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

    HttpContext context = server.createContext("/", new RecipeRequestHandler(recipes,recipeList));
    HttpContext recipeContext = server.createContext("/api",new APIRequestHandler());
    
    
    server.setExecutor(threadPoolExecutor);
    server.start();

    System.out.println("Server Started on port " + SERVER_PORT);
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