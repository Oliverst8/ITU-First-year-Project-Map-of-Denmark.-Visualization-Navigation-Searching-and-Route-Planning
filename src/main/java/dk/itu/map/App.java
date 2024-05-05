package dk.itu.map;

import javafx.stage.Stage;
import dk.itu.map.fxml.Screen;
import javafx.application.Application;
import javafx.scene.Scene;

public class App extends Application {
    static public String DATA_PATH = "maps/";
    static public String mapPath;
    // The stage is the window that the application is running in
    private static Stage stage;
    // The view builder is responsible for creating the view
    private static ViewBuilder viewBuilder;

    /**
     * Main method to start the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start method to start the application
     * @param stage the stage to start the application
     * @throws Exception if an exception occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        if (App.stage != null) return;
        App.stage = stage;
        App.viewBuilder = new ViewBuilder();

        setView(new Screen.Home());
        
        stage.setTitle("MapIT");
        stage.show();
    }

    /**
     * Sets the view of the application
     * @param view The view to set
     */
    public static void setView(Screen<?, ?> view) {
        viewBuilder.setView(view);
        stage.setScene(new Scene(viewBuilder.build()));
    }
}
