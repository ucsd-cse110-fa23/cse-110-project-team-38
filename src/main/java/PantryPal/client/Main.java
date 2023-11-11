package main.java.PantryPal.client;

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
import java.util.Scanner;

import javafx.scene.layout.Region;
import java.io.PrintWriter;
import java.io.IOException;
import javafx.scene.Node;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;

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


class RecipeList extends VBox {
    RecipeList() {
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: white;");
        this.loadRecipes();
    }

    public void removeRecipe(RecipeItem recipeItem) {
        this.getChildren().remove(recipeItem);
    }

    public void loadRecipes() {
        File file = new File("./savedRecipes.csv");
        try {
            Scanner scanner = new Scanner(file);
            System.out.println("loading from: " + file.getPath());
            while (scanner.hasNext()) {
                RecipeItem recipe = new RecipeItem();
                String line = scanner.nextLine();

                //process title
                String rawTitle =line.substring(0, line.indexOf(","));
                String title = decryptByteString(rawTitle);
                
                //process description
                String rawDesc = line.substring(line.indexOf(",") + 1);
                String description = decryptByteString(rawDesc);
                // System.out.println("RESULT:" + description);

                recipe.setRecipeTitle(title);
                recipe.setRecipeDescription(description);

                // add it to the children list
                this.getChildren().add(recipe);
            }
        } catch (Exception e) {
            System.out.println("Could not find '/savedRecipes.csv' in home dir");
            System.err.println(e.getMessage());
        }
    }

    /*
     * Given a string representing bytes, returns the string represented by the
     * bytes
     */
    public String decryptByteString(String rawString) {
        String[] stringArray = rawString.split(" ");
        byte[] byteArray = new byte[stringArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = Byte.parseByte(stringArray[i]);
        }
        return new String(byteArray);
    }

    /**
     * 
     * looks like
     * 12 123 40 94 99, 39 71 61 46 32 55 11 3 3 20 90
     * 134 99 30 293 93 20 10 43, 12 03 57 18 97 36 47 119 101
     */
    public void saveRecipes() {
        File file = new File("savedRecipes.csv");
        try (PrintWriter writer = new PrintWriter(file)) {
            // writer.println("Title,Description");
            for (Node node : this.getChildren()) {
                if (node instanceof RecipeItem) {
                    RecipeItem recipe = (RecipeItem) node;
                    byte[] titleByte = recipe.getFullRecipeTitle().getBytes();
                    byte[] descriptionByte = recipe.getFullRecipeDescription().getBytes();
                    System.out.println(Arrays.toString(descriptionByte));

                    writer.println(Arrays.toString(titleByte)
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", "") + "," + Arrays.toString(descriptionByte)
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", ""));
                }
            }
        } catch (IOException ex) {
            System.err.println("Error writing to CSV: " + ex.getMessage());
        }
    }

    
    public void exportToCSV(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Title,Description");
            for (Node node : this.getChildren()) {
                if (node instanceof RecipeItem) {
                    RecipeItem recipe = (RecipeItem) node;
                    byte[] descriptionByte = recipe.getRecipeDescription().getBytes();
                    writer.println(recipe.getRecipeTitle() + "," + descriptionByte.toString());
                }
            }
        } catch (IOException ex) {
            System.err.println("Error writing to CSV: " + ex.getMessage());
        }
    }
}

/*class RecipeDetailsPage extends VBox {
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
        textField.setStyle(
                "-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: "
                + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR
                + "; -fx-border-width: 1px; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: "
                + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-color: "
                + Constants.BUTTON_HOVER_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: "
                + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: "
                + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
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
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR
                + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: "
                + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: "
                + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));

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
            // Alert alert = new Alert(AlertType.WARNING);
            // alert.setTitle("Warning");
            // alert.setHeaderText("Invalid Phone Number");
            // alert.setContentText("Phone Number must be numeric!");
            // alert.showAndWait();
            // return;
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
}*/

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
        // sortButton.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black;
        // -fx-font-weight: bold; -fx-font-size: 12px;");
        // sortButton.setPrefSize(40, 25);
        // sortButton.setOnAction(e -> {
        // if (this.getParent() instanceof AppFrame) {
        // AppFrame appFrame = (AppFrame) this.getParent();
        // appFrame.getRecipeList().sortContacts();
        // }
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
    private Button saveRecipesButton;

    Footer() {
        this.setPrefSize(500, 40);
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-alignment: center;");


        saveRecipesButton = new Button("Save Recipes");
        saveRecipesButton.setStyle(
                "-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        saveRecipesButton.setPrefSize(140, 40);

        saveRecipesButton.setOnAction(e -> {
            AppFrame appFrame = (AppFrame) this.getParent();
            appFrame.getRecipeList().saveRecipes();
        });

        this.getChildren().addAll(saveRecipesButton, saveToCSVButton);
    }

    public Button getSaveToCSVButton() {
        return saveToCSVButton;
    }

    public Button getSaveRecipesButton() {
        return saveRecipesButton;
    }
}

class AppFrame extends BorderPane {

    private Header header;
    private RecipeList recipeList;
    private Button addButton;
    private Footer footer;

    AppFrame() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, " + Constants.PRIMARY_COLOR + ", "
                + Constants.SECONDARY_COLOR + ");");
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
            CreateRecipePage detailsPage = null;
            try {
                detailsPage = new CreateRecipePage(this);
            } catch (Exception err) {
                System.out.println("Error when create page");
            }
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
