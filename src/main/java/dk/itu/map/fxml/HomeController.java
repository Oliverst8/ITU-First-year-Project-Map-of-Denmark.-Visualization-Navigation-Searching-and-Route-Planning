package dk.itu.map.fxml;

import dk.itu.map.Model;

import dk.itu.map.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

public class HomeController extends ViewController{

    public HomeController(Controller controller, Model viewModel) {
        super(controller, viewModel);
    }

    @FXML
    public void initialize(){
        loadMaps();
    }

}
