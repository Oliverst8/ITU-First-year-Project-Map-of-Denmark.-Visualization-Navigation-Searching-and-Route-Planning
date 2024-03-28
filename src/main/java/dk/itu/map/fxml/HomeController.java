package dk.itu.map.fxml;

import dk.itu.map.Model;
import dk.itu.map.Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class HomeController {
    private final Controller controller;

    public HomeController(Controller controller, Model viewModel) {
        this.controller = controller;
    }

    @FXML
    void importClicked(ActionEvent event) {
        controller.setView("map");
    }
}
