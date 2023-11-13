# Team 38 PantryPal

## How to run!

### 1:

Make sure to include javafx in your launch.json using "vmArgs": "--module-path '' --add-modules javafx.controls,javafx.fxml"

replace <PATH> with `lib/openjfx-21_windows-x64_bin-sdk/javafx-sdk-21/lib` OR the absolute path

### 2: 

Run Main.java in PantryPal/client to start the app!

If you want to see the HTTP server saves, make sure to also start MyServer.java in PantryPal/Server, or else hitting save will cause HTTP connection errors (and nothing else)
