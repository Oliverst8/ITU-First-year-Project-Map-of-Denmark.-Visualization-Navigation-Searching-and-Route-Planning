package itu.map;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

            FileHandler fileHandler = new FileHandler(new File("/home/jogge/Downloads/isle-of-man-latest.osm.bz2"));
            //fileHandler.load();

        Model model = new Model(fileHandler);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }
}
