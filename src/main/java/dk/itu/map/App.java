package dk.itu.map;

import java.io.File;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.parser.FileHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (!new File("zoomLayers/config").exists()) {
            //Isle of man relative path: data/isle-of-man-latest2.osm
            FileHandler fileHandler = new FileHandler(new File("/home/ostarup/Downloads/denmark-latest.osm"));
            fileHandler.load();
        } else{
            System.out.println("File already exists. loading...");
        }


        ChunkHandler chunkHandler = new ChunkHandler("zoomLayers");
        Model model = new Model(chunkHandler);
        var view = new View(model, primaryStage);
        System.out.println("View created.");
        new Controller(model, view);
        System.out.println("Controller created.");
    }
}
