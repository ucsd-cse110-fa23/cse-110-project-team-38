import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class LoginValidationTest {
    
    private MongoCollection<Document> usersCollectionMock;
    private MockMongoCollection loginValidation;

    @BeforeEach
    void setUp() {
        // Mock the MongoCollection
        usersCollectionMock = Mockito.mock(MongoCollection.class);

        // Create a fake user document
        Document fakeUser = new Document("username", "admin").append("password", "12345");

        // Mock the behavior of the find method
        when(usersCollectionMock.find(Mockito.any())).thenReturn(new FindIterableImpl<>(Collections.singletonList(fakeUser)));

        // Instantiate LoginValidation with the mocked collection
        loginValidation = new MockMongoCollection(usersCollectionMock);
    }

    @Test
    void testValidUser() {
        assertTrue(loginValidation.validateUser("admin", "12345"));
    }

    @Test
    void testInvalidUser() {
        assertFalse(loginValidation.validateUser("invalidUser", "wrongPass"));
    }
}
