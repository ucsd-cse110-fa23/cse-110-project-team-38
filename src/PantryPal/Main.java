package PantryPal;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;

class Constants {
    public static final String PRIMARY_COLOR = "#2E4053";
    public static final String SECONDARY_COLOR = "#D5D8DC";
    public static final String BUTTON_HOVER_COLOR = "#566573";
    public static final int MAX_TITLE_LENGTH = 15;
    public static final int MAX_DESCRIPTION_LENGTH = 40; 
}

class RecipeItem extends HBox {
    private VBox detailsBox;
    private Label recipeTitleLabel; 
    private Label recipeDescriptionLabel;
    private Button editButton;
    private Button deleteButton;
    private String fullRecipeTitle;
    private String fullRecipeDescription;

    RecipeItem() {
        this.setPrefSize(500, 120); 
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0; -fx-border-color: " + Constants.SECONDARY_COLOR + "; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");


        detailsBox = new VBox(5);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        detailsBox.setPadding(new Insets(0, 0, 0, 10)); 

        recipeTitleLabel = new Label();
        recipeTitleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        recipeTitleLabel.setMaxWidth(250);
        recipeTitleLabel.setEllipsisString("...");
        detailsBox.getChildren().add(recipeTitleLabel);

        recipeDescriptionLabel = new Label();
        recipeDescriptionLabel.setStyle("-fx-font-size: 16px;");
        recipeDescriptionLabel.setWrapText(true);
        detailsBox.getChildren().add(recipeDescriptionLabel);


        this.getChildren().add(detailsBox);
        HBox buttonBox = new HBox(10); 
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        buttonBox.getChildren().add(editButton);

        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        buttonBox.getChildren().add(deleteButton);

        this.getChildren().add(buttonBox);

        deleteButton.setOnAction(e -> {
            if (this.getParent() instanceof RecipeList) {
                RecipeList parentList = (RecipeList) this.getParent();
                parentList.removeRecipe(this);
            }
        });

        editButton.setOnAction(e -> {
            AppFrame appFrame = (AppFrame) this.getScene().getRoot();
            RecipeDetailsPage detailsPage = new RecipeDetailsPage(appFrame, this);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(detailsPage);
        });
    }

    public void setRecipeTitle(String title) {
        this.fullRecipeTitle = title;
        String truncatedTitle = title.length() > Constants.MAX_TITLE_LENGTH ? title.substring(0, Constants.MAX_TITLE_LENGTH) + "..." : title;
        this.recipeTitleLabel.setText(truncatedTitle);
    }

    public String getFullRecipeTitle() {
        return this.fullRecipeTitle;
    }

    public String getRecipeTitle() {
        return this.recipeTitleLabel.getText();
    }

    public void setRecipeDescription(String description) {
        this.fullRecipeDescription = description;
        String truncatedDescription = description.length() > Constants.MAX_DESCRIPTION_LENGTH ? description.substring(0, Constants.MAX_DESCRIPTION_LENGTH) + "..." : description;
        this.recipeDescriptionLabel.setText(truncatedDescription);
    }

    public String getFullRecipeDescription(){
        return this.fullRecipeDescription;
    }

    public String getRecipeDescription(){
        return this.recipeDescriptionLabel.getText();
    }



    public Button getEditButton() {
        return this.editButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }


}


class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5); 
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: white;");
    }

    public void removeRecipe(RecipeItem recipeItem) {
        this.getChildren().remove(recipeItem);
    }

    public void saveContacts() {
        
    }

    // public void sortContacts() {
    //     List<ContactItem> contacts = this.getChildren().stream()
    //         .filter(node -> node instanceof ContactItem)
    //         .map(node -> (ContactItem) node)
    //         .collect(Collectors.toList());

    //     List<String> lowercaseNames = contacts.stream()
    //         .map(contact -> contact.getContactName().toLowerCase())
    //         .collect(Collectors.toList());

    //     Collections.sort(lowercaseNames);

    //     List<ContactItem> sortedContacts = new ArrayList<>();
    //     for (String name : lowercaseNames) {
    //         for (ContactItem contact : contacts) {
    //             if (contact.getContactName().equalsIgnoreCase(name)) {
    //                 sortedContacts.add(contact);
    //                 contacts.remove(contact);
    //                 break;
    //             }
    //         }
    //     }
    //     this.getChildren().setAll(sortedContacts);
    // }

    public void exportToCSV(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Name,Email,Phone");
            for (Node node : this.getChildren()) {
                if (node instanceof RecipeItem) {
                    RecipeItem contact = (RecipeItem) node;
                    writer.println(contact.getRecipeTitle());
                }
            }
        } catch (IOException ex) {
            System.err.println("Error writing to CSV: " + ex.getMessage());
        }
    }
}

class RecipeDetailsPage extends VBox {
    private TextField titleField;
    private TextArea descriptionField;
    private Button doneButton;
    private Button backButton;
    private RecipeItem currentRecipeItem;
    private AppFrame appFrame;
    private Label titleLabel;
    private Label descriptionLabel;

    public RecipeDetailsPage(AppFrame appFrame) {
        this.appFrame = appFrame;
        this.setSpacing(10);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        titleLabel = new Label("Title");
        styleLabels(titleLabel);
        titleField = new TextField();
        titleField.setPromptText("Title");
        styleTextField(titleField);

        descriptionLabel = new Label("Description");
        styleLabels(descriptionLabel);
        descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        styleTextField(descriptionField);
        descriptionField.setPrefRowCount(10); 
        descriptionField.setWrapText(true);

        backButton = new Button("<-");
        backButton.setPrefSize(50, 30);
        backButton.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(appFrame);
        });
        styleBackButton(backButton);

        doneButton = new Button("Save Recipe");
        saveButton(doneButton);

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

    private void styleTextField(TextField textField) {
        textField.setPrefHeight(40);
        textField.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
    }

    private void styleLabels(Label label) {
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Constants.PRIMARY_COLOR + ";");
    }

    private void styleTextField(TextInputControl control) {
    control.setPrefHeight(40);
    control.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
        if (control instanceof TextArea) {
            ((TextArea) control).setPrefHeight(200); // Set a fixed height for the TextArea
        }
    }

    private void saveButton(Button button) {
        button.setPrefHeight(40);
        button.setPrefWidth(150);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));

        button.setOnAction(e -> {
            // if (titleField.getText().trim().isEmpty() && phoneField.getText().trim().isEmpty()) {
            //     Alert alert = new Alert(AlertType.WARNING);
            //     alert.setTitle("Warning");
            //     alert.setHeaderText("Incomplete Contact Details");
            //     alert.setContentText("Please fill in the email address or phone number to save a new contact!");
            //     alert.showAndWait();
            //     return;
            // }

            // String phoneNumber = phoneField.getText().trim();
            // if (!phoneNumber.isEmpty() && !phoneNumber.matches("\\d+")) {
            //     Alert alert = new Alert(AlertType.WARNING);
            //     alert.setTitle("Warning");
            //     alert.setHeaderText("Invalid Phone Number");
            //     alert.setContentText("Phone Number must be numeric!");
            //     alert.showAndWait();
            //     return;
            // }

            if (currentRecipeItem == null) {
                RecipeItem recipeItem = new RecipeItem();
                recipeItem.setRecipeTitle(titleField.getText());
                recipeItem.setRecipeDescription(descriptionField.getText());

                appFrame.getRecipeList().getChildren().add(recipeItem);
            } else {
                currentRecipeItem.setRecipeDescription(descriptionField.getText());
            }

            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(appFrame, 500, 600));
        });
    }
}

class Header extends VBox {

    private Button addButton;
   // private Button sortButton;

    Header() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: white; -fx-alignment: center;");

        HBox upperRow = new HBox();
        upperRow.setAlignment(Pos.CENTER);

        String addButtonStyle = "-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px;";
        addButton = new Button("+");
        addButton.setStyle(addButtonStyle);
        addButton.setPrefSize(30, 30);
        upperRow.getChildren().add(addButton);
        HBox.setMargin(addButton, new Insets(0, 10, 0, 10));

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        upperRow.getChildren().add(leftSpacer);

        Text titleText = new Text("Pantry Pal");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        upperRow.getChildren().add(titleText);

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        upperRow.getChildren().add(rightSpacer);

        // sortButton = new Button("Sort");
        // sortButton.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        // sortButton.setPrefSize(40, 25); 
        // sortButton.setOnAction(e -> {
        //     if (this.getParent() instanceof AppFrame) {
        //         AppFrame appFrame = (AppFrame) this.getParent();
        //         appFrame.getRecipeList().sortContacts();
        //     }
        // });
        // HBox.setMargin(sortButton, new Insets(0, 10, 0, 10));
        // upperRow.getChildren().add(sortButton);

        this.getChildren().addAll(upperRow);
    }

    public Button getAddButton() {
        return addButton;
    }

   
}

class Footer extends HBox {
    private Button saveToCSVButton;

    Footer() {
        this.setPrefSize(500, 40);
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-alignment: center;");

        saveToCSVButton = new Button("Save as CSV Files");
        saveToCSVButton.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        saveToCSVButton.setPrefSize(140, 40);

        saveToCSVButton.setOnAction(e -> {
            if (this.getParent() instanceof AppFrame) {
                AppFrame appFrame = (AppFrame) this.getParent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Contacts");
                fileChooser.getExtensionFilters().add(
                    new ExtensionFilter("CSV Files", "*.csv")
                );
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    appFrame.getRecipeList().exportToCSV(file);
                }
            }
        });
        
        this.getChildren().add(saveToCSVButton);
    }

    public Button getSaveToCSVButton() {
        return saveToCSVButton;
    }
}


class AppFrame extends BorderPane {

    private Header header;
    private RecipeList recipeList;
    private Button addButton;
    private Footer footer;

    AppFrame() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, " + Constants.PRIMARY_COLOR + ", " + Constants.SECONDARY_COLOR + ");");
        header = new Header();
        recipeList = new RecipeList();
        
        this.setTop(header);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        this.setCenter(scroller);

        addButton = header.getAddButton();

        addListeners();

        footer = new Footer();
        this.setBottom(footer);
    }

    public void addListeners() {
        addButton.setOnAction(e -> {
            RecipeDetailsPage detailsPage = new RecipeDetailsPage(this);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(detailsPage);
        });
    }

    public RecipeList getRecipeList() {
        return recipeList;
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        AppFrame root = new AppFrame();
        primaryStage.setTitle("Pantry Pal");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
