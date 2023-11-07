package PantryPal;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
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
import javafx.scene.control.TextArea;
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

class RecipeDetailsPage extends VBox {
    private TextField titleField;
    private TextArea descriptionField;
    private Button doneButton;
    private Button backButton;
    private RecipeItem currentRecipeItem;
    private AppFrame appFrame;
    private Label titleLabel;
    private Label descriptionLabel;
    private boolean generated = false;

    public RecipeDetailsPage(AppFrame appFrame) {
        this.appFrame = appFrame;
        this.setSpacing(10);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        // Create the title label and text field
        titleLabel = new Label("Title");
        styleLabels(titleLabel);
        titleField = new TextField();
        titleField.setPromptText("Title");
        styleTextField(titleField);

        // Create the description label and text field
        descriptionLabel = new Label("Description");
        styleLabels(descriptionLabel);
        descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        styleTextField(descriptionField);

        // Create the back button
        backButton = new Button("<-");
        backButton.setPrefSize(50, 30);
        backButton.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(appFrame);
        });
        styleBackButton(backButton);

        // Create the done button
        doneButton = new Button("Save Recipe");
        saveButton(doneButton);

        // Add the components to the VBox in the correct order
        this.getChildren().addAll(backButton, titleLabel, titleField, descriptionLabel, descriptionField, doneButton);
    }


    // Overloaded constructor for showing the detail page after clicking on "edit"
    public RecipeDetailsPage(AppFrame appFrame, RecipeItem recipeItem) {
        this(appFrame); 
        currentRecipeItem = recipeItem;
        if (currentRecipeItem != null) {
            titleField.setText(currentRecipeItem.getFullRecipeTitle());
            descriptionField.setText(currentRecipeItem.getFullRecipeDescription());
        
        }
    }

    public RecipeDetailsPage(AppFrame appFrame, RecipeItem recipeItem, boolean generated) {
        this(appFrame);
        this.generated = generated;
        currentRecipeItem = recipeItem;
        if (currentRecipeItem != null) {
            titleField.setText(currentRecipeItem.getFullRecipeTitle());
            descriptionField.setText(currentRecipeItem.getFullRecipeDescription());
        }
    }

    private void styleTextField(TextField textField) {
        textField.setPrefHeight(40);
        textField.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
    }

    private void styleTextField(TextInputControl control) {
    control.setPrefHeight(40);
    control.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
        if (control instanceof TextArea) {
            ((TextArea) control).setPrefHeight(200); // Set a fixed height for the TextArea
        }
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

        button.setOnAction(e -> {
            if (titleField.getText().trim().isEmpty() ||
            descriptionField.getText().trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Incomplete Recipe Details");
            alert.setContentText("Please make sure there are no empty fields!");
            alert.showAndWait();
            return;
            }

            // String phoneNumber = phoneField.getText().trim();
            // if (!phoneNumber.isEmpty() && !phoneNumber.matches("\\d+")) {
            //     Alert alert = new Alert(AlertType.WARNING);
            //     alert.setTitle("Warning");
            //     alert.setHeaderText("Invalid Phone Number");
            //     alert.setContentText("Phone Number must be numeric!");
            //     alert.showAndWait();
            //     return;
            // }

            if (currentRecipeItem == null || generated) {
                RecipeItem recipeItem = new RecipeItem();
                recipeItem.setRecipeTitle(titleField.getText());
                recipeItem.setRecipeDescription(descriptionField.getText());

                appFrame.getRecipeList().getChildren().add(recipeItem);
            } else {
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
            }

            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(appFrame, 500, 600));
        });
    }
}