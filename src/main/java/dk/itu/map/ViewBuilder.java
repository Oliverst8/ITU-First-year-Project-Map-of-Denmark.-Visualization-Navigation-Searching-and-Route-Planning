package dk.itu.map;

import javafx.util.Builder;
import dk.itu.map.fxml.Screen;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;

public class ViewBuilder implements Builder<Region> {
    private Screen<?, ?> view;

    /**
     * The view builder is responsible for building the views, this includes loading the FXML files and setting the controller.
     */
    public ViewBuilder() { }

    @Override
    public Region build() {
        FXMLLoader loader = new FXMLLoader();

        if (getClass().getResource("/scenes/" + view.fxml) == null) {
            throw new RuntimeException("Could not find view: " + view.fxml);
        }

        loader.setLocation(getClass().getResource("/scenes/" + view.fxml));

        try {

            loader.setController(view.view);

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
    public void setView(Screen<?, ?> view) {
        this.view = view;
    }
}
