package PantryPal.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class RecipeItem extends HBox {
    private VBox detailsBox;
    private Label recipeTitleLabel;
    private Label recipeDescriptionLabel;
    private String fullRecipeTitle;
    private String fullRecipeDescription;
    private String recipeId;
    private boolean generated;
    private LocalDateTime creationTimestamp;

    public RecipeItem() {
        this.creationTimestamp = LocalDateTime.now();
        this.setPrefSize(500, 120);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0; -fx-border-color: "
                + Constants.SECONDARY_COLOR
                + "; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

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

        this.setOnMouseClicked(e -> {

            try {
                AppFrame appFrame = (AppFrame) this.getScene().getRoot();
                RecipeDetailsPage detailsPage = new RecipeDetailsPage(appFrame, this, false, false);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.getScene().setRoot(detailsPage);
            }
            catch (Exception err) {
                System.out.println("Error generating details page");
            }
        });
    }

    private void setPrefSize(int i, int j) {
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

    public String getRecipeDescription() {
        return this.recipeDescriptionLabel.getText();
    }
    public LocalDateTime getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public boolean isGenerated() {
        return generated;
    }
    
    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String shareRecipe(String username){
        HttpClient client = HttpClient.newHttpClient();

        //request body format
        //System.out.println(username);
        //System.out.println(fullRecipeTitle);
        //System.out.println(fullRecipeDescription);
        String Id = Integer.toString(this.hashCode());
        System.out.println(Id);
        String formParams = "username=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                            "&title=" + URLEncoder.encode(this.fullRecipeTitle, StandardCharsets.UTF_8) +
                            "&description=" + URLEncoder.encode(this.fullRecipeDescription, StandardCharsets.UTF_8) +
                            "&id=" + URLEncoder.encode(Id, StandardCharsets.UTF_8);

        //POST request to server
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8100/share"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formParams))
                .build();

        return ("http://localhost:8100/sr" + "/" + username + "/" + Id);
    }

}