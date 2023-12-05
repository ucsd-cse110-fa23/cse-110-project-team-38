package PantryPal.server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.util.*;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.w3c.dom.html.HTMLDListElement;

/*
 * 
 */
class ShareHandler implements HttpHandler {
    private String username;
    private String title;
    private String desc;
    private HttpServer server;
    private String path;
    private HTMLDocument pageHTML;
    private Map<String,HttpContext> contextMap;
    private String id;

    RecShareHandler(Map<String,HttpContext> contextMap, HttpServer server){
        this.contextMap = contextMap;
      this.server = server;
    }

    @Override

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.println(method);
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
     * gets should return the recipe page html
     */
    public String handleGet(HttpExchange httpExchange) {
        return pageHTML.toString();
    }


    public String handlePost(HttpExchange httpExchange){
        // get and extract the request body
        InputStream requestBody = httpExchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        // parse the body to get username and password
        String[] params = body.split("&");
        this.username = params[0].split("=")[1];
        this.title = params[1].split("=")[1];
        this.desc = params[2].split("=")[1];
        this.id = params[3].split("=")[1];

        // Create a specific page
        String name = "http://localhost:8100" + "/recipe/" + this.username + "/"
                + this.id;
        System.out.println("server: " + name);

        // Create a new context {id, context}
        contextMap.put(this.id,this.server.createContext(name, new ShareRecipeHandler(this.title, this.desc)));

        // POST request to server
        // HttpRequest request = HttpRequest.newBuilder()
        //         .uri(URI.create(name))
        //         .header("Content-Type", "application/x-www-form-urlencoded")
        //         .GET()
        //         .build();

        /*
         * TODO: Create the HTML for this page right here!!!!
         */
        this.pageHTML = new HTMLDocument();
        return name;
    }

    public String handlePut(HttpExchange httpExchange){
        return "IDK what to do with this...";
    }
    public String handleDelete(HttpExchange httpExchange){
        server.removeContext(path);
        return "removed!";//TODO fix this.... its stupid and untested and weird
    }

}