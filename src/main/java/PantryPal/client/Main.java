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

import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq;
import org.bson.types.ObjectId;
import org.json.JSONObject;

class Constants {
    public static final String PRIMARY_COLOR = "#2E4053";
    public static final String SECONDARY_COLOR = "#D5D8DC";
    public static final String BUTTON_HOVER_COLOR = "#566573";
    public static final int MAX_TITLE_LENGTH = 15;
    public static final int MAX_DESCRIPTION_LENGTH = 40;
}

class RecipeList extends VBox {
    private String username;
    RecipeList(String username) {
        this.username = username;
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: white;");
        this.loadRecipes();
    }

    // public void removeRecipe(RecipeItem recipeItem) {
    //     //remove from the UI
    //     this.getChildren().remove(recipeItem);

    //     JSONObject json = new JSONObject();
    //     //TODO: pack recipeitem into the json

    //     RequestSender request = new RequestSender();
    //     String response = request.performRequest("DELETE", null, json, recipeID);

    //     //TODO: working code below!!! port to server!!!
    //     //remove from database
    //     MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
    //     Bson filter = Filters.and(
    //             Filters.eq("username", username),
    //             Filters.eq("title", recipeItem.getFullRecipeTitle()),
    //             Filters.eq("description", recipeItem.getFullRecipeDescription())
    //     );
    //     recipesCollection.deleteOne(filter);
    // }


    public void loadRecipes() {
        RequestSender request = new RequestSender();

        String response = request.performRequest("GET", null, null, "ALL");
        //TODO: given a response in json form, unpack and turn into many RecipeItem or however you want to do this
        JSONObject responses = new JSONObject(response);

        ArrayList<RecipeItem> recipeList = new ArrayList<>();
        for(RecipeItem recipe:recipeList){


            this.getChildren().add(recipe);
        }
        


        //TODO: working code below!!! PORT TO SERVER!
        // MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        // FindIterable<Document> recipes = recipesCollection.find(eq("username", username));
        // for (Document recipeDoc : recipes) {
        //     RecipeItem recipe = new RecipeItem();
        //     recipe.setRecipeTitle(recipeDoc.getString("title"));
        //     recipe.setRecipeDescription(recipeDoc.getString("description"));
        //     recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
        //     this.getChildren().add(recipe);
        // }
    }

    public void saveRecipes() {
        RequestSender request = new RequestSender();
        /*
         * either call loop a post request for each client recipe, OR make one big json and send ONE request
         */

        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        for (Node node : this.getChildren()) {
            if (node instanceof RecipeItem) {
                RecipeItem recipe = (RecipeItem) node;
                JSONObject json = new JSONObject();

                //TODO: pack recipe item data we need to save into json

                String response = request.performRequest("POST", null, json, null); //perform a save post given json and no query

                //TODO: working DB save code below! port to server!!!
                // Document recipeDoc = new Document("username", username)
                //                         .append("title", recipe.getFullRecipeTitle())
                //                         .append("description", recipe.getFullRecipeDescription());

                // if (recipe.getRecipeId() == null || recipe.getRecipeId().isEmpty() || recipe.isGenerated()) {
                //     // Insert new recipe only if it's generated and not yet saved
                //     recipesCollection.insertOne(recipeDoc);
                //     recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
                //     recipe.setGenerated(false); // Reset the generated flag
                // } else {
                //     // Update existing recipe
                //     ObjectId id = new ObjectId(recipe.getRecipeId());
                //     Bson filter = Filters.eq("_id", id);
                //     recipesCollection.updateOne(filter, new Document("$set", recipeDoc));
                // }


            }
        }
    }


    public void sortRecipesAlphabetically() {
        List<Node> recipeItems = new ArrayList<>(this.getChildren());

        // Sort the recipe items alphabetically based on the recipe titles
        Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getRecipeTitle()));

        // Clear the existing children and add the sorted recipe items
        this.getChildren().clear();
        this.getChildren().addAll(recipeItems);
    }

    public void sortRecipesChronologically() {
        List<Node> recipeItems = new ArrayList<>(this.getChildren());

        // Sort the recipe items chronologically based on the creation timestamp
        Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getCreationTimestamp()));

        // Clear the existing children and add the sorted recipe items
        this.getChildren().clear();
        this.getChildren().addAll(recipeItems);
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

        HBox upperRowRight = new HBox();
        upperRowRight.setAlignment(Pos.TOP_RIGHT);

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


        //create a dropdown menu
        ComboBox<String> sortByComboBox = new ComboBox<>(FXCollections.observableArrayList("Time", "A to Z"));
        sortByComboBox.setPromptText("Sort By");
        sortByComboBox.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        sortByComboBox.setPrefSize(140, 40);

        sortByComboBox.setOnAction(e -> {
            if (sortByComboBox.getValue() != null) {
                if (this.getParent() instanceof AppFrame) {
                    AppFrame appFrame = (AppFrame) this.getParent();
                    String sortBy = sortByComboBox.getValue();
                    if ("Time".equals(sortBy)) {
                        appFrame.getRecipeList().sortRecipesChronologically();
                    } else if ("A to Z".equals(sortBy)) {
                        appFrame.getRecipeList().sortRecipesAlphabetically();
                    }
                }
            }
        });

        upperRowRight.getChildren().addAll(sortByComboBox);

        this.getChildren().addAll(upperRowRight);
    
    }

    public Button getAddButton() {
        return addButton;
    }

}


class Footer extends HBox {
    private Button logoutButton;

    Footer() {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        this.getChildren().add(logoutButton);
    }

    private void handleLogout() {
        Stage stage = (Stage) this.getScene().getWindow();
        LoginPage loginPage = new LoginPage(stage);
        Scene scene = new Scene(loginPage, 300, 200);
        stage.setScene(scene);
    }
}


class AppFrame extends BorderPane {

    private Header header;
    private Footer footer;
    private RecipeList recipeList;
    private Button addButton;

    AppFrame(String username) {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, " + Constants.PRIMARY_COLOR + ", "
                + Constants.SECONDARY_COLOR + ");");
        header = new Header();
        recipeList = new RecipeList(username);

        this.setTop(header);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(recipeList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        this.setCenter(scroller);

        addButton = header.getAddButton();

        footer = new Footer();
        this.setBottom(footer);

        addListeners();
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
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene scene = new Scene(loginPage, 300, 200);
        primaryStage.setTitle("Pantry Pal Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop(){
        //deletes existing images of recipes
        for(File file: new File("images").listFiles()) 
        if (!file.isDirectory()) 
            file.delete();
        System.out.println("Stage is closing");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
