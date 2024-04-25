package dk.itu.map.fxml.views;

import dk.itu.map.fxml.controllers.ChunkController;
import dk.itu.map.fxml.models.ChunkModel;
import javafx.fxml.FXML;

public class ChunkView {

    ChunkController controller;
    ChunkModel model;

    public ChunkView(ChunkController controller, ChunkModel model) {
        this.controller = controller;
        this.model = model;
    }
    
    @FXML
    public void initialize() {
        controller.importMap(model.OSMFile, model.mapName);
        
    }
}
