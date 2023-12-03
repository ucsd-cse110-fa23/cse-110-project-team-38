import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RequestSender;
import PantryPal.client.RecipeEncryptor;
import PantryPal.client.RecipeItem;
import PantryPal.server.MockGPT;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeSavingTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;

    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test saving to server and getting
    public void saveRecipeToServer() {
        try {
            item.setRecipeTitle("Steak and Eggs");
            item.setRecipeDescription("Ingredients... instructions...");
            String title = RecipeEncryptor.encryptSingle("Steak and Eggs");
            String description = RecipeEncryptor.encryptSingle("Ingredients... instructions...");
            RequestSender model = new RequestSender();
            String query = RecipeEncryptor.encryptSingle("Steak and Eggs");
            
            model.performRequest("POST", title, description, null);

            String response = model.performRequest("GET", null, null, query);
            assertEquals(item.getFullRecipeDescription(), response);
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Scenario test when saving newly generated recipe
    public void saveNewlyGeneratedRecipe() {
        try {
            MockRecorder recorder = new MockRecorder("");
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));

            String title = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());
            String description = RecipeEncryptor.encryptSingle(item.getFullRecipeDescription());
            RequestSender model = new RequestSender();
            String query = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());
            
            model.performRequest("POST", title, description, null);

            String response = model.performRequest("GET", null, null, query);
            assertEquals(item.getFullRecipeDescription(), response);
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Edit recipe and save changes
    public void saveEditedRecipe() {

        try {
            item.setRecipeDescription("New Edited Description");

            String title = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());
            String description = RecipeEncryptor.encryptSingle(item.getFullRecipeDescription());
            RequestSender model = new RequestSender();
            String query = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());
            
            model.performRequest("POST", title, description, null);

            String response = model.performRequest("GET", null, null, query);
            assertEquals(item.getFullRecipeDescription(), response);
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }
}
