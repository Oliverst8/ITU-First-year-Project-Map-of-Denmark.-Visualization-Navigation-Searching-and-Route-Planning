package dk.itu.map;

import java.lang.reflect.Constructor;

import javafx.util.Builder;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;

public class ViewBuilder implements Builder<Region> {
    private final Controller controller;
    private final Model viewModel;
    private String view;

    public enum Views {
        Home,
        Map;
    }

    /**
     * The view builder is responsible for building the views, this includes loading the FXML files and setting the controller.
     * @param controller The controller of the application
     * @param viewModel The view model of the application
     * @param initialView The initial view to be displayed
     */
    public ViewBuilder(Controller controller, Model viewModel, String initialView) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.view = initialView;
    }

    @Override
    public Region build() {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/scenes/" + view.toLowerCase() + ".fxml"));

        // Set the controller based on the viewname
        try {
            String controllerName = view.substring(0, 1).toUpperCase() + view.substring(1) + "Controller";

            Class<?> clazz = Class.forName("dk.itu.map.fxml." + controllerName);
            Constructor<?> constructor = clazz.getConstructor(Controller.class, Model.class);
            Object instance = constructor.newInstance(controller, viewModel);

            loader.setController(instance);

            return loader.load();
        } catch (Exception e) {
            // Improved logging should be implemented
            System.out.println("Could not load view: " + view);
            e.printStackTrace();

            return new BorderPane();
        }
    }

    /**
     * Set the view to be displayed
     * @param view The view to be displayed
     */
    public void setView(Views view) {
        this.view = view.name();
    }
}
