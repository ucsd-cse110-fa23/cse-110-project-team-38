package PantryPal;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.util.stream.Collectors;
import java.util.List;
import javafx.scene.layout.Region;
import java.io.PrintWriter;
import java.io.IOException;
import javafx.scene.Node;
import java.util.Collections;
import java.util.ArrayList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


class Constants {
    public static final String PRIMARY_COLOR = "#2E4053";
    public static final String SECONDARY_COLOR = "#D5D8DC";
    public static final String BUTTON_HOVER_COLOR = "#566573";
}

class ContactItem extends HBox {
    private ImageView contactImageView;
    private VBox detailsBox;
    private Label contactNameLabel; 
    private Label contactPhoneLabel; 
    private Label contactEmailLabel; 
    private Button editButton;
    private Button deleteButton;

    ContactItem() {
        this.setPrefSize(500, 120); 
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: white; -fx-border-width: 0 0 1 0; -fx-border-color: " + Constants.SECONDARY_COLOR + "; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");

        contactImageView = new ImageView();
        contactImageView.setFitHeight(100);
        contactImageView.setFitWidth(100);
        this.getChildren().add(contactImageView);

        detailsBox = new VBox(5);
        detailsBox.setAlignment(Pos.CENTER_LEFT);
        detailsBox.setPadding(new Insets(0, 0, 0, 10)); 

        contactNameLabel = new Label();
        contactNameLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        contactNameLabel.setMaxWidth(250);
        contactNameLabel.setEllipsisString("...");
        detailsBox.getChildren().add(contactNameLabel);

        contactPhoneLabel = new Label(); 
        detailsBox.getChildren().add(contactPhoneLabel);

        contactEmailLabel = new Label(); 
        detailsBox.getChildren().add(contactEmailLabel);

        this.getChildren().add(detailsBox);
        HBox buttonBox = new HBox(10); 
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(buttonBox, Priority.ALWAYS);

        editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        buttonBox.getChildren().add(editButton);

        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #DAE5EA; -fx-border-width: 0;");
        buttonBox.getChildren().add(deleteButton);

        this.getChildren().add(buttonBox);

        deleteButton.setOnAction(e -> {
            if (this.getParent() instanceof ContactList) {
                ContactList parentList = (ContactList) this.getParent();
                parentList.removeContact(this);
            }
        });

        editButton.setOnAction(e -> {
            AppFrame appFrame = (AppFrame) this.getScene().getRoot();
            ContactDetailsPage detailsPage = new ContactDetailsPage(appFrame, this);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(detailsPage);
        });
    }

    public void setContactName(String name) {
        this.contactNameLabel.setText(name);
    }

    public void setContactPhone(String phone) {
        this.contactPhoneLabel.setText("Phone: " + phone);
    }

    public void setContactEmail(String email) {
        this.contactEmailLabel.setText("Email: " + email);
    }

    public String getContactName() {
        return this.contactNameLabel.getText();
    }

    public String getContactEmail() {
        String emailText = this.contactEmailLabel.getText();
        return emailText.replace("Email: ", "");
    }

    public String getContactPhone() {
        String phoneText = this.contactPhoneLabel.getText();
        return phoneText.replace("Phone: ", "");
    }

    public Button getEditButton() {
        return this.editButton;
    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    public void setContactImage(Image image) {
        this.contactImageView.setImage(image);
    }

    public Image getContactImage() {
        return this.contactImageView.getImage();
    }
}


class ContactList extends VBox {
    ContactList() {
        this.setSpacing(5); 
        this.setPrefSize(500, 560);
        this.setStyle("-fx-background-color: white;");
    }

    public void removeContact(ContactItem contactItem) {
        this.getChildren().remove(contactItem);
    }

    public void saveContacts() {
        
    }

    public void sortContacts() {
        List<ContactItem> contacts = this.getChildren().stream()
            .filter(node -> node instanceof ContactItem)
            .map(node -> (ContactItem) node)
            .collect(Collectors.toList());

        List<String> lowercaseNames = contacts.stream()
            .map(contact -> contact.getContactName().toLowerCase())
            .collect(Collectors.toList());

        Collections.sort(lowercaseNames);

        List<ContactItem> sortedContacts = new ArrayList<>();
        for (String name : lowercaseNames) {
            for (ContactItem contact : contacts) {
                if (contact.getContactName().equalsIgnoreCase(name)) {
                    sortedContacts.add(contact);
                    contacts.remove(contact);
                    break;
                }
            }
        }
        this.getChildren().setAll(sortedContacts);
    }

    public void exportToCSV(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("Name,Email,Phone");
            for (Node node : this.getChildren()) {
                if (node instanceof ContactItem) {
                    ContactItem contact = (ContactItem) node;
                    writer.println(contact.getContactName() + "," + contact.getContactEmail() + "," + contact.getContactPhone());
                }
            }
        } catch (IOException ex) {
            System.err.println("Error writing to CSV: " + ex.getMessage());
        }
    }
}

class ContactDetailsPage extends VBox {
    private TextField nameField;
    private TextField emailField;
    private TextField phoneField;
    private ImageView contactImageView;
    private Image contactImage;
    private Button uploadButton;
    private Button doneButton;
    private Button backButton;
    private ContactItem currentContactItem;
    private AppFrame appFrame;
    private Label nameLabel;
    private Label emailLabel;
    private Label phoneLabel;

    public ContactDetailsPage(AppFrame appFrame) {
        this.appFrame = appFrame;
        this.setSpacing(10);
        this.setPadding(new Insets(10, 20, 10, 20));
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + ";");

        nameLabel = new Label("Name");
        styleLabels(nameLabel);
        emailLabel = new Label("Email");
        styleLabels(emailLabel);
        phoneLabel = new Label("Phone");
        styleLabels(phoneLabel);

        backButton = new Button("<-");
        backButton.setPrefSize(50, 30);
        backButton.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(appFrame);
        });
        styleBackButton(backButton);

        nameField = new TextField();
        nameField.setPromptText("Full Name");
        styleTextField(nameField);

        emailField = new TextField();
        emailField.setPromptText("Email Address");
        styleTextField(emailField);

        phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        styleTextField(phoneField);

        contactImageView = new ImageView();
        contactImageView.setFitHeight(150);
        contactImageView.setFitWidth(150);
        contactImageView.setStyle("-fx-border-color: #B0B0B0; -fx-border-radius: 5; -fx-background-color: white;");

        HBox imageBox = new HBox(contactImageView);
        imageBox.setAlignment(Pos.CENTER);

        uploadButton = new Button("Upload");
        uploadButton.setPrefWidth(100);
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            Stage stage = (Stage) ((Button) e.getSource()).getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                contactImage = new Image(selectedFile.toURI().toString());
                contactImageView.setImage(contactImage);
            }
        });
        styleUploadButton(uploadButton);

        doneButton = new Button("Save Contact");
        saveButton(doneButton);

        this.getChildren().addAll(backButton, nameLabel, nameField, emailLabel, emailField, phoneLabel, phoneField, imageBox, uploadButton, doneButton);
    }

    // Overloaded constructor for showing the detail page after clicking on "edit"
    public ContactDetailsPage(AppFrame appFrame, ContactItem contactItem) {
        this(appFrame); 
        currentContactItem = contactItem;
        if (currentContactItem != null) {
            nameField.setText(currentContactItem.getContactName());
            emailField.setText(currentContactItem.getContactEmail());
            phoneField.setText(currentContactItem.getContactPhone());
            contactImageView.setImage(currentContactItem.getContactImage());
        }
    }

    private void styleTextField(TextField textField) {
        textField.setPrefHeight(40);
        textField.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-radius: 5; -fx-border-color: #B0B0B0; -fx-padding: 5 10;");
    }

    private void styleBackButton(Button button) {
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-text-fill: " + Constants.PRIMARY_COLOR + "; -fx-border-color: " + Constants.PRIMARY_COLOR + "; -fx-border-width: 1px; -fx-border-radius: 5;"));
    }

    private void styleUploadButton(Button button) {
        button.setPrefHeight(30);
        button.setStyle("-fx-font-size: 14px; -fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-background-color: #A0A0A0; -fx-text-fill: black; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-border-radius: 5;"));
    }

    private void styleLabels(Label label) {
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + Constants.PRIMARY_COLOR + ";");
    }

    private void saveButton(Button button) {
        button.setPrefHeight(40);
        button.setPrefWidth(150);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.BUTTON_HOVER_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-background-color: " + Constants.PRIMARY_COLOR + "; -fx-text-fill: white; -fx-border-radius: 5;"));

        button.setOnAction(e -> {
            if (emailField.getText().trim().isEmpty() && phoneField.getText().trim().isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Incomplete Contact Details");
                alert.setContentText("Please fill in the email address or phone number to save a new contact!");
                alert.showAndWait();
                return;
            }

            String phoneNumber = phoneField.getText().trim();
            if (!phoneNumber.isEmpty() && !phoneNumber.matches("\\d+")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Invalid Phone Number");
                alert.setContentText("Phone Number must be numeric!");
                alert.showAndWait();
                return;
            }

            if (currentContactItem == null) {
                ContactItem contactItem = new ContactItem();
                contactItem.setContactName(nameField.getText());
                contactItem.setContactEmail(emailField.getText());
                contactItem.setContactPhone(phoneField.getText());
                contactItem.setContactImage(contactImage);

                appFrame.getContactList().getChildren().add(contactItem);
            } else {
                currentContactItem.setContactName(nameField.getText());
                currentContactItem.setContactEmail(emailField.getText());
                currentContactItem.setContactPhone(phoneField.getText());
                currentContactItem.setContactImage(contactImage);
            }

            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(new Scene(appFrame, 500, 600));
        });
    }
}

class Header extends VBox {

    private Button addButton;
    private Button sortButton;

    Header() {
        this.setPrefSize(500, 60);
        this.setStyle("-fx-background-color: white; -fx-alignment: center;");

        HBox upperRow = new HBox();
        upperRow.setAlignment(Pos.CENTER);

        String addButtonStyle = "-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px;";
        addButton = new Button("+");
        addButton.setStyle(addButtonStyle);
        addButton.setPrefSize(30, 30);
        upperRow.getChildren().add(addButton);
        HBox.setMargin(addButton, new Insets(0, 10, 0, 10));

        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        upperRow.getChildren().add(leftSpacer);

        Text titleText = new Text("Contact Manager");
        titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        upperRow.getChildren().add(titleText);

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        upperRow.getChildren().add(rightSpacer);

        sortButton = new Button("Sort");
        sortButton.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        sortButton.setPrefSize(40, 25); 
        sortButton.setOnAction(e -> {
            if (this.getParent() instanceof AppFrame) {
                AppFrame appFrame = (AppFrame) this.getParent();
                appFrame.getContactList().sortContacts();
            }
        });
        HBox.setMargin(sortButton, new Insets(0, 10, 0, 10));
        upperRow.getChildren().add(sortButton);

        this.getChildren().addAll(upperRow);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getSortButton() {
        return sortButton;
    }
}

class Footer extends HBox {
    private Button saveToCSVButton;

    Footer() {
        this.setPrefSize(500, 40);
        this.setStyle("-fx-background-color: " + Constants.SECONDARY_COLOR + "; -fx-alignment: center;");

        saveToCSVButton = new Button("Save as CSV Files");
        saveToCSVButton.setStyle("-fx-background-color: #B0B0B0; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 12px;");
        saveToCSVButton.setPrefSize(140, 40);

        saveToCSVButton.setOnAction(e -> {
            if (this.getParent() instanceof AppFrame) {
                AppFrame appFrame = (AppFrame) this.getParent();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Contacts");
                fileChooser.getExtensionFilters().add(
                    new ExtensionFilter("CSV Files", "*.csv")
                );
                File file = fileChooser.showSaveDialog(null);
                if (file != null) {
                    appFrame.getContactList().exportToCSV(file);
                }
            }
        });
        
        this.getChildren().add(saveToCSVButton);
    }

    public Button getSaveToCSVButton() {
        return saveToCSVButton;
    }
}


class AppFrame extends BorderPane {

    private Header header;
    private ContactList contactList;
    private Button addButton;
    private Footer footer;

    AppFrame() {
        this.setStyle("-fx-background-color: linear-gradient(to bottom, " + Constants.PRIMARY_COLOR + ", " + Constants.SECONDARY_COLOR + ");");
        header = new Header();
        contactList = new ContactList();
        
        this.setTop(header);

        ScrollPane scroller = new ScrollPane();
        scroller.setContent(contactList);
        scroller.setFitToWidth(true);
        scroller.setFitToHeight(true);
        this.setCenter(scroller);

        addButton = header.getAddButton();

        addListeners();

        footer = new Footer();
        this.setBottom(footer);
    }

    public void addListeners() {
        addButton.setOnAction(e -> {
            ContactDetailsPage detailsPage = new ContactDetailsPage(this);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.getScene().setRoot(detailsPage);
        });
    }

    public ContactList getContactList() {
        return contactList;
    }
}

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        AppFrame root = new AppFrame();
        primaryStage.setTitle("Contact Manager");
        primaryStage.setScene(new Scene(root, 500, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
