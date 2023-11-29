import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MockMongoCollection {
    private MongoCollection<Document> usersCollection;

    public MockMongoCollection(MongoCollection<Document> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public boolean validateUser(String username, String password) {
        Document foundUser = usersCollection.find(and(eq("username", username), eq("password", password))).first();
        return foundUser != null;
    }
}

