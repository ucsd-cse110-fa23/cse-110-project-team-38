import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockDallE;
import PantryPal.client.MockGPT;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RecipeItem;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MockDallETest {
    private MockDallE mockDallE;

    @BeforeEach
    public void setUp() {
        mockDallE = new MockDallE();
    }

    @Test
    //test if mockdalle output is correct
    public void MockDallETest() {
        try {
            String response = mockDallE.processRequest("Test prompt");
            assertEquals("image.jpg", response);
        }
        catch (Exception err) {
            System.out.println("Handle Exception");
        }
    }
}
