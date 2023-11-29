package PantryPal.client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class CreateAccountPage extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button createAccountButton;
    private Button backButton;

    public CreateAccountPage(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(15));

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> handleCreateAccount(primaryStage));

        backButton = new Button("Back");
        backButton.setOnAction(e -> navigateToLoginPage(primaryStage));

        this.getChildren().addAll(new Label("Create Account"), usernameField, passwordField, createAccountButton, backButton);
    }

    private void handleCreateAccount(Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        //basic validation for checking if username or password is valid
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty");
            return;
        }

        //check if user already exists in DB
        MongoCollection<Document> usersCollection = DatabaseConnect.getCollection("users");
        if (usersCollection.countDocuments(new Document("username", username)) > 0) {
            showAlert("Error", "Username already exists");
            return;
        }

        //creates new user
        Document newUser = new Document("username", username)
                               .append("password", password); // Consider encrypting the password
        usersCollection.insertOne(newUser);
        showAlert("Success", "Account created successfully");
        navigateToLoginPage(primaryStage);
    }

    //function to show the alert warning
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToLoginPage(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        primaryStage.setScene(new Scene(loginPage, 300, 200));
    }
}
