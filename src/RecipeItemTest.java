


// import org.junit.Test;
// import org.junit.Before;
// import static org.junit.Assert.*;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeEach;

import org.testfx.framework.junit.ApplicationTest;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.PantryPal.client.Main;
import javafx.scene.layout.StackPane;

public class RecipeItemTest extends ApplicationTest {
    private RecipeItem item;
    private String fullDescription;
    
    @Override
    public void start(Stage stage) {
        item = new RecipeItem();
        stage.setScene(new Scene(new StackPane(item), 500, 250));
        stage.show();
    }

    @Before
    public void setUp() {
        try {
            Path descriptionFilePath = Paths.get("C:\\Users\\steve\\OneDrive\\Documents\\GitHub\\cse-110-project-team-38\\src\\PantryPal\\longDescription.txt");
            fullDescription = Files.readString(descriptionFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            fullDescription = "";
        }
    }

    @Test
    // Test the truncated string title length
    public void testSetAndGetRecipeTitle() {
        interact(() -> {
            String title = "Pan-roasted pastry rolls, layered with an herbed tomato puree, a creamy blend of artisanal cheeses, and tender bites of aged salami. \r\n" + //
                    "Also known as a hot pocket (or pizza rolls).";
            item.setRecipeTitle(title);
            assertEquals(title, item.getFullRecipeTitle());
            assertTrue(item.getRecipeTitle().length() <= Constants.MAX_TITLE_LENGTH+3);
        });
    }

    @Test
    // Test the original title string length when it is longer than the max
    public void testGetFullRecipeTitle() {
        interact(() -> {
            String title = "Pan-roasted pastry rolls, layered with an herbed tomato puree, a creamy blend of artisanal cheeses, and tender bites of aged salami. \r\n" + //
                    "Also known as a hot pocket (or pizza rolls).";
            item.setRecipeTitle(title);
            assertEquals(title, item.getFullRecipeTitle());
            assertTrue(item.getFullRecipeTitle().length() > Constants.MAX_TITLE_LENGTH);
        });
    }

    @Test
    // Test the truncated string description length
    public void testSetAndGetRecipeDescription() {
        interact(() -> {
            item.setRecipeDescription(fullDescription);
            assertEquals(fullDescription, item.getFullRecipeDescription());
            assertTrue(item.getRecipeDescription().length() <= Constants.MAX_DESCRIPTION_LENGTH+3);
        });
    }

    @Test
    // Test the original string description length when it is longer than the max
    public void testGetFullRecipeDescription() {
        interact(() -> {
            item.setRecipeDescription(fullDescription);
            assertEquals(fullDescription, item.getFullRecipeDescription());
            assertTrue(item.getFullRecipeDescription().length() > Constants.MAX_DESCRIPTION_LENGTH);
        });
    }

    @Test
    // Test the empty title
    public void testEmptyRecipeTitle() {
        interact(() -> {
            String title = "";
            item.setRecipeTitle(title);
            assertEquals("Empty title should be an empty string", "", item.getFullRecipeTitle());
        });
    }

    @Test
    // Test the empty description
    public void testEmptyRecipeDescription() {
        interact(() -> {
            String description = "";
            item.setRecipeDescription(description);
            assertEquals("Empty description should be an empty string", "", item.getFullRecipeDescription());
        });
    }

}
