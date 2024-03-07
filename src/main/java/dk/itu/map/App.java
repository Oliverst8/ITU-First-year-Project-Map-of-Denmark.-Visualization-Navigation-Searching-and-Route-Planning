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
        FileHandler fileHandler = new FileHandler(new File("C:\\Users\\nickl\\Downloads\\isle-of-man-latest.osm.bz2"));
        //fileHandler.load();

        Model model = new Model(fileHandler);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }
}
