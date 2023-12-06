package PantryPal.client;



import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import javafx.application.Application;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;

class RecipeDetailsPage extends VBox {
    private TextField titleField;
    private TextArea descriptionField;
    private Button doneButton;
    private Button backButton;
    private Button editButton;
    private Button deleteButton;
    private Button regenerateButton;
    private Button shareButton;
    private RecipeItem currentRecipeItem;
    private AppFrame appFrame;
    private boolean generated = false;
    private ImageView imageView = new ImageView();

    public RecipeDetailsPage(AppFrame appFrame, RecipeItem recipeItem, boolean isEditable, boolean generated) throws IOException, InterruptedException, URISyntaxException {
        this.appFrame = appFrame;
        this.currentRecipeItem = recipeItem;
        this.generated = generated;
        this.setSpacing(10);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        Label titleLabel = new Label("What To Cook Today?");
        styleLabels(titleLabel);
        titleField = new TextField(recipeItem.getFullRecipeTitle());
        //titleField.setStyle("-fx-font-weight: bold; -fx-text-fill: darkblue;");
        styleTextInputControl(titleField,false,"Monaco");
        titleField.setEditable(isEditable);

        Label descriptionLabel = new Label("Ingredients & Directions");
        styleLabels(descriptionLabel);
        descriptionField = new TextArea(recipeItem.getFullRecipeDescription());
        styleTextInputControl(descriptionField,true,"Monaco");
        descriptionField.setEditable(isEditable);
        descriptionField.setPrefHeight(200);

        backButton = new Button("<- ");
        styleButton(backButton);
        backButton.setOnAction(e -> goBack());

        doneButton = new Button("Save Changes");
        styleButton(doneButton);
        doneButton.setVisible(isEditable);
        doneButton.setOnAction(e -> {
            regenerateButton.setVisible(false);

            if (titleField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty()) {
                showAlert("Incomplete Recipe Details", "Please make sure there are no empty fields!");
                return;
            }
        
            if (this.generated) {
                if (currentRecipeItem == null) {
                    currentRecipeItem = new RecipeItem();
                }
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
        
                //only add newRecipe to the list if it's a newly generated recipe
                if (!appFrame.getRecipeList().getChildren().contains(currentRecipeItem)) {
                    appFrame.getRecipeList().getChildren().add(0, currentRecipeItem);
                }
                this.generated = false; //reset the flag after saving
                appFrame.getRecipeList().saveRecipes();
            } else {
                //update the existing recipe
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
                JSONObject obj = new JSONObject();
                obj.put("title", currentRecipeItem.getFullRecipeTitle());
                obj.put("description", currentRecipeItem.getFullRecipeDescription());
                obj.put("username", appFrame.getRecipeList().username);


                String response = RequestSender.performRequest("PUT", "recipe", obj, null, appFrame.getRecipeList().username);
                this.generated = false;
                //appFrame.getRecipeList().saveRecipes();
            }
        
            setEditableMode(false);
        });
        

        editButton = new Button("Edit");
        styleButton(editButton);
        editButton.setVisible(!isEditable);
        editButton.setOnAction(e -> setEditableMode(true));

        deleteButton = new Button("Delete");
        styleButton(deleteButton);
        deleteButton.setOnAction(e -> deleteRecipe());

        regenerateButton = new Button("Regenerate");
        styleButton(regenerateButton);
        regenerateButton.setVisible(generated);
        regenerateButton.setOnAction(e -> regenerateRecipe());

        shareButton = new Button("Share");
        styleButton(shareButton);
        shareButton.setOnAction(e -> shareRecipe());

        String imagePath = generateImage();
        Image image = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        this.getChildren().addAll(backButton, titleLabel, titleField, descriptionLabel, descriptionField, imageView, editButton, deleteButton, doneButton, regenerateButton, shareButton);
    }

    private void setEditableMode(boolean editable) {
        titleField.setEditable(editable);
        descriptionField.setEditable(editable);
        doneButton.setVisible(editable);
        editButton.setVisible(!editable);
    }

    private void deleteRecipe() {
        RecipeList parentList = (RecipeList) currentRecipeItem.getParent();
        if (parentList != null) {
            parentList.removeRecipe(currentRecipeItem);
        }
        parentList.saveRecipes();
        goBack();
    }

    private void shareRecipe() {
        RecipeList parentList = (RecipeList) currentRecipeItem.getParent();
        String link = currentRecipeItem.shareRecipe(parentList.username);
        System.out.println("link: " + link);
    }

    private void goBack() {
        Stage stage = (Stage) this.getScene().getWindow();
        stage.getScene().setRoot(appFrame);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void styleLabels(Label label) {
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;-fx-font-family: Impact; -fx-text-fill: " + Constants.PRIMARY_COLOR + ";");
    }
   

    private void styleTextInputControl(TextInputControl control, boolean isTextArea, String fontFamily) {
        control.setPrefHeight(40);  
    
        if (isTextArea) {
            control.setStyle("-fx-font-size: 14px; -fx-font-family: '" + fontFamily + "';-fx-background-color: #F5F5F5; -fx-border-radius: 5; -fx-border-color: #CCCCCC; -fx-padding: 5 10;");
            ((TextArea) control).setPrefHeight(360);  
        } else {
            control.setStyle("-fx-font-size: 18px; -fx-font-style: italic; -fx-font-family: '" + fontFamily + "'; -fx-background-color: #F5F5F5; -fx-border-radius: 5; -fx-border-color: #CCCCCC; -fx-padding: 5 10; -fx-font-weight: bold;");
        }
    }
    

    private void styleButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-font-family: Impact;-fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px;-fx-font-family: Impact; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-font-family: Impact;-fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
    }

    private String generateImage() throws MalformedURLException, IOException, URISyntaxException {
        
        String newFileName = titleField.getText().replaceAll("\\s", "");
        String newPath = "images/" + newFileName + ".jpg";
        String response = RequestSender.performRequest("GET", "recipe", null, titleField.getText().replace(" ", ""), null);
        JSONObject responsePath = new JSONObject(response);
        String imageURL = responsePath.getString("imageURL");
        try(
            InputStream in = new URI(imageURL).toURL().openStream()
        )
        {
            Files.copy(in, Paths.get(newPath));
        }
        catch (Exception err) {
        } 
        return newPath;
    }

    private void regenerateRecipe() {
        //TODO: move logic to server side
        
        /*try {
            //whisper API used to get text from audio
            Whisper whisper = new Whisper();
            String prompt = whisper.sendRequest(); //the audio
            System.out.println("Request sent");
            
            //chatGPT call used to get back chatGPT output
            ChatGPT chatGPT = new ChatGPT();
            String details = chatGPT.processRequest(prompt + " generate a recipe");

            //parse output of ChatGPT
            String[] parts = details.split("\n");
            System.out.println(details);
            titleField.setText(parts[2]);
            String detailsWithNoTitle = details.replace(parts[2], "");
            descriptionField.setText(detailsWithNoTitle.replace("\n\n\n\n", ""));

            String imagePath = generateImage();
            Image image = new Image(new File(imagePath).toURI().toString());
            imageView.setImage(image);
        }
        catch (Exception err) {
            System.out.println("Error regenerating" + err);
        }*/

        try {
        // //whisper API used to get text from audio
        Whisper whisper = new Whisper();
        System.out.println("sending request to server...");
        String response = whisper.sendRequest(); //send request via Whisper, recieve a gpt response
        

        // //--------------------------


        // //parse output of ChatGPT
        String[] parts = response.split("\n");
        System.out.println("GPT response: " + response);
        titleField.setText(parts[2]);
        String detailsWithNoTitle = response.replace(parts[2], "");
        descriptionField.setText(detailsWithNoTitle.replace("\n\n\n\n", ""));

        String imagePath = generateImage();
        Image image = new Image(new File(imagePath).toURI().toString());
        imageView.setImage(image);

    } catch (Exception ex){
        System.out.println("Error Generating!");
        ex.printStackTrace();
    };
    }

}