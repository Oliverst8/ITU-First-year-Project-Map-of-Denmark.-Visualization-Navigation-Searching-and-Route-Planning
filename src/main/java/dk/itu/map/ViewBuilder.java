package dk.itu.map;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javafx.util.Builder;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;

public class ViewBuilder implements Builder<Region> {
    private final Controller controller;
    private final Model viewModel;
    private String view;

    public ViewBuilder(Controller controller, Model viewModel, String initialView) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.view = initialView;
    }

    @Override
    public Region build() {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/scenes/" + view + ".fxml"));

        // Set the controller based on the viewname
        try {
            String controllerName = view.substring(0, 1).toUpperCase() + view.substring(1) + "Controller";

            Class<?> clazz = Class.forName("dk.itu.map.fxml." + controllerName);
            Constructor<?> constructor = clazz.getConstructor(Controller.class, Model.class);
            Object instance = constructor.newInstance(controller, viewModel);

            loader.setController(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return loader.load();
        } catch (IOException e) {
            System.out.println("Error loading fxml");
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    public void setView(String view) {
        this.view = view;
    }
}
