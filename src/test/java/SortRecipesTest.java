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

public class SortRecipesTest {
    private RecipeItem item1;
    private RecipeItem item2;
    private RecipeItem item3;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;
    


    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test use of sorting alphabetically
    public void testSortAlphabetically() {
        try {
            item1.setRecipeTitle("Chicken");
            item2.setRecipeTitle("Bacon");
            item3.setRecipeTitle("Eggs");

            List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
            recipeItems.add(item1);
            recipeItems.add(item2);
            recipeItems.add(item3);

            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item2);
            expected.add(item1);
            expected.add(item3);

            Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getRecipeTitle()));

            assertEquals(expected, recipeItems);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

    @Test
    //Test use sorting alphabetically backwards
    public void testSortAlphabeticallyBackwards() {
        try {
            item1.setRecipeTitle("Chicken");
            item2.setRecipeTitle("Bacon");
            item3.setRecipeTitle("Eggs");

            List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
            recipeItems.add(item1);
            recipeItems.add(item2);
            recipeItems.add(item3);

            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item3);
            expected.add(item1);
            expected.add(item2);

            Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getRecipeTitle()));
            Collections.reverse(recipeItems);

            assertEquals(expected, recipeItems);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

    @Test
    //Test use of sorting chronologically
    public void testSortChronologically() {
        try {
            item1.setRecipeTitle("Chicken");
            item2.setRecipeTitle("Bacon");
            item3.setRecipeTitle("Eggs");

            List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
            recipeItems.add(item1);
            recipeItems.add(item2);
            recipeItems.add(item3);

            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item1);
            expected.add(item2);
            expected.add(item3);

            Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getCreationTimestamp()));

            assertEquals(expected, recipeItems);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }

    @Test
    //Test use of sorting chronologically backwards
    public void testSortChronologicallyBackwards() {
        try {
            item1.setRecipeTitle("Chicken");
            item2.setRecipeTitle("Bacon");
            item3.setRecipeTitle("Eggs");

            List<RecipeItem> recipeItems = new ArrayList<RecipeItem>();
            recipeItems.add(item1);
            recipeItems.add(item2);
            recipeItems.add(item3);

            List<RecipeItem> expected = new ArrayList<RecipeItem>();
            expected.add(item3);
            expected.add(item2);
            expected.add(item2);

            Collections.sort(recipeItems, Comparator.comparing(node -> ((RecipeItem) node).getCreationTimestamp()));
            Collections.reverse(recipeItems);

            assertEquals(expected, recipeItems);
        }
        catch (Exception err) {
            System.out.println("Handle exception");
        }
    }
}
