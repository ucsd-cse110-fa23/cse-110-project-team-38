package PantryPal;

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

        Label titleLabel = new Label("Title");
        styleLabels(titleLabel);
        titleField = new TextField(recipeItem.getFullRecipeTitle());
        styleTextInputControl(titleField);
        titleField.setEditable(isEditable);

        Label descriptionLabel = new Label("Description");
        styleLabels(descriptionLabel);
        descriptionField = new TextArea(recipeItem.getFullRecipeDescription());
        styleTextInputControl(descriptionField);
        descriptionField.setEditable(isEditable);

        backButton = new Button("<- Back");
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
                appFrame.getRecipeList().getChildren().add(currentRecipeItem);
                System.out.println("Hello!");
            } else {
                currentRecipeItem.setRecipeTitle(titleField.getText());
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
            }

            goBack();
        });

        editButton = new Button("Edit");
        styleButton(editButton);
        editButton.setVisible(!isEditable);
        editButton.setOnAction(e -> setEditableMode(true));

        deleteButton = new Button("Delete");
        styleButton(deleteButton);
        deleteButton.setOnAction(e -> deleteRecipe());

        this.getChildren().addAll(backButton, titleLabel, titleField, descriptionLabel, descriptionField, editButton, deleteButton, doneButton);
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
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Constants.PRIMARY_COLOR + ";");
    }

    private void styleTextInputControl(TextInputControl control) {
        control.setPrefHeight(40);
        control.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
        if (control instanceof TextArea) {
            ((TextArea) control).setPrefHeight(200);
        }
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
    }

}
