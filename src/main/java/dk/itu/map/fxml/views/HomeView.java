package dk.itu.map.fxml.views;

import dk.itu.map.fxml.controllers.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

public class HomeView {
    
    @FXML
    private MenuButton mapList;
    private final HomeController controller;
    
    public HomeView(HomeController controller) {
        this.controller = controller;
    }
    
    @FXML
    public void initialize() {
        controller.loadSavedMaps(mapList);
    }

    @FXML
    void importMap(ActionEvent event) {
        controller.importMap();
    }
}
