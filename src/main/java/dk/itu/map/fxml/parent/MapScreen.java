package dk.itu.map.fxml.parent;

import dk.itu.map.Model;
import dk.itu.map.fxml.MapController;

public class MapScreen extends Screen<Model> {
    
    public MapScreen(String mapName) {
        this.fxml = "map.fxml";
        this.model = new Model();
        model.importMap("", mapName);
        this.controller = new MapController(model);
        // this.view = new MapScreen();

    }
}
