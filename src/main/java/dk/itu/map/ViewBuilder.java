package dk.itu.map;

import java.net.URL;

import dk.itu.map.fxml.HomeController;

import java.io.IOException;

import javafx.util.Builder;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;

public class ViewBuilder implements Builder<Region> {
    private final Model viewModel;

    public ViewBuilder(Model viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public Region build() {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/scenes/home.fxml"));
    
        try {
            return loader.load();
        } catch (IOException e) {
            System.out.println("Error loading fxml");
            e.printStackTrace();

            return new BorderPane();
        }
    }
}
