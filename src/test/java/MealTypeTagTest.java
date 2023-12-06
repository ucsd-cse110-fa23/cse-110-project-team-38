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

public class MealTypeTagTest {
    private RecipeItem item;
    private MockGPT mockGPT;
    private MockWhisper mockWhisper;
    


    @BeforeEach
    public void setUp() {
        mockGPT = new MockGPT();
        mockWhisper = new MockWhisper();
    }

    @Test
    //Test if meal type tags are correct with input
    public void TestGenerationWithMealTypeTagBreakfast() {
        try{
            
            String input = "Breakfast, I have steak and eggs.";
            String mealType = "";
            MockRecorder recorder = new MockRecorder(input);
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            if (prompt.contains("Breakfast") || prompt.contains("breakfast")) {
                mealType = "Breakfast";
            }
            else if (prompt.contains("Lunch") || prompt.contains("lunch")) {
                mealType = "Lunch";
            }
            else {
                mealType = "Dinner";
            }
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));
            item.setMealType(mealType);

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs Breakfast");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs Breakfast\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");
            assertEquals(item.getMealType(), "Breakfast");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if meal type tags are correct with input
    public void TestGenerationWithMealTypeTagLunch() {
        try{
            
            String input = "Lunch, I have steak and eggs.";
            String mealType = "";
            MockRecorder recorder = new MockRecorder(input);
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            if (prompt.contains("Breakfast") || prompt.contains("breakfast")) {
                mealType = "Breakfast";
            }
            else if (prompt.contains("Lunch") || prompt.contains("lunch")) {
                mealType = "Lunch";
            }
            else {
                mealType = "Dinner";
            }
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));
            item.setMealType(mealType);

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs Lunch");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs Lunch\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");
            assertEquals(item.getMealType(), "Lunch");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if meal type tags are correct with input
    public void TestGenerationWithMealTypeTagDinner() {
        try{
            
            String input = "Dinner, I have steak and eggs.";
            String mealType = "";
            MockRecorder recorder = new MockRecorder(input);
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            if (prompt.contains("Breakfast") || prompt.contains("breakfast")) {
                mealType = "Breakfast";
            }
            else if (prompt.contains("Lunch") || prompt.contains("lunch")) {
                mealType = "Lunch";
            }
            else {
                mealType = "Dinner";
            }
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));
            item.setMealType(mealType);

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs Dinner");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs Dinner\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");
            assertEquals(item.getMealType(), "Dinner");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }

    @Test
    //Test if meal type tags are correct with input
    public void TestGenerationWithNoMealTypeTag() {
        try{
            
            String input = "I have steak and eggs.";
            String mealType = "";
            MockRecorder recorder = new MockRecorder(input);
            String prompt = mockWhisper.sendRequest(recorder.getRecording());
            if (prompt.contains("Breakfast") || prompt.contains("breakfast")) {
                mealType = "Breakfast";
            }
            else if (prompt.contains("Lunch") || prompt.contains("lunch")) {
                mealType = "Lunch";
            }
            else {
                mealType = "Dinner";
            }
            String details = mockGPT.processRequest(prompt);

            String[] parts = details.split("\n");
            item.setRecipeTitle(parts[0]);
            item.setRecipeDescription(details.replace(parts[0], ""));
            item.setMealType(mealType);

            assertEquals(item.getFullRecipeTitle(), "Steak and Eggs");
            assertEquals(item.getFullRecipeDescription(), "Steak and Eggs\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve");
            assertEquals(item.getMealType(), "Dinner");

        } catch (Exception err) {
            System.out.println("Handle exceptions");
        }
    }
}
