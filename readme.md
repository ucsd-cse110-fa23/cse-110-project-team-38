# Team 38 PantryPal

## How to run!

### 1:

Make sure to include javafx in your launch.json using "vmArgs": "--module-path '' --add-modules javafx.controls,javafx.fxml"

replace <PATH> with `lib/openjfx-21_windows-x64_bin-sdk/javafx-sdk-21/lib` OR the absolute path

### 2: 

Run Main.java in PantryPal/client to start the app!

If you want to see the HTTP server saves, make sure to also start MyServer.java in PantryPal/Server, or else hitting save will cause HTTP connection errors (and nothing else)

### 3:

Make a empty folder call "images" on the root directory, this step is essential for generating images.

### 4:

Run the server first, Go to <PATH> `\src\main\java\PantryPal\server\MyServer.java` and run MyServer.java. Then go to <PATH> `\src\main\java\PantryPal\client\Main.java` and run Main.java to run the APP.

Link to repo: https://github.com/ucsd-cse110-fa23/cse-110-project-team-38
