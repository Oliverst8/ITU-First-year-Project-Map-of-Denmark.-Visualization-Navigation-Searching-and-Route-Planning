package dk.itu.map.fxml.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import dk.itu.map.App;
import dk.itu.map.fxml.Screen;
import dk.itu.map.fxml.models.HomeModel;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

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

    public void loadSavedMaps(MenuButton mapList, MenuButton deleteList) {
        File externalDirectoryPath = new File(App.DATA_PATH);

        String[] internalMaps = new String[]{"Isle"};
        String[] externapMaps = externalDirectoryPath.list();

        addMaps(mapList, internalMaps, "intrnal");
        addMaps(mapList, externapMaps, "external");

        addMaps(deleteList, externapMaps, "delete");
    }

    private void addMaps(MenuButton mapList, String[] maps, String type) {
        if (maps == null) return;

        for (String map : maps) {
            MenuItem item = new MenuItem(map);
            mapList.getItems().add(item);

            if(type.equalsIgnoreCase("delete")){
                item.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete: " + map);
                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get() == ButtonType.OK){
                        File fileToBeDeleted = new File("./maps/" + map);
                            System.out.println(fileToBeDeleted);
                        try {
                            FileUtils.deleteDirectory(fileToBeDeleted);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            } else{
                item.setOnAction(e -> {
                    mapList.hide();

                    App.setView(new Screen.Map(item.getText(), type));
                });
            }
        }
    }
}
