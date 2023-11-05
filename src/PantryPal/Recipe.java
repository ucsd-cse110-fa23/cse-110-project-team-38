package PantryPal;

import javafx.scene.control.Label;

public class Recipe {
    private Label title;
    private Label description;

    public Label getTitle(){
        return this.title;
    }
    public void setTitle(String s){
        this.title.setText(s);
    }

    public Label getDescription(){
        return this.description;
    }
    public void setDescription(String s){
        this.description.setText(s);
    }
}
