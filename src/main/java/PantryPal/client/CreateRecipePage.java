package main.java.PantryPal.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.stream.Collectors;
import java.util.List;
import javafx.scene.layout.Region;
import java.io.PrintWriter;
import java.io.IOException;
import javafx.scene.Node;
import java.util.Collections;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CreateRecipePage extends VBox {
    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private ImageView contactImageView;
    private Image contactImage;
    private Button generateButton;
    private Button micButton;
    private Button backButton;
    private RecipeItem currentContactItem;
    private AppFrame appFrame;
    private Label nameLabel;
    private Label recordingLabel;
    private Label generatingLabel;
    private AudioRecorder recorder = new AudioRecorder();

    //String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public CreateRecipePage(AppFrame appFrame) throws IOException, URISyntaxException{
        this.appFrame = appFrame;
        this.setSpacing(10);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        nameLabel = new Label("Directions:\n-Press mic input button\n-Say meal time (Breakfast, Lunch, or Dinner)\n-Say ingredients\n-Press Generate");
        styleLabels(nameLabel);

        backButton = new Button("<-");
        backButton.setPrefSize(50, 30);
        backButton.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(appFrame);
        });
        styleBackButton(backButton);

        
        micButton = new Button("Voice Input");
        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        recordingLabel.setVisible(false);
        micButton.setOnAction(e -> {
            try {
                recordingLabel.setVisible(true);
                recorder.startRecording();
            }
            catch (Exception err) {
                System.out.println("mic button error");  
            };
        });
        saveButton(micButton);

    

        generatingLabel = new Label("Generating...");
        generatingLabel.setStyle(defaultLabelStyle);
        generatingLabel.setVisible(false);
        generateButton = new Button("Generate Recipe");
        generateButton.setOnAction(e -> {
            try {
                recordingLabel.setVisible(false);
                generatingLabel.setVisible(true);
                recorder.stopRecording();
                Whisper whisper = new Whisper();
                String prompt = whisper.sendRequest();
                //MockWhisper mockWhisper = new MockWhisper();
                //String prompt = mockWhisper.sendRequest();
                System.out.println("Request sent");
                //MockGPT mock = new MockGPT();
                //String details = mock.processRequest(prompt);
                ChatGPT chatGPT = new ChatGPT();
                String details = chatGPT.processRequest(prompt + " generate a recipe");
                String[] parts = details.split("\n");
                System.out.println(details); //uncomment when using API
                RecipeItem newRecipe = new RecipeItem();
                newRecipe.setRecipeTitle(parts[2]);
                String detailsWithNoTitle = details.replace(parts[2], "");
                newRecipe.setRecipeDescription(detailsWithNoTitle.replace("\n\n\n\n", ""));
                //newRecipe.setRecipeTitle(parts[0]);
                //newRecipe.setRecipeDescription(details.replace(parts[0], ""));
                RecipeDetailsPage detailsPage = new RecipeDetailsPage(appFrame, newRecipe, true);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.getScene().setRoot(detailsPage);
                //recorder.stopRecording();
            } catch (Exception ex){
                System.out.println("Error generating");
            };
        });
        saveButton(generateButton);

        contactImageView = new ImageView();
        contactImageView.setFitHeight(150);
        contactImageView.setFitWidth(150);
        contactImageView.setStyle("-fx-border-color: #B0B0B0; -fx-border-radius: 5; -fx-background-color: white;");

        HBox imageBox = new HBox(contactImageView);
        imageBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(backButton, nameLabel, imageBox, micButton, generateButton, recordingLabel, generatingLabel);
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
    }

    private void styleLabels(Label label) {
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Constants.PRIMARY_COLOR + ";");
    }

    private void saveButton(Button button) {
        button.setPrefHeight(40);
        button.setPrefWidth(150);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));

    }
}
