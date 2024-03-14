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
        if (!new File("chunkData/config").exists()) {
            FileHandler fileHandler = new FileHandler(new File("data/isle-of-man-latest2.osm"));
            fileHandler.load();
        }

        ChunkHandler chunkHandler = new ChunkHandler("chunkData");
        Model model = new Model(chunkHandler);
        var view = new View(model, primaryStage);
        new Controller(model, view);
    }
}
