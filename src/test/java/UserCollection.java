import org.bson.conversions.Bson;
import org.bson.Document;

public interface UserCollection {
    Document findUser(Bson filter);
}
