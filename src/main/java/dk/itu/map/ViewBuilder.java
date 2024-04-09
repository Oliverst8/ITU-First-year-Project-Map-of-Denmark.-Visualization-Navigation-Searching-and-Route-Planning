package dk.itu.map;

import dk.itu.map.fxml.parent.Screen;
import javafx.util.Builder;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

public class ViewBuilder implements Builder<Region> {
    private Screen view;

    // public enum Views {
    //     Home,
    //     Map;
    // }

    /**
     * The view builder is responsible for building the views, this includes loading the FXML files and setting the controller.
     */
    public ViewBuilder() { }

    @Override
    public Region build() {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/scenes/" + view.fxml));

        // Set the controller based on the viewname
        try {
            // String controllerName = view.substring(0, 1).toUpperCase() + view.substring(1) + "Controller";

            // Class<?> clazz = Class.forName("dk.itu.map.fxml." + controllerName);

            loader.setController(view.controller);

            return loader.load();
        } catch (Exception e) {
            // Improved logging should be implemented
            System.out.println("Could not load view: " + view);
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    /**
     * Set the view to be displayed
     * @param view The view to be displayed
     */
    public void setView(Screen view) {
        this.view = view;
    }
}
