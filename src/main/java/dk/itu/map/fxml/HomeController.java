package dk.itu.map.fxml;

import dk.itu.map.Model;

import javafx.fxml.FXML;

public class HomeController extends Controller {

    public HomeController(Model viewModel) {
        super(viewModel);
    }

    @FXML
    public void initialize(){
        loadMaps();
    }

}
