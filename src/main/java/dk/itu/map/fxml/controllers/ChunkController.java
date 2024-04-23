package dk.itu.map.fxml.controllers;

import java.io.File;

import dk.itu.map.App;
import dk.itu.map.fxml.Screen;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.parser.OSMParser;

public class ChunkController {
    
    ChunkModel model;
    private String name;

    public ChunkController(ChunkModel model, String name) {
        this.model = model;
        App.mapPath = App.dataPath + name + "/";
        this.name = name;
    }

    
    /**
     * Imports a map from a file
     * @param filePath The path to the file
     * @param name The name of the map to be saved to
     */
    public void importMap(String filePath) {
        if (!new File(App.mapPath + "/config").exists()) {
            OSMParser parser = new OSMParser(new File(filePath));
            parser.setCallback((Runnable)() -> {
                System.out.println("Finished importing map!");
                App.setView(new Screen.Map(name));
            });
            parser.start();
        };
    }
}
