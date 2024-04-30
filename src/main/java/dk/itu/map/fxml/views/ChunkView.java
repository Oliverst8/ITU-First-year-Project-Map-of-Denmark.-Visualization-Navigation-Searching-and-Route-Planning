package dk.itu.map.fxml.views;

import dk.itu.map.fxml.controllers.ChunkController;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.parser.FileProgress;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class ChunkView {

    ChunkController controller;
    ChunkModel model;
    @FXML
    public ProgressBar chunkingProgressbar;

    public ChunkView(ChunkController controller, ChunkModel model) {
        this.controller = controller;
        this.model = model;
    }
    
    @FXML
    public void initialize() {
        FileProgress fileProgress = new FileProgress(chunkingProgressbar);
        controller.importMap(model.OSMFile, fileProgress);
    }
}
