package PantryPal.server.serverTestApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class View {
    private TextField titleField, descriptionField, queryField;
    private Button postButton, getButton, putButton, deleteButton;
    private GridPane grid;

    public View() {
        grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(10);

        Label titleLabel = new Label("Title:");
        titleField = new TextField();
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);

        Label descriptionLabel = new Label("Description:");
        descriptionField = new TextField();
        grid.add(descriptionLabel, 0, 1);
        grid.add(descriptionField, 1, 1);

        Label queryLabel = new Label("Query:");
        queryField = new TextField();
        grid.add(queryLabel, 0, 2);
        grid.add(queryField, 1, 2);

        postButton = new Button("Post");
        getButton = new Button("Get");
        putButton = new Button("Put");
        deleteButton = new Button("Delete");

        grid.add(postButton, 0, 3);
        grid.add(getButton, 1, 3);
        grid.add(putButton, 0, 4);
        grid.add(deleteButton, 1, 4);

    }

    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public String getQuery() {
        return queryField.getText();
    }

    public GridPane getGrid() {
        return grid;
    }

    public void setPostButtonAction(EventHandler<ActionEvent> eventHandler) {
        postButton.setOnAction(eventHandler);
    }

    public void setGetButtonAction(EventHandler<ActionEvent> eventHandler) {
        getButton.setOnAction(eventHandler);
    }

    public void setPutButtonAction(EventHandler<ActionEvent> eventHandler) {
        putButton.setOnAction(eventHandler);
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> eventHandler) {
        deleteButton.setOnAction(eventHandler);
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}