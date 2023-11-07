package PantryPal;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class RecipeCreationTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;
    


    @Before
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

                assertEquals(item.getRecipeTitle(), "Title");
            } catch (Exception err) {
                System.out.println("Handle Exceptions");
            }
    }

}
