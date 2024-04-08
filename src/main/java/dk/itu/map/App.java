package dk.itu.map;

import javafx.stage.Stage;
import javafx.application.Application;

public class App extends Application {

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
        new Controller(stage);
        
        stage.setTitle("MapIT");
        stage.show();
    }
}
