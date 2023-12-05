public class LoginValidation {

    public boolean validateUser(String username, String password) {
        // Here, in a real scenario, you would interact with the database.
        // For testing, we'll just check if the credentials are what we expect.
        return "admin".equals(username) && "12345".equals(password);
    }
}
