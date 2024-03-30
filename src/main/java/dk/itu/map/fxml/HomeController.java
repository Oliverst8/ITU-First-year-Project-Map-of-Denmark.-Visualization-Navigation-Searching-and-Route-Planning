package dk.itu.map.fxml;

import dk.itu.map.Model;

import java.io.File;

import dk.itu.map.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.ActionEvent;

public class HomeController {
    private final Model viewModel;
    private final Controller controller;

    @FXML
    private MenuButton mapList;

    public HomeController(Controller controller, Model viewModel) {
        this.viewModel = viewModel;
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        loadMaps();
    }

    @FXML
    void importClicked(ActionEvent event) {
        File selectedFile = getMapFile();
        if (selectedFile == null) return;

        String mapName = getMapName();

        viewModel.importMap(selectedFile.getAbsolutePath(), mapName);
        controller.setView("map");
    }

    private void loadMaps() {
        File directoryPath = new File("maps/");

        String maps[] = directoryPath.list();

        // If the directory is empty, return.
        if (maps == null) return;

        for(int i=0; i < maps.length; i++) {
            MenuItem item = new MenuItem(maps[i]);
            mapList.getItems().add(item);

            item.setOnAction(e -> {
                viewModel.importMap("maps/" + item.getText(), item.getText());
                controller.setView("map");
            });
        }
    }

    private File getMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Map File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("OSM", "*.osm"));
            
        Stage dialog = new Stage();
        return fileChooser.showOpenDialog(dialog);
    }

    private String getMapName() {
        TextInputDialog dialog = new TextInputDialog("Map Name");
        dialog.setTitle("Map Name");
        dialog.setHeaderText(null);
        dialog.setGraphic(null);
        dialog.setContentText("Map Name:");
        dialog.resizableProperty().setValue(false);

        return dialog.showAndWait().get();
    }
}
