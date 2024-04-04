package dk.itu.map.fxml;

import dk.itu.map.Controller;
import dk.itu.map.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

abstract class ViewController {

    @FXML
    private MenuButton mapList;
    protected final Model viewModel;
    protected final Controller controller;


    public ViewController(Controller controller, Model viewModel) {
        this.viewModel = viewModel;
        this.controller = controller;
    }



    @FXML
    void importMap(ActionEvent event) {
        File selectedFile = getMapFile();
        if (selectedFile == null) return;

        String mapName = getMapName();

        viewModel.importMap(selectedFile.getAbsolutePath(), mapName);
        controller.setView("map");
    }

    protected void loadMaps() {
        File directoryPath = new File("maps/");

        String[] maps = directoryPath.list();

        // If the directory is empty, return.
        if (maps == null) return;

        for (String map : maps) {
            MenuItem item = new MenuItem(map);
            mapList.getItems().add(item);

            item.setOnAction(e -> {
                viewModel.importMap("maps/" + item.getText(), item.getText());
                controller.setView("map");
            });
        }
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
