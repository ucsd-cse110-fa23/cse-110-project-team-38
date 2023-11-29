package PantryPal.client;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import PantryPal.server.ChatGPT;
import PantryPal.server.serverTestApp.Model;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class CreateRecipePage extends VBox {
    private ImageView contactImageView;
    private Button generateButton;
    private Button micButton;
    private Button backButton;
    private AppFrame appFrame;
    private Label nameLabel;
    private Label recordingLabel;
    private Label generatingLabel;
    private AudioRecorder recorder = new AudioRecorder();
    private static final String FILE_PATH = "recording.wav"; //fill with certain file path for audio

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
                //start listening for audio
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
                //set recording label to let user know
                recordingLabel.setVisible(false);
                generatingLabel.setVisible(true);
                recorder.stopRecording();
                
                //--------------------------

                Model request = new Model();
                File file = new File(FILE_PATH);

                //should be a chatGPT response
                String response = request.performRequest("POST", null, null);
                
                
                // //whisper API used to get text from audio
                Whisper whisper = new Whisper();
                String whisperRequest = whisper.sendRequest(); //the audio
                // System.out.println("Request sent");
                
                // //chatGPT call used to get back chatGPT output
                // ChatGPT chatGPT = new ChatGPT();
                // String details = chatGPT.processRequest(prompt + " generate a recipe");
                

                // //--------------------------


                // //parse output of ChatGPT
                // String[] parts = details.split("\n");
                // System.out.println(details);
                // RecipeItem newRecipe = new RecipeItem();
                // newRecipe.setRecipeTitle(parts[2]);
                // String detailsWithNoTitle = details.replace(parts[2], "");
                // newRecipe.setRecipeDescription(detailsWithNoTitle.replace("\n\n\n\n", ""));

                // RecipeDetailsPage detailsPage = new RecipeDetailsPage(appFrame, newRecipe, true, true);
                // Stage stage = (Stage) this.getScene().getWindow();
                // stage.getScene().setRoot(detailsPage);
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