package PantryPal.server;

import com.sun.net.httpserver.*;

import java.io.*;


/*
 * creates the HTML for a specific recipe
 */
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
        System.out.println("===============SUCCESS!!! Got a request for ShareRecipeHandler at: " + httpExchange.getRequestURI());

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("-------------Exception handling the request!----------");
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

    public String handleGet(HttpExchange httpExchange) {
        String response = "Invalid GET request";

        //We dont care about the query. We only care about the HTML
        //thus ignore any query on the page
        try {
            // create HTML for the page
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder
                    .append("<html>")
                    .append("<body>")
                    .append("<h1>")
                    .append("Hello ")
                    .append(this.title)
                    .append("</h1>")
                    .append("<h1>")
                    .append("Hello ")
                    .append(this.description)
                    .append("</h1>")
                    .append("</body>")
                    .append("</html>");
            response = htmlBuilder.toString();
            
        } catch (Exception e) {
            System.out.println("Exception while building recipe");
            e.printStackTrace();
        }

        return response;
    }
    
}
