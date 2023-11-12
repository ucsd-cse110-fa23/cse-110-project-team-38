package PantryPal.server.serverTestApp;

import PantryPal.client.RecipeEncryptor;
import javafx.event.ActionEvent;

public class Controller {
    private View view;
    private Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        this.view.setPostButtonAction(this::handlePostButton);
        this.view.setGetButtonAction(this::handleGetButton);
        this.view.setPutButtonAction(this::handlePutButton);
        this.view.setDeleteButtonAction(this::handleDeleteButton);
    }

    private void handlePostButton(ActionEvent event) {
        String title = RecipeEncryptor.encryptSingle(view.getTitle());
        String description = RecipeEncryptor.encryptSingle(view.getDescription());
        //encrypt then post
        String response = model.performRequest("POST", title, description, null);
        view.showAlert("Response", response);
    }

    private void handleGetButton(ActionEvent event) {
        //encrypt query field
        String query = RecipeEncryptor.encryptSingle(view.getQuery());
        String response = model.performRequest("GET", null, null, query);
        view.showAlert("Response", response);
    }

    private void handlePutButton(ActionEvent event) {
        // encrypt before sending
        String title = RecipeEncryptor.encryptSingle(view.getTitle());
        String description = RecipeEncryptor.encryptSingle(view.getDescription());
        String response = model.performRequest("PUT", title, description, null);
        view.showAlert("Response", response);
    }

    private void handleDeleteButton(ActionEvent event) {
        String query = RecipeEncryptor.encryptSingle(view.getQuery());
        String response = model.performRequest("DELETE", null, null, query);
        view.showAlert("Response", response);
    }
}