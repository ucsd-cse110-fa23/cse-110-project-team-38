package PantryPal.server;

public class RecipeData {
    private String description;
    private String title;
    
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String desc){
        this.description = desc;
    }

    public String export(){ // add other fields as needed into the new string[]
        return StringPacker.encryptMany( new String[]{title,description});
    }
}
