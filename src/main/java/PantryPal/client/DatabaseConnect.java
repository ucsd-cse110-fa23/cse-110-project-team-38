package PantryPal.client;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class DatabaseConnect {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        String uri = "mongodb+srv://stevenx2021:Ss20020829414%2A%2A%2A@cluster0.vmnsedo.mongodb.net/?retryWrites=true&w=majority";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("PantryPalDB");
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    // Optional: Close the client when the application stops
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
