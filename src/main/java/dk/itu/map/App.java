package dk.itu.map;

import java.io.File;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FileHandler fileHandler = new FileHandler(new File("/home/ostarup/github/itu/itu_semester_2/First-year Project Map of Denmark. Visualization, Navigation, Searching, and Route Planning/Handins/handinthree/data/small.osm"));
        //fileHandler.load();

        Model model = new Model(fileHandler);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }
}
