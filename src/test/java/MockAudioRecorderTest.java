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

public class MockAudioRecorderTest {
    private MockRecorder mockRecorder;

    @Test
    //Test if mockrecorder output is correct
    public void MockRecorderTest() {
        mockRecorder = new MockRecorder("Test Voice Input");
        mockRecorder.startRecording();
        mockRecorder.stopRecording();

        String response = mockRecorder.getRecording();
        assertEquals("Test Voice Input", response);
    }
}
