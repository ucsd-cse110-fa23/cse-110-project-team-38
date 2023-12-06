import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginValidationTest {

    @Test
    void testValidUser() {
        LoginValidation loginValidation = new LoginValidation();
        assertTrue(loginValidation.validateUser("admin", "12345"));
    }

    @Test
    void testInvalidUser() {
        LoginValidation loginValidation = new LoginValidation();
        assertFalse(loginValidation.validateUser("invalidUser", "wrongPass"));
    }
}
