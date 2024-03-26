package dk.itu.map;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(new Controller().getView());
        
        stage.setTitle("MapIT");
        stage.setScene(scene);
        stage.show();
    }

    /* 
     * Need to merge:
     * 
     * public void start(Stage primaryStage) throws Exception {

        if (!new File("zoomLayers/config").exists()) {
            FileHandler fileHandler = new FileHandler(new File("data/isle-of-man-latest2.osm"));
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
     */
}
