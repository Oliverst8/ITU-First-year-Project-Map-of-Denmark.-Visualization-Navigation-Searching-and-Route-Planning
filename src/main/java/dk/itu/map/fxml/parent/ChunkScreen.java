package dk.itu.map.fxml.parent;

import dk.itu.map.fxml.controllers.ChunkController;
import dk.itu.map.fxml.models.ChunkModel;

public class ChunkScreen extends Screen<ChunkModel> {
    
    public ChunkScreen(String OSMFile, String mapName) {
        this.fxml = "chunking.fxml";
        this.model = new ChunkModel(OSMFile, mapName);
        this.controller = new ChunkController(model);
        // this.view = new ChunkScreen();
    }
}
