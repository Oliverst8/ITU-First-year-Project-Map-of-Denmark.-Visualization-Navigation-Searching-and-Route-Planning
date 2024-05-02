package dk.itu.map.fxml;

import dk.itu.map.fxml.controllers.ChunkController;
import dk.itu.map.fxml.controllers.HomeController;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.ChunkModel;
import dk.itu.map.fxml.models.HomeModel;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.views.ChunkView;
import dk.itu.map.fxml.views.HomeView;
import dk.itu.map.fxml.views.MapView;

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

        public Map(String mapName, String mapType) {
            this.fxml = "map.fxml";
            this.model = new MapModel(mapType);
            this.controller = new MapController(model);
            controller.importMap("", mapName);
            this.view = new MapView(controller, model);
            controller.setView((MapView)view);
        }
    }

    public static class Chunker extends Screen<ChunkModel, ChunkController> {

        public Chunker(String OSMFile, String mapName) {
            this.fxml = "chunking.fxml";
            this.model = new ChunkModel(OSMFile);
            this.controller = new ChunkController(model, mapName);
            this.view = new ChunkView(controller, model);
        }
    }
}
