import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RecipeItem;
import PantryPal.server.MockGPT;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RegenerationTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;

    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test use of mockGPT and mockWhisper objects to create new recipe and regenerate
    public void testRecipeCreationAndRegeneration() {
        try{
            
            MockRecorder recorder = new MockRecorder("Breakfast, I have steak and eggs.");
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));

            //regenrates again
            prompt = mockWhisper.sendRequest(recorder.getRecording());
            details = mockGPT.processRequest(prompt);

            parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs Breakfast");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs Breakfast\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }
}
