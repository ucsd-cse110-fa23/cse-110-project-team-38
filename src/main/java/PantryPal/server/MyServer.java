package server;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class MyServer {
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";

  public static void main(String[] args) throws IOException {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10); //10 thread executor

    Map<String, String> data = new HashMap<>();

    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME,SERVER_PORT),0);

    HttpContext context = server.createContext("/", new RequestHandler(data));

    
    HttpContext otherContext = server.createContext("/name",new MyHandler());

    server.setExecutor(threadPoolExecutor);
    server.start();

    System.out.println("Server Started on port " + SERVER_PORT);
  }
}