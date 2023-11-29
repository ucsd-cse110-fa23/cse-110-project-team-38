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

public class MockGPTTest {
    private MockGPT mockGPT;

    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
    }

    @Test
    //Test if mockGPT output works
    public void MockGPTTest() {
        try {
            String response = mockGPT.processRequest("Voice Ingredient input");
            assertEquals("Title\nIngredients:...\nInstructions:...", response);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

    @Test
    //Test if mockGPT output works with no voice input
    public void MockGPTTestWithNoInput() {
        try {
            String response = mockGPT.processRequest("");
            assertEquals("Title\nIngredients:...\nInstructions:...", response);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }
}
