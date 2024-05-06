package dk.itu.map.fxml.controllers;

import java.io.File;

import dk.itu.map.App;
import dk.itu.map.fxml.Screen;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.parser.FileProgress;
import dk.itu.map.parser.OSMParser;

public class ChunkController {
    ChunkModel model;
    private String name;

    /**
     * Creates a new ChunkController
     * @param model The model to be used
     * @param name The name of the map
     */
    public ChunkController(ChunkModel model, String name) {
        this.model = model;
        App.mapName = App.DATA_PATH + name + "/";
        this.name = name;
    }

    /**
     * Imports a map from a file
     * @param filePath The path to the file
     * @param name The name of the map to be saved to
     */
    public void importMap(String filePath, FileProgress fileProgress) {
        if (!new File(App.mapName + "/config").exists()) {
            File file = new File(filePath);
            OSMParser parser = new OSMParser(file, fileProgress);
            parser.setCallback((Runnable)() -> {
                System.out.println("Finished importing map!");
                App.setView(new Screen.Map(name, "external"));
            });
            parser.start();
        };
    }
}
