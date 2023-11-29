import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RecipeEncryptor;
import PantryPal.client.RecipeItem;
import PantryPal.server.MockGPT;
import PantryPal.server.serverTestApp.Model;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class RecipeDeletingTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;

    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Delete recipe from server
    public void testRecipeDeletion() {
        try {

            String title = RecipeEncryptor.encryptSingle("Steak and Eggs");
            String description = RecipeEncryptor.encryptSingle("Ingredients... instructions...");
            Model model = new Model();
            String query = RecipeEncryptor.encryptSingle("Steak and Eggs");
            
            model.performRequest("POST", title, description, null);

            model.performRequest("DELETE", null, null, query);

            String response = model.performRequest("GET", null, null, query);

            assertNotEquals("Ingredients... instructions...", response);
            } catch (Exception err) {
                System.out.println("Handle Exceptions");
        }
    }

    @Test
        //Scenario test when deleting the recipe
        public void recipeDeletionTest() {
            try {
                MockRecorder recorder = new MockRecorder("Breakfast, I have steak and eggs.");
                String prompt = mockWhisper.sendRequest(recorder.getRecording());
                String details = mockGPT.processRequest(prompt);

                String[] parts = details.split("\n");
                item.setRecipeTitle(parts[0]);
                item.setRecipeDescription(details.replace(parts[0], ""));

                String title = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());
                String description = RecipeEncryptor.encryptSingle(item.getFullRecipeDescription());
                Model model = new Model();
                String query = RecipeEncryptor.encryptSingle(item.getFullRecipeTitle());

                model.performRequest("POST", title, description, null);

                model.performRequest("DELETE", null, null, query);

                String response = model.performRequest("GET", null, null, query);

                assertEquals(item.getFullRecipeDescription(), response);
            } catch (Exception err) {
                System.out.println("Handle Exceptions");
            }
        }
}
