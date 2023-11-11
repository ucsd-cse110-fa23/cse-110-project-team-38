package main.java.PantryPal.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RecipeItem extends HBox {
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

    public Button getEditButton() {
        return this.editButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

}
