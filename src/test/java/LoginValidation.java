import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class LoginValidation {

    private MongoDatabase database;

    public LoginValidation(MongoDatabase database) {
        this.database = database;
    }

    public boolean validateUser(String username, String password) {
        MongoCollection<Document> usersCollection = database.getCollection("users");
        Document foundUser = usersCollection.find(and(eq("username", username), eq("password", password))).first();
        return foundUser != null;
    }
}
