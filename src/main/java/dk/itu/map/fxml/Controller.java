package dk.itu.map.fxml;

import dk.itu.map.App;
import dk.itu.map.Model;
import dk.itu.map.fxml.parent.MapScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;

public abstract class Controller {

    @FXML
    private MenuButton mapList;
    protected final Model viewModel;


    public Controller(Model viewModel) {
        this.viewModel = viewModel;
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
                App.setView(new MapScreen(""));
            });
        }
    }
}
