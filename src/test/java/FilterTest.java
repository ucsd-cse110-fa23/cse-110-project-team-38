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

            item1.setMealType("Breakfast");
            item2.setMealType("Lunch");
            item3.setMealType("Dinner");
            item4.setMealType("Dinner");

            List<RecipeItem> breakfastRecipes = new ArrayList<RecipeItem>();
            List<RecipeItem> lunchRecipes = new ArrayList<RecipeItem>();
            List<RecipeItem> dinnerRecipes = new ArrayList<RecipeItem>();


            breakfastRecipes.add(item1);
            lunchRecipes.add(item2);
            dinnerRecipes.add(item3);
            dinnerRecipes.add(item4);


            List<RecipeItem> filteredList = switch ("Breakfast".toLowerCase()) {
                case "breakfast" -> breakfastRecipes;
                case "lunch" -> lunchRecipes;
                case "dinner" -> dinnerRecipes;
                default -> allRecipes;
            };

            
            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item1);



            assertEquals(expected, breakfastRecipes);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

}