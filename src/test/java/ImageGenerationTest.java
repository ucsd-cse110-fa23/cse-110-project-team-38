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

public class ImageGenerationTest {
    RecipeItem item;
    MockDallE mockdalle;

    @BeforeEach
    public void setUp() {
        mockdalle = new MockDallE();
    }
    
    @Test
    //Test image generation for existing recipe when viewing details
    public void TestImageGenerationWhenViewingRecipe() {
        try {
            item.setRecipeDescription("Test recipe description");
            item.setRecipeTitle("Test Recipe Title");
            String imagePath = mockdalle.processRequest(item.getFullRecipeTitle());
            assertEquals("image.jpg", imagePath);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }
}
