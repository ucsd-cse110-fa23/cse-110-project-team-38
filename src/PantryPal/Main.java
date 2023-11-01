package PantryPal;

// Add necessary imports
import javafx.application.Application;
import javafx.event.Event;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.event.EventHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;



class Header extends HBox {
    Header() {
        // this is COPY PASTE of TodoList Header except the title is different
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        Text titleText = new Text("Contact Manager"); // Text of the Header
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.getChildren().add(titleText);
        this.setAlignment(Pos.CENTER); // Align the text to the Center
    }
}

/**
 * A contact's fields can be directly modified using the TextField datatype --->
 * NO!
 * 
 * OK change of plans. If we double click a portrait, we open a window where:
 * 1. we can change the email, name, phone, and portrait
 * 2. we can delete the user!
 */
class Contact extends HBox {
    private Label name;
    private Label phone;
    private Label email;
    private Image img;
    private boolean deleteFlag = false;
    private Button selectButton;

    private ContactEditor ce;
    private Scene ceScene;
    
    public Contact() {
        this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");



        this.name = new Label("placeholder");
        this.name.setPrefSize(150, 50); // set size of name label
        this.name.setTextAlignment(TextAlignment.CENTER); // Set alignment of name label
        this.name.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the name label

        this.phone = new Label("placeholder");
        this.phone.setPrefSize(150, 50); // set size of name label
        this.phone.setTextAlignment(TextAlignment.CENTER); // Set alignment of phone label
        this.phone.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the phone label

        this.email = new Label("placeholder");
        this.email.setPrefSize(150, 50); // set size of name label
        this.email.setTextAlignment(TextAlignment.CENTER); // Set alignment of email label
        this.email.setPadding(new Insets(10, 0, 10, 0)); // adds some padding to the email label

        this.img = new Image("file:src/ContactManager/rdj.jpg");

        ImageView iv = new ImageView();
        // iv.setViewport(new Rectangle2D(50, 50, 100, 100));
        iv.setImage(img);
        iv.setFitHeight(75);
        iv.setFitWidth(75);

        this.selectButton = new Button("", iv);
        this.selectButton.setPrefSize(30, 50);

        this.ce = new ContactEditor(this, selectButton);
        this.ceScene = new Scene(ce,400,150);

        // button double click function
        this.selectButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && (mouseEvent.getClickCount() == 2)) {
                    openContactEditor(ceScene,ce);
                }
            }
        });
        // to add elements to an Box type thing... add each element in order of which
        // they should display (hbox is left to right)
        this.getChildren().addAll(selectButton, name, phone, email);
    }

    public void refreshContact(){
        if(!deleteFlag){
            this.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0; -fx-font-weight: bold;");
        } else{
            this.setStyle("-fx-background-color: #E87485; -fx-border-width: 0; -fx-font-weight: bold;");
        }
    }

    public void openContactEditor(Scene scene,ContactEditor ce) {
        Parent root;
        try {
            /**
             * make stage
             * make a the frame
             * make scene with frame
             * put scene in stage
             */
            Stage stage = new Stage();
            root = ce;
            stage.setTitle("Contact Editor");
            scene.setRoot(root);
            stage.setScene(scene); //400, 150
            stage.show();
            stage.setOnCloseRequest(e ->{
                ce.updateContactFields();
                this.refreshContact();
                System.out.println("contact editor closed!");
            });

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    // field getters
    public Label getName() {
        return this.name;
    }

    public Label getPhone() {
        return this.phone;
    }

    public Label getEmail() {
        return this.email;
    }

    public Image getImg() {
        return this.img;
    }

    public Boolean getDeleteFlag(){
        return this.deleteFlag;
    }

    public void flipDeleteFlag(){
        this.deleteFlag = !this.deleteFlag;
    }
    
}

class ContactEditor extends VBox {
    private FieldEditor editNameField;
    private FieldEditor editEmailField;
    private FieldEditor editPhoneField;
    private Button editImageButton;
    private Button deleteContactsButton;

    public ContactEditor(Contact contact,Button imgButton) {
        editNameField = new FieldEditor("New Name Entry", contact.getName());
        editEmailField = new FieldEditor("New Email Entry", contact.getEmail());
        editPhoneField = new FieldEditor("New Phone Entry", contact.getPhone());
        editImageButton = new Button("Upload New Image");
        editImageButton.setOnAction(e -> {
            uploadImage(imgButton);
        });
        
        deleteContactsButton = new Button("Toggle Delete? [  ]");
        deleteContactsButton.setOnAction(e ->{
            contact.flipDeleteFlag();
            if(contact.getDeleteFlag()){
            deleteContactsButton.setText("Toggle Delete? [x]");
            } else{
                deleteContactsButton.setText("Toggle Delete? [  ]");
            }
        });

        
        
        this.getChildren().addAll(editNameField, editEmailField, editPhoneField, editImageButton,deleteContactsButton);
    }
    /**
     * uploadImage modified from Lab 1
     */
    private void uploadImage(Button target) {
        FileChooser fileChooser = new FileChooser();
        // Select which extensions are allowed
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("THIS IS MY URI: " + selectedFile.toURI().toString());
            Image image = new Image(selectedFile.toURI().toString());
            ImageView iv = new ImageView(image);
            iv.setFitHeight(75);
            iv.setFitWidth(75);
            target.setGraphic(iv);
        }
    }

    public void updateContactFields(){
        //update if they ever got updated, and you didnt hit submit
        editNameField.updateText();
        editEmailField.updateText();
        editPhoneField.updateText();
    }

}

class FieldEditor extends HBox {
    private TextField field;
    private Label ref;
    private Button submitButton;

    public FieldEditor(String desc, Label ref) {
        this.field = new TextField();
        this.field.setText(ref.getText());
        this.ref = ref;
        this.submitButton = new Button("Submit");
        this.submitButton.setOnAction(e -> {
            updateText();
        });
        this.getChildren().addAll(new Label(desc), field, submitButton);
    }

    public void updateText() {
        ref.setText(field.getText());
    }
}

class ContactList extends VBox {
    // vbox must contain a list of contacts

    // copy pasted from lab 1
    public ContactList() {
        this.setSpacing(5); // sets spacing between tasks
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: #F0F8FF;");
    }

    public void addContact() {
        this.getChildren().add(new Contact());
    }

    public void deleteContacts() {
        System.out.println("deleting selected contacts");
        this.getChildren().removeIf(n -> n instanceof Contact && ((Contact) n).getDeleteFlag());
    }

    //Export to CSV file
    public void saveData(){
        String out = "contacts.csv";
        try (FileWriter writer = new FileWriter(out)) {
            writer.write("Name,Email,Phone,Image\n");//header
            for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Contact) {
                writer.write(((Contact) this.getChildren().get(i)).getName().getText() 
                + "," + ((Contact) this.getChildren().get(i)).getEmail().getText() + "," 
                + ((Contact) this.getChildren().get(i)).getPhone().getText() + 
                "," + ((Contact) this.getChildren().get(i)).getImg().getUrl() + "\n");
            
            }
        }
            System.out.println("Saved Complete");
            writer.close();
        } catch (IOException e) {
            System.out.println("save tasks failed");
        }
    }

    //sort contacts via names in alphabetical order 
    public void sortContacts(){
        List<Node> sl = this.getChildren().sorted((n1,n2) -> {return ((Contact)n1).getName().getText().compareTo(((Contact)n2).getName().getText()); }); //gives a SortedList
        this.getChildren().setAll(sl); //im assuming this takes all entries in sl and puts it into the thingy....
        System.out.println("Sorted Contacts!");
    }

}

class Footer extends HBox {
    private Button addContactButton;
    private Button deleteContactsButton;
    private Button sortContactsButton;
    private Button saveDataButton;

    public Footer() {

        // MODIFIED LAB 1 CODE
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: #F0F8FF;");
        this.setSpacing(15);

        // set a default style for buttons - background color, font size, italics
        String defaultButtonStyle = "-fx-font-style: italic; -fx-background-color: #FFFFFF;  -fx-font-weight: bold; -fx-font: 11 arial;";

        addContactButton = new Button("Add New Contact"); // text displayed on add button
        addContactButton.setStyle(defaultButtonStyle); // styling the button

        deleteContactsButton = new Button("Delete Marked Contacts"); // text displayed on clear tasks button
        deleteContactsButton.setStyle(defaultButtonStyle);

        sortContactsButton = new Button("Sort Contacts");
        sortContactsButton.setStyle(defaultButtonStyle);

        saveDataButton = new Button("Save all contacts"); //text displayed
        saveDataButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(addContactButton, deleteContactsButton, sortContactsButton, saveDataButton); // adding buttons to footer
        this.setAlignment(Pos.CENTER); // aligning the buttons to center
    }

    // button getters

    public Button getAddContactButton() {
        return this.addContactButton;
    }

    public Button getDeleteContactsButton() {
        return this.deleteContactsButton;
    }

    public Button getSortContactsButton(){
        return this.sortContactsButton;
    }

    public Button getSaveDataButton(){
        return this.saveDataButton;
    }
}

/**
 * heres my plan: we get have a top header, center contactList, and bottom
 * footer
 * 
 * the header simply displays the header that says "contact manager" or
 * something
 * 
 * the contactList houses all the contacts. Each contact will show: image, name,
 * phone no., email address
 * 
 * the footer contains buttons to perform the CRUD operations (create, read ,
 * update, delete)
 * the delete mechanism will be dependant on perhaps a checkbox feature, where
 * you can choose which contacts to delete
 */
class AppFrame extends BorderPane {

    private Header header;
    private ContactList contactList;
    private Footer footer;

    private Button addContactButton;
    private Button deleteContactsButton;
    private Button sortContactsButton;
    private Button saveDataButton;

    AppFrame() {
        header = new Header();
        contactList = new ContactList();
        footer = new Footer();

        ScrollPane sp = new ScrollPane(contactList);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);

        // Add header to the top of the BorderPane
        this.setTop(header);
        // Add scroller to the centre of the BorderPane
        this.setCenter(sp);

        this.setBottom(footer);

        this.addContactButton = footer.getAddContactButton();
        this.deleteContactsButton = footer.getDeleteContactsButton();
        this.sortContactsButton = footer.getSortContactsButton();
        this.saveDataButton = footer.getSaveDataButton();


        // Call Event Listeners for the Buttons
        addListeners();
    }

    public void addListeners() {
        this.addContactButton.setOnAction(e -> {
            contactList.addContact();
        });
        this.deleteContactsButton.setOnAction(e -> {
            contactList.deleteContacts();
            System.out.println("BUTTON EVENT: delete button clicked!");
        });

        this.sortContactsButton.setOnAction(e ->{
            contactList.sortContacts();
            System.out.println("BUTTON EVENT: sort contacts button clicked!");
        });

        this.saveDataButton.setOnAction(e -> {
            contactList.saveData();
            System.out.println("SAVE BUTTON CLICKED");
        });
    }
}

// JavaFX Application main entry point
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        AppFrame root = new AppFrame();

        // sets title of stage
        primaryStage.setTitle("Contact Manager");
        primaryStage.setScene(new Scene(root, 500, 600));
        // Make window non-resizable
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
