package PantryPal.client;



import javafx.application.Application;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;

class RecipeDetailsPage extends VBox {
    private TextField titleField;
    private TextArea descriptionField;
    private Button doneButton;
    private Button backButton;
    private Button editButton;
    private Button deleteButton;
    private Button regenerateButton;
    private RecipeItem currentRecipeItem;
    private AppFrame appFrame;
    private boolean generated = false;

    public RecipeDetailsPage(AppFrame appFrame, RecipeItem recipeItem, boolean isEditable, boolean generated) {
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

        backButton = new Button("<- ");
        styleButton(backButton);
        backButton.setOnAction(e -> goBack());

        doneButton = new Button("Save Changes");
        styleButton(doneButton);
        doneButton.setVisible(isEditable);
        doneButton.setOnAction(e -> {
            if (titleField.getText().trim().isEmpty() || descriptionField.getText().trim().isEmpty()) {
                showAlert("Incomplete Recipe Details", "Please make sure there are no empty fields!");
                return;
            }

            if (currentRecipeItem == null || generated) {

                currentRecipeItem = new RecipeItem();
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
                appFrame.getRecipeList().getChildren().add(0, currentRecipeItem);
            } else {
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
                appFrame.getRecipeList().saveRecipes();
            }

            appFrame.getRecipeList().saveRecipes();
            
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

        this.getChildren().addAll(backButton, titleLabel, titleField, descriptionLabel, descriptionField, editButton, deleteButton, doneButton, regenerateButton);
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
   

    // private void styleTextInputControl(TextInputControl control) {
    //     control.setPrefHeight(40);
    //     control.setStyle("-fx-font-size: 25px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
    //     if (control instanceof TextArea) {
    //         ((TextArea) control).setPrefHeight(350);
    //     }
    // }

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

    private void regenerateRecipe() {
        //TODO: move logic to server side
        
        try {
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
        }
        catch (Exception err) {
            System.out.println("Error regenerating");
        }
    }

}