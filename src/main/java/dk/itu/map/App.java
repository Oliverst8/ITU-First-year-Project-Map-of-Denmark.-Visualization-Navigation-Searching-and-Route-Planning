package dk.itu.map;

import dk.itu.map.controller.MapController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MapIT");

        Model model = new Model();
        View view = new View(model, primaryStage);
        new MapController(model, view);
    }
}
