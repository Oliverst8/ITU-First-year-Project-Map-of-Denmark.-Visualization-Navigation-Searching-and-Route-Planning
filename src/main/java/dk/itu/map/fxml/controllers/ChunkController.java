package dk.itu.map.fxml.controllers;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.App;
import dk.itu.map.fxml.Screen;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.parser.OSMParser;

public class ChunkController {
    
    ChunkModel model;

    public ChunkController(ChunkModel model) {
        this.model = model;

        importMap(model.OSMFile, model.mapName);
    }

    
    /**
     * Imports a map from a file
     * @param filePath The path to the file
     * @param name The name of the map to be saved to
     */
    public void importMap(String filePath, String name) {
        try {            
            if (!new File("maps" + "/" + name + "/config").exists()) {
                OSMParser OSMParser = new OSMParser(new File(filePath), "maps" + "/" + name); // TODO: put on new thread
                OSMParser.load();
                System.out.println("Finished importing map!");
            };
            App.setView(new Screen.Map(name));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
