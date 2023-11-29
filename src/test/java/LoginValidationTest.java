import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import PantryPal.client.DatabaseConnect;


public class LoginValidationTest {
    
    private static DatabaseConnect dbConnect;
    private static LoginValidation userValidation;

    @BeforeAll
    static void setUp() {
        dbConnect = new DatabaseConnect();
        userValidation = new LoginValidation(DatabaseConnect.getDatabase());
    }

    @Test
    void testValidateUser() {
        assertTrue(userValidation.validateUser("admin", "12345"));
        assertFalse(userValidation.validateUser("sadas", "5465"));
    }

    @AfterAll
    static void tearDown() {
        DatabaseConnect.close();
    }
}



