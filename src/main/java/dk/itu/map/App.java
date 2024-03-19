package dk.itu.map;

import java.io.File;

import dk.itu.map.parser.FileHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // if (!new File("zoomLayers/config").exists()) {
            FileHandler fileHandler = new FileHandler(new File("C:\\Users\\augus\\Downloads\\isle-of-man-latest.osm"));
        // }
        //fileHandler.load();

        Model model = new Model(fileHandler);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }
}
