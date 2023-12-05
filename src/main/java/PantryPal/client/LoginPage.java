package PantryPal.client;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.model.Filters;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javafx.application.Platform;
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

        // MongoCollection<Document> usersCollection = DatabaseConnect.getCollection("users");
        // Bson filter = Filters.and(Filters.eq("username", username), Filters.eq("password", password)); // Consider hashing the password
        // long userCount = usersCollection.countDocuments(filter);

        // if (userCount == 1) {
        //     //success login
        //     navigateToMainApp(primaryStage, username);
        // } else {
        //     showAlert("Error", "Invalid username or password");
        // }

        HttpClient client = HttpClient.newHttpClient();

        //request body format
        String formParams = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                            "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        //POST request to server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8100/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formParams))
                .build();

        //send the request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept(response -> {
                  Platform.runLater(() -> {
                      if ("Success".equals(response)) {
                          navigateToMainApp(primaryStage, username);
                      } else {
                          showAlert("Error", "Invalid username or password");
                      }
                  });
              })
              .exceptionally(e -> {
                  return null;
              });
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
    

    private void navigateToMainApp(Stage primaryStage, String username) {
        AppFrame appFrame = new AppFrame(username);
        Scene scene = new Scene(appFrame, 500, 600);
        primaryStage.setScene(scene);
    }
}
