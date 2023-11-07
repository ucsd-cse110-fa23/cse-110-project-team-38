


import main.java.PantryPal.MockGPT;
import main.java.PantryPal.MockWhisper;
import main.java.PantryPal.RecipeItem;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

                assertNotEquals(item, null);
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
}
