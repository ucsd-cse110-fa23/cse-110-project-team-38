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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.prefs.Preferences;

class Constants {
    public static final String PRIMARY_COLOR = "#2E4053";
    public static final String SECONDARY_COLOR = "#D5D8DC";
    public static final String BUTTON_HOVER_COLOR = "#566573";
    public static final int MAX_TITLE_LENGTH = 15;
    public static final int MAX_DESCRIPTION_LENGTH = 40;
}

class RecipeList extends VBox {
    public String username;

    //private List<Node> originalRecipeList;

    RecipeList(String username) {
        this.username = username;
        this.setSpacing(5);
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: white;");
        this.loadRecipes();
    }

    public JSONObject buildRecipeJSON(RecipeItem recipeItem, JSONObject jsonObject) {
        jsonObject.put("title", recipeItem.getFullRecipeTitle());
        jsonObject.put("description", recipeItem.getFullRecipeDescription());
        jsonObject.put("isGenerated", recipeItem.isGenerated());
        jsonObject.put("username", username);
        jsonObject.put("mealType", recipeItem.getMealType());
        return jsonObject;
    }
        

    public void removeRecipe(RecipeItem recipeItem) {
        //remove from the UI
        this.getChildren().remove(recipeItem);

        JSONObject json = new JSONObject();
        json = buildRecipeJSON(recipeItem, json);

        RequestSender request = new RequestSender();
        String response = request.performRequest("DELETE", null, json, recipeItem.getFullRecipeTitle(), username);

        //TODO: working code below!!! port to server!!!
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
        RequestSender request = new RequestSender();
        ArrayList<RecipeItem> recipeList = new ArrayList<>();
        System.out.println("Sending get request for all recipes");
        String response = request.performRequest("GET", null, null, "ALL", username);
        System.out.println(response);

        //originalRecipeList.addAll(this.getChildren());



        //TODO: given a response in json form, unpack and turn into many RecipeItem or however you want to do this
        try {
            JSONArray responseArray = new JSONArray(response);
            for (int i = 0; i < responseArray.length(); i++) {
            RecipeItem item = new RecipeItem();
            item.setRecipeDescription(responseArray.getJSONObject(i).getString("description"));
            item.setRecipeTitle(responseArray.getJSONObject(i).getString("title"));
            item.setGenerated(responseArray.getJSONObject(i).getBoolean("isGenerated"));
            item.setMealType(responseArray.getJSONObject(i).getString("mealType"));
            recipeList.add(item);
            
            
            //have a copy of original recipes
            //originalRecipeList.add(item);
            }

        }

        catch (Exception err) {
            System.out.println("Empty");
        }

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

        //MongoCollection<Document> recipesCollection = DatabaseConnect.getCollection("recipes");
        for (Node node : this.getChildren()) {
            if (node instanceof RecipeItem) {
                RecipeItem recipe = (RecipeItem) node;
                JSONObject json = new JSONObject();

                //TODO: pack recipe item data we need to save into json
                json.put("title", recipe.getFullRecipeTitle());
                json.put("description", recipe.getFullRecipeDescription());
                json.put("isGenerated", recipe.isGenerated());
                json.put("username", username);
                json.put("mealType", recipe.getMealType());

                System.out.println(json.toString());

                String response = request.performRequest("POST", null, json, null, null); //perform a save post given json and no query

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
        Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getCreationTimestamp()).reversed());

        // Clear the existing children and add the sorted recipe items
        this.getChildren().clear();
        this.getChildren().addAll(recipeItems);
    }
    
  
  
    public void filterRecipesByMealType(String mealType) {
        List<Node> recipeItems = new ArrayList<>(this.getChildren());

        // Check if the meal type is "all"; if yes, show all recipes
        if ("all".equalsIgnoreCase(mealType)) {
            this.getChildren().clear();
            this.getChildren().addAll(recipeItems);
            return; 
        }
        
        List<RecipeItem> filteredRecipes = new ArrayList<>();

        // Iterate through the recipe items and filter by meal type
        for (Node node : recipeItems) {
            if (node instanceof RecipeItem) {
                RecipeItem recipe = (RecipeItem) node;
                if (mealType.equalsIgnoreCase(recipe.getMealType())) {
                // Add the recipe to the filtered list if it matches the specified meal type
                filteredRecipes.add(recipe);
                }
            }
        }
         // Update the UI with the filtered list
         this.getChildren().clear();
         this.getChildren().addAll(filteredRecipes);
}


        // this.getChildren().clear();
        
        // // Show all recipes if "all" is selected
        // if ("All".equalsIgnoreCase(mealType)) {
        //     this.getChildren().addAll(originalRecipeList);
        // } else {
        //     // Filter recipes based on the selected meal type
        //     for (Node recipe : originalRecipeList) {
        //         if (((RecipeItem) recipe).getMealType().equalsIgnoreCase(mealType)) {
        //             this.getChildren().add(recipe);
        //         }
        //     }
        // }
    //}


    // public List<Node> getOriginalRecipes() {
    //     return this.originalRecipeList;
    // }
    
    






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

        // Create a dropdown menu for filtering by meal type

        ComboBox<String> mealTypeFilterComboBox = new ComboBox<>(FXCollections.observableArrayList("All", "Breakfast", "Lunch", "Dinner"));
        mealTypeFilterComboBox.setPromptText("Filter By Meal Type");
        mealTypeFilterComboBox.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        mealTypeFilterComboBox.setPrefSize(140, 40);
   
        mealTypeFilterComboBox.setOnAction(e -> {
            if (mealTypeFilterComboBox.getValue() != null) {
                if (this.getParent() instanceof AppFrame) {
                    AppFrame appFrame = (AppFrame) this.getParent();
                    String filterByMealType = mealTypeFilterComboBox.getValue();
                    appFrame.getRecipeList().filterRecipesByMealType(filterByMealType);
                }
            }
        });
   
        upperRowRight.getChildren().addAll(sortByComboBox, mealTypeFilterComboBox);
       //upperRowRight.getChildren().addAll(sortByComboBox);
       this.getChildren().addAll(upperRowRight);
    
    }
       
   

       

       


    public Button getAddButton() {
        return addButton;
    }

}


class Footer extends HBox {
    private Button logoutButton;
    private Label usernameLabel;

    Footer(String username) {
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        // Username label
        usernameLabel = new Label("Logged in as: " + username);
        usernameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");
        this.getChildren().add(usernameLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        // Logout button
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        this.getChildren().add(logoutButton);
    }

    private void handleLogout() {
        clearStoredCredentials();
        Stage stage = (Stage) this.getScene().getWindow();
        LoginPage loginPage = new LoginPage(stage);
        Scene scene = new Scene(loginPage, 300, 200);
        stage.setScene(scene);
    }

    private void clearStoredCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(LoginPage.class);
        prefs.remove("username");
        prefs.remove("password");
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

        footer = new Footer(username);
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
