package TodoList;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.scene.text.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Task extends HBox {

    private Label index;
    private TextField taskName;
    private Button doneButton;

    private boolean markedDone;

    Task() {
        this.setPrefSize(500, 20); // sets size of task
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
        markedDone = false;

        index = new Label();
        index.setText(""); // create index label
        index.setPrefSize(40, 20); // set size of Index label
        index.setTextAlignment(TextAlignment.CENTER); // Set alignment of index label
        index.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the task
        this.getChildren().add(index); // add index label to task

        taskName = new TextField(); // create task name text field
        taskName.setPrefSize(380, 20); // set size of text field
        taskName.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // set background color of texfield
        index.setTextAlignment(TextAlignment.LEFT); // set alignment of text field
        taskName.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the text field
        this.getChildren().add(taskName); // add textlabel to task

        doneButton = new Button("Done"); // creates a button for marking the task as done
        doneButton.setPrefSize(100, 20);
        doneButton.setPrefHeight(Double.MAX_VALUE);
        doneButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); // sets style of button

        //modified code from partner Athu.... WHY WOULD YOU NOT INITIALIZE IT WITHIN THE CONSTRUCTOR?
            doneButton.setOnAction(e1 -> {
                this.toggleDone();
            });

        this.getChildren().add(doneButton);
    }

    public void setTaskIndex(int num) {
        this.index.setText(num + ""); // num to String
        this.taskName.setPromptText("Task " + num);
    }

    public TextField getTaskName() {
        return this.taskName;
    }

    public Button getDoneButton() {
        return this.doneButton;
    }

    public boolean isMarkedDone() {
        return this.markedDone;
    }

    public void toggleDone() {
        
        markedDone = !markedDone;
        
        if(markedDone){
            this.setStyle("-fx-border-color: #000000; -fx-border-width: 0; -fx-font-weight: bold;"); // remove border of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #BCE29E; -fx-border-width: 0;"); // change color of task to green
            }
        } else{
            //else return it to normal
            this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;"); // sets background color of task
            for (int i = 0; i < this.getChildren().size(); i++) {
                this.getChildren().get(i).setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;"); 
            }
        }
    }
}

class TaskList extends VBox {

    TaskList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void updateTaskIndices() {
        int index = 1;
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Task) {
                ((Task) this.getChildren().get(i)).setTaskIndex(index);
                index++;
            }
        }
    }

    public void removeCompletedTasks() {
        this.getChildren().removeIf(task -> task instanceof Task && ((Task) task).isMarkedDone());
        this.updateTaskIndices();
    }

    /*
     * Load tasks from a file called "tasks.txt"
     * Add the tasks to the children of tasklist component
     * tasks.txt is comma separated
     * tasks.txt stores: status as 0 / 1 for incomplete and complete
     *                   taskDescription after the first 0/1
     */
    public void loadTasks() {
        // hint 1: use try-catch block
        // hint 2: use BufferedReader and FileReader
        // hint 3: task.getTaskName().setText() sets the text of the task
        FileReader in = null;
        BufferedReader r = null;
        try{
            in = new FileReader("tasks.txt");
            r = new BufferedReader(in);
            int count = 1;
        while(r.ready()){
            //read the line for the text
            String txt = r.readLine();
            // update the task
            Task readTask = new Task();
            readTask.setTaskIndex(count);
            readTask.getTaskName().setText(txt);
            count++;
            

            

            //put task into the taskList:

            this.getChildren().add(readTask);
        }
        } catch (Exception e) {
            System.err.println("Error while loading tasks from file:" + e.getMessage());
        } finally{
            if (in != null){try{in.close();}catch(Exception e){System.err.println(e.getMessage());}}
            if (r != null){try{r.close();}catch(Exception e){System.err.println(e.getMessage());}}
            this.updateTaskIndices();
        }
    }

    /*
     * Save tasks to a file called "tasks.txt"
     */
    public void saveTasks() {
        // hint 1: use try-catch block
        // hint 2: use FileWriter
        // hint 3: this.getChildren() gets the list of tasks
        FileWriter fr = null;
        try {
            fr = new FileWriter("./tasks.txt");
            // for( int i = 0; i < this.getChildren().size(); i++){}
            for (Node t : this.getChildren()) {
                fr.write(   ((Task)t).getTaskName().getText()    );
                fr.write('\n');
            }
        } catch (Exception e) {
            System.err.println("Error while writing to file:" + e.getMessage());
        } finally{
            if (fr != null){try{fr.close();}catch(Exception e){System.err.println(e.getMessage());}}
            System.out.println("Saved to tasks.txt");
        }
    }

    /*
     * Sort the tasks lexicographically
     */
    public void sortTasks() {
        // hint 1: this.getChildren() gets the list of tasks
        // hint 2: Collections.sort() can be used to sort the tasks
        // hint 3: task.getTaskName().setText() sets the text of the task
        // my lambda: (n1,n2) -> {return ((Task)n1).getTaskName().getText().compareTo(((Task)n2).getTaskName().getText()); }
        List<Node> sl = this.getChildren().sorted((n1,n2) -> {return ((Task)n1).getTaskName().getText().compareTo(((Task)n2).getTaskName().getText()); }); //gives a SortedList
        this.getChildren().setAll(sl); //im assuming this takes all entries in sl and puts it into the thingy....
        this.updateTaskIndices();
    }
}

class Footer extends HBox {

    private Button addButton;
    private Button clearButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;

    Footer() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addButton = new Button("Add Task"); // text displayed on add button
        addButton.setStyle(defaultButtonStyle); // styling the button
        clearButton = new Button("Clear finished"); // text displayed on clear tasks button
        clearButton.setStyle(defaultButtonStyle);

        
        // Create loadButton, saveButton and sortButton to the footer
        loadButton = new Button("Load Tasks");
        loadButton.setStyle(defaultButtonStyle);

        saveButton = new Button("Save Tasks");
        saveButton.setStyle(defaultButtonStyle);

        sortButton = new Button("Sort Tasks");
        sortButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addButton, clearButton,loadButton, saveButton, sortButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center

    }
    
    public Button getAddButton() {
        return addButton;
    }
    
    public Button getClearButton() {
        return clearButton;
    }
    
    //  Add getters for loadButton, saveButton and sortButton
    public Button getLoadButton(){
        return loadButton;
    }
    
    public Button getSaveButton(){
        return saveButton;
    }
    
    
    public Button getSortButton(){
        return sortButton;
    }
}

class Header extends HBox {

    Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("To Do List"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

class AppFrame extends BorderPane{

    private Header header;
    private Footer footer;
    private TaskList taskList;

    private Button addButton;
    private Button clearButton;
    private Button loadButton;
    private Button saveButton;
    private Button sortButton;

    AppFrame()
    {
        // Initialise the header Object
        header = new Header();

        // Create a tasklist Object to hold the tasks
        taskList = new TaskList();
        
        // Initialise the Footer Object
        footer = new Footer();

        // TODO: Add a Scroller to the Task List
        // hint 1: ScrollPane() is the Pane Layout used to add a scroller - it will take the tasklist as a parameter
        // hint 2: setFitToWidth, and setFitToHeight attributes are used for setting width and height
        // hint 3: The center of the AppFrame layout should be the scroller window instead  of tasklist
        ScrollPane sp = new ScrollPane(taskList);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);


        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(sp);
        // Add footer to the bottom of the BorderPane
        this.setBottom(footer);

        // Initialise Button Variables through the getters in Footer
        addButton = footer.getAddButton();
        clearButton = footer.getClearButton();
        loadButton = footer.getLoadButton();
        saveButton = footer.getSaveButton();
        sortButton = footer.getSortButton();

        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners()
    {

        // Add button functionality
        addButton.setOnAction(e -> {
            // Create a new task
            Task task = new Task();
            // Add task to tasklist
            taskList.getChildren().add(task);
            
            // Add doneButtonToggle to the Done button
            Button doneButton = task.getDoneButton();
            doneButton.setOnAction(e1 -> {
                // Call toggleDone on click
                task.toggleDone();
            });
            // Update task indices
            taskList.updateTaskIndices();
        });
        
        // Clear finished tasks
        clearButton.setOnAction(e -> {
            taskList.removeCompletedTasks();
        });


        // Load Tasks
        loadButton.setOnAction(e -> {
            taskList.loadTasks();
        });
        // Save Tasks

        saveButton.setOnAction(e ->{
            taskList.saveTasks();
        });

        // Sort Tasks
        sortButton.setOnAction(e ->{
            taskList.sortTasks();
        });
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Setting the Layout of the Window- Should contain a Header, Footer and the TaskList
        AppFrame root = new AppFrame();

        // Set the title of the app
        primaryStage.setTitle("To Do List");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
