package PantryPal.server;

import java.net.http.HttpRequest;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.text.html.HTMLDocument;

import org.json.JSONObject;

/*
 * 
 */
class ShareHandler implements HttpHandler {
    private static final String StandardCharsets = null;
    private String username;
    private String title;
    private String desc;
    private HttpServer server;
    private String path;
    private Map<String, HttpContext> contextMap;
    private String id;

    public ShareHandler(Map<String, HttpContext> contextMap, HttpServer server) {
        this.contextMap = contextMap;
        this.server = server;
    }

    @Override

    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        System.out.println("ShareHandler got: " + method + ", at" + httpExchange.getRequestURI());
        try {
            switch (method) {// only needs POST, PUT, and DELETE
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
            System.out.println("ShareHandler Handler got an Exception:");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();

    }

    public String handlePost(HttpExchange httpExchange) throws IOException {
        // get and extract the request body
        InputStream requestBody = httpExchange.getRequestBody();
        String body = new String(requestBody.readAllBytes());

        JSONObject json = new JSONObject(body);


        // parse the body to get username and password
        this.username = json.getString("username");
        this.title = json.getString("title");
        this.desc = json.getString("description");
        this.id = json.getString("id");

        // Create a specific page
        this.path = "/sr/" + this.username + "/" + this.id;
        System.out.println("========== share path: " + this.path + " ===========");

        // Create a new context {id, context} where the context has path 'path'
        // 
        contextMap.put(this.id, this.server.createContext(path, new ShareRecipeHandler(this.title, this.desc)));
        System.out.println("========== SUCCESS! share context with path " + path + "==========");

        return path;
    }

    public String handlePut(HttpExchange httpExchange) {
        return "IDK what to do with this...";
    }

    public String handleDelete(HttpExchange httpExchange) {
        server.removeContext(path);
        return "removed!";// TODO fix this.... its stupid and untested and weird
    }

}