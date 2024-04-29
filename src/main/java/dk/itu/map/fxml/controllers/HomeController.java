package dk.itu.map.fxml.controllers;

import java.io.File;

import dk.itu.map.App;
import dk.itu.map.fxml.Screen;
import dk.itu.map.fxml.models.HomeModel;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeController {

    private final HomeModel model;

    
    public HomeController(HomeModel model) {
        this.model = model;
    }


    public void importMap() {
        File selectedFile = getMapFile();
        if (selectedFile == null) return;

        String mapName = getMapName();

        App.setView(new Screen.Chunker(selectedFile.getAbsolutePath(), mapName));
    }

    protected File getMapFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Map File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OSM", "*.osm"),
                new FileChooser.ExtensionFilter("BZ2", "*.bz2"));

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

    public void loadSavedMaps(MenuButton mapList) {
        File directoryPath = new File(model.getDataPath());

        String[] maps = directoryPath.list();

        // If the directory is empty, return.
        if (maps == null) return;

        for (String map : maps) {
            MenuItem item = new MenuItem(map);
            mapList.getItems().add(item);

            item.setOnAction(e -> {
                mapList.hide();

                App.setView(new Screen.Map(item.getText()));
            });
        }
    }

}
