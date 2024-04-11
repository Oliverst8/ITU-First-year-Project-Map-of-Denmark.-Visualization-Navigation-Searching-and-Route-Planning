package dk.itu.map.fxml;

import dk.itu.map.fxml.controllers.ChunkController;
import dk.itu.map.fxml.controllers.HomeController;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.fxml.models.HomeModel;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.views.HomeView;

public abstract class Screen<M, C> {
    public String fxml;
    public M model; // state
    public C controller; // functions
    public Object view; // drawing



    public static class Home extends Screen<HomeModel, HomeController> {
        
        public Home() {
            this.fxml = "home.fxml";
            this.model = new HomeModel();
            this.controller = new HomeController(model);
            this.view = new HomeView(controller);

        }
    }

    public static class Map extends Screen<MapModel, MapController> {
    
        public Map(String mapName) {
            this.fxml = "map.fxml";
            this.model = new MapModel();
            model.importMap("", mapName);
            this.controller = new MapController(model);
            // this.view = new MapScreen();
    
        }
    }

    public static class Parse extends Screen<ChunkModel, ChunkController> {
        
        public Parse(String OSMFile, String mapName) {
            this.fxml = "chunking.fxml";
            this.model = new ChunkModel(OSMFile, mapName);
            this.controller = new ChunkController(model);
            // this.view = new ChunkScreen();
        }
    }    
}
