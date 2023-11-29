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

import PantryPal.server.serverTestApp.Model;

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

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq;
import org.bson.types.ObjectId;

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

    // public void addMocks() {
    //     RecipeItem mock1 = new RecipeItem();
    //     mock1.setRecipeTitle("Bacon and Eggs");
    //     mock1.setRecipeDescription("You take the moon and you take the sun");

    //     RecipeItem mock2 = new RecipeItem();
    //     mock2.setRecipeTitle("Shrimp Fried Rice");
    //     mock2.setRecipeDescription("So you're telling me a SHRIMP fried this rice!?");

    //     RecipeItem mock3 = new RecipeItem();
    //     mock3.setRecipeTitle("Nothing burger");
    //     mock3.setRecipeDescription("Absolutely nothing");

    //     this.getChildren().addAll(mock1, mock2, mock3);
    // }

    public void removeRecipe(RecipeItem recipeItem) {
        //remove from the UI
        this.getChildren().remove(recipeItem);

        //remove from database
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        Bson filter = Filters.and(
                Filters.eq("username", username),
                Filters.eq("title", recipeItem.getFullRecipeTitle()),
                Filters.eq("description", recipeItem.getFullRecipeDescription())
        );
        recipesCollection.deleteOne(filter);
    }


    public void loadRecipes() {
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        FindIterable<Document> recipes = recipesCollection.find(eq("username", username));
        for (Document recipeDoc : recipes) {
            RecipeItem recipe = new RecipeItem();
            recipe.setRecipeTitle(recipeDoc.getString("title"));
            recipe.setRecipeDescription(recipeDoc.getString("description"));
            recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
            this.getChildren().add(recipe);
        }
    }

    public void saveRecipes() {
        MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        for (Node node : this.getChildren()) {
            if (node instanceof RecipeItem) {
                RecipeItem recipe = (RecipeItem) node;
                Document recipeDoc = new Document("username", username)
                                        .append("title", recipe.getFullRecipeTitle())
                                        .append("description", recipe.getFullRecipeDescription());

                if (recipe.getRecipeId() == null || recipe.getRecipeId().isEmpty() || recipe.isGenerated()) {
                    // Insert new recipe only if it's generated and not yet saved
                    recipesCollection.insertOne(recipeDoc);
                    recipe.setRecipeId(recipeDoc.getObjectId("_id").toString());
                    recipe.setGenerated(false); // Reset the generated flag
                } else {
                    // Update existing recipe
                    ObjectId id = new ObjectId(recipe.getRecipeId());
                    Bson filter = Filters.eq("_id", id);
                    recipesCollection.updateOne(filter, new Document("$set", recipeDoc));
                }
            }
        }
    }


    /*
     * saves recipes to the HTTP server, if its running
     */
    public void saveRecipesToServer() {
        Model request = new Model();
        for (Node node : this.getChildren()) {
            if (node instanceof RecipeItem) {
                RecipeItem recipe = (RecipeItem) node;
                String response = request.performRequest("PUT",recipe.getFullRecipeTitle(),recipe.getFullRecipeDescription(),null);
                System.out.println("Response:" + response);
            
            }
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

class AppFrame extends BorderPane {

    private Header header;
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
    public void stop() throws Exception {
        DatabaseConnect.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
