package dk.itu.map.fxml;

import java.io.File;

import dk.itu.map.App;
import dk.itu.map.Model;
import dk.itu.map.fxml.parent.ChunkScreen;
import dk.itu.map.fxml.parent.MapScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController extends Controller {

    public HomeController(Model viewModel) {
        super(viewModel);
    }

    @FXML
    public void initialize() {
        loadMaps();
    }

    @FXML
    void importMap(ActionEvent event) {
        File selectedFile = getMapFile();
        if (selectedFile == null) return;

        String mapName = getMapName();

        App.setView(new ChunkScreen(selectedFile.getAbsolutePath(), mapName));
    }

    protected File getMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OSM", "*.osm"));

        Stage dialog = new Stage();
        return fileChooser.showOpenDialog(dialog);
    }

    protected String getMapName() {
        TextInputDialog dialog = new TextInputDialog("Map Name");
        dialog.setTitle("Map Name");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Map Name:");
        dialog.resizableProperty().setValue(false);

        return dialog.showAndWait().get();
    }

}
