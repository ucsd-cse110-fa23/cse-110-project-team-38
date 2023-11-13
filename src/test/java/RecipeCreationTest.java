import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockGPT;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RecipeItem;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeCreationTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;
    


    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test use of mockGPT and mockWhisper objects to create new recipe
    public void testRecipeCreation() {
        try {

                String prompt = mockWhisper.sendRequest();
                String details = mockGPT.processRequest(prompt);

                String[] parts = details.split("\n");

                item.setRecipeTitle(parts[0]);
                item.setRecipeDescription(details.replace(parts[0], ""));

                assertEquals(true,item == null);
            } catch (Exception err) {
                System.out.println("Handle Exceptions");
            }
    }

    @Test
    //Test if title is the same as the mock and correct
    public void testCreatedRecipeTitle() {
        try {
                String prompt = mockWhisper.sendRequest();
                String details = mockGPT.processRequest(prompt);

                String[] parts = details.split("\n");

                item.setRecipeTitle(parts[0]);
                item.setRecipeDescription(details.replace(parts[0], ""));

                assertEquals(item.getFullRecipeTitle(), "Title");
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if description is correct
    public void testCreatedRecipeDescription() {
        try {
                String prompt = mockWhisper.sendRequest();
                String details = mockGPT.processRequest(prompt);

                String[] parts = details.split("\n");

                item.setRecipeTitle(parts[0]);
                item.setRecipeDescription(details.replace(parts[0], ""));

                assertEquals(item.getFullRecipeDescription(), "Ingredients:...\nInstructions:...");
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if generating and saving recipes work
    public void testRecipeCreationGivenVoiceInput() {
        try{
            
            MockRecorder recorder = new MockRecorder("Breakfast, I have steak and eggs.");
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs Breakfast");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs Breakfast\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if generating with no input
    public void testRecipeCreationGivenNoVoiceInput() {
        try {
            MockRecorder recorder = new MockRecorder("");
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));

            assertEquals(item.getFullRecipeTitle(), "Grilled Cheese Sandwich");
            assertEquals(item.getFullRecipeDescription(), "Grilled Cheese Sandwich\nIngredients:\nbreak\ncheese\nInstructions:\n1. put cheese in between break\n2. grill and serve");

            
        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }
}
