package dk.itu.map.fxml.parent;

import dk.itu.map.Model;
import dk.itu.map.fxml.HomeController;

public class HomeScreen extends Screen {
    
    public HomeScreen() {
        this.fxml = "home.fxml";
        this.model = new Model();
        this.controller = new HomeController(model);
        // this.view = new HomeScreen();

    }
}
