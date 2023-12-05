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

public class MockWhisperTest {
    private MockWhisper mockWhisper;

    @BeforeEach
    public void setUp() {
        mockWhisper = new MockWhisper();
    }

    @Test
    //test if mockwhisper send request output works
    public void MockWhisperTest() {
        try {
            String response = mockWhisper.sendRequest();
            assertEquals("Test Prompt", response);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }
}
