package PantryPal.client;

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
        // this.addMocks();
    }
    
    public void addMocks(){
        RecipeItem mock1 = new RecipeItem();
        mock1.setRecipeTitle("Bacon and Eggs");
        mock1.setRecipeDescription("You take the moon and you take the sun");
        
        RecipeItem mock2 = new RecipeItem();
        mock2.setRecipeTitle("Shrimp Fried Rice");
        mock2.setRecipeDescription("So you're telling me a SHRIMP fried this rice!?");
        
        RecipeItem mock3 = new RecipeItem();
        mock3.setRecipeTitle("Nothing burger");
        mock3.setRecipeDescription("Absolutely nothing");

        this.getChildren().addAll(mock1,mock2,mock3);
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
                String[] recipeInfo = RecipeEncryptor.decryptRecipeInfo(line);
                
                recipe.setRecipeTitle(recipeInfo[0]);
                recipe.setRecipeDescription(recipeInfo[1]);

                // add it to the children list
                this.getChildren().add(recipe);
            }
        } catch (Exception e) {
            System.out.println("Error during loadRecipes!!!");
            System.err.println(e.getMessage());
        }
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
            for (Node node : this.getChildren()) {
                if (node instanceof RecipeItem) {
                    RecipeItem recipe = (RecipeItem) node;
                    String encryption = RecipeEncryptor.encryptRecipeInfo(recipe.getFullRecipeTitle(), recipe.getFullRecipeDescription());
                    System.out.println(encryption);

                    writer.println(encryption);
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

        saveToCSVButton = new Button("Save as CSV Files");
        saveToCSVButton.setStyle(
                "-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        saveToCSVButton.setPrefSize(140, 40);

        saveToCSVButton.setOnAction(e -> {
            if (this.getParent() instanceof AppFrame) {
                AppFrame appFrame = (AppFrame) this.getParent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Contacts");
                fileChooser.getExtensionFilters().add(
                        new ExtensionFilter("CSV Files", "*.csv"));
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    appFrame.getRecipeList().exportToCSV(file);
                }
            }
        });

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
