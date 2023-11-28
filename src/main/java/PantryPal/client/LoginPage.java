package PantryPal.client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class LoginPage extends VBox {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button createAccountButton;
    private CheckBox rememberMeCheckbox;
    private AppFrame appFrame;

    public LoginPage(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(15));

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        rememberMeCheckbox = new CheckBox("Remember me");

        loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(primaryStage));

        createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> navigateToCreateAccountPage(primaryStage));

        this.getChildren().addAll(new Label("Login"), usernameField, passwordField, rememberMeCheckbox, loginButton, createAccountButton);
    }

    private void handleLogin(Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty");
            return;
        }

        MongoCollection<Document> usersCollection = DatabaseConnect.getCollection("users");
        Bson filter = Filters.and(Filters.eq("username", username), Filters.eq("password", password)); // Consider hashing the password
        long userCount = usersCollection.countDocuments(filter);

        if (userCount == 1) {
            //success login
            navigateToMainApp(primaryStage);
        } else {
            showAlert("Error", "Invalid username or password");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToCreateAccountPage(Stage primaryStage) {
        CreateAccountPage createAccountPage = new CreateAccountPage(primaryStage);
        Scene scene = new Scene(createAccountPage, 300, 200);
        primaryStage.setScene(scene);
    }
    

    private void navigateToMainApp(Stage primaryStage) {
        appFrame = new AppFrame();
        Scene scene = new Scene(appFrame, 500, 600);
        primaryStage.setScene(scene);
    }
}
