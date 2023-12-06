import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PantryPal.client.IAudioRecorder;
import PantryPal.client.MockGPT;
import PantryPal.client.MockRecorder;
import PantryPal.client.MockWhisper;
import PantryPal.client.RecipeItem;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterTest {
    private RecipeItem item1;
    private RecipeItem item2;
    private RecipeItem item3;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;
    private RecipeList list1;



    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test use of sorting alphabetically
    public void testFilterBreakfast() {
        try {
            item1.setRecipeType("Breakfast");
            item2.setRecipeType("Lunch");
            item3.setRecipeType("Dinner");
            item4.setRecipeType("Dinner");

            List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
            recipeItems.add(item1);
            recipeItems.add(item2);
            recipeItems.add(item3);
            recipeItems.add(item4);
            recipeItems.filterRecipesByMealType("breakfast");
            

            



            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item1);



            assertEquals(expected, recipeItems);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

}