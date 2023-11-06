package PantryPal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class mock {


    RecipeItem shortTitle = new RecipeItem();
    shortTitle.setRecipeTitle("Roasted Chicken");
    shortTitle.setRecipeDescription("Place the chicken in the oven, on the middle rack, and roast for an hour, at the thickest part,"+ 
    "reaches 165 degrees.Use a large spoon or baster to baste the chicken with the drippings every 30 minutes.");

    RecipeItem shortDesciption = new RecipeItem();
    shortDesciption.setRecipeTitle("Peanut Butter Toast");
    shortDesciption.setRecipeDescription("Toasted your bread and put peanut butter spread on it.");

    RecipeItem longTitle = new RecipeItem();
    longTitle.setRecipeTitle("Cheesy Mixed Pasta Casserole With Mushrooms, Chicken, Peas and Alfredo Sauce");
    longTitle.setRecipeDescription("In a large skillet, melt the remaining 2 tablespoons of butter." +
    "Add the mushrooms, season with salt and pepper and cook over moderately high heat until browned in spots, about 5 minutes."+
    "Add the mushrooms to the porcini sauce. Stir in the pasta and fontina cheese and season with salt and pepper."+
    "Scrape the pasta into an 8 1/2-by-12-inch baking dish.");

    RecipeItem longDescription = new RecipeItem();
    Path DescriptionFilePath = Paths.get("*insert your filepath");
    String fullDescription = Files.readString(DescriptionFilePath);
    longDescription.setRecipeTitle("");
    longDescription.setRecipeDescription(fullDescription);

    RecipeItem longTitleAndDescription = new RecipeItem();
    Path TitleFilePath = Paths.get("*insert your filepath");
    String fullTitle = Files.readString(TitleFilePath);
    longTitleAndDescription.setRecipeTitle(fullTitle);
    String fullDescription2 = Files.readString(DescriptionFilePath);
    longDescription.setRecipeTitle("");
    longDescription.setRecipeDescription(fullDescription2);

    RecipeItem noTitle = new RecipeItem();
    noTitle.setRecipeTitle("");
    noTitle.setRecipeDescription("In a large skillet, cook the butter over moderately high heat until lightly browned, 5 minutes."+
    "Stir in the tortelli, the reserved pasta water and the remaining 1/4 cup of grated cheese. Serve right away.");

    RecipeItem noDescription = new RecipeItem();
    noDescription.setRecipeTitle("Spinach-and-Ricotta Tortelli with Browned Butter");
    noDescription.setRecipeDescription("");

    RecipeItem noTitleAndDescription = new RecipeItem();
    noTitleAndDescription.setRecipeTitle("");
    noTitleAndDescription.setRecipeDescription("");
    








    
}
