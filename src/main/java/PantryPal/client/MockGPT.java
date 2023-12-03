package PantryPal.client;




import java.io.IOException;
import java.net.URISyntaxException;

public class MockGPT implements IGPT {
    public String processRequest(String prompt) throws IOException, InterruptedException, URISyntaxException {
        return "Title\nIngredients:...\nInstructions:...";
    }

    public String mockProcessRequest(String prompt) {
        if (prompt.equals("Breakfast, I have steak and eggs.")) {
            return "Steak and Eggs Breakfast\nIngredients:\nsteak\neggs\nInstructions:\n1. put steak and eggs in stove\n2. Serve";
        }
        else if (prompt.equals("")){
            return "Grilled Cheese Sandwich\nIngredients:\nbreak\ncheese\nInstructions:\n1. put cheese in between break\n2. grill and serve";
        }
        else {
            return "";
        }
    }
}
