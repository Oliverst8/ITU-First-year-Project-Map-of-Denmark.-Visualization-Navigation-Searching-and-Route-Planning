package dk.itu.map;

import dk.itu.map.ViewBuilder.Views;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    // The stage is the window that the application is running in
    private final Stage stage;
    // The model is the data that the application is working with
    private final Model model;
    // The view builder is responsible for creating the view
    private final ViewBuilder viewBuilder;

    /**
     * Constructor for the controller
     * Initializes the stage, model and view builder
     * Sets the view to the home view
     * @param stage The stage that the application is running in
     */
    public Controller(Stage stage) {

        this.stage = stage;
        this.model = new Model();
        this.viewBuilder = new ViewBuilder(this, this.model, "home");

        setView(Views.Home);
    }

    /**
     * Sets the view of the application
     * @param view The view to set
     */
    public void setView(Views view) {
        viewBuilder.setView(view);
        stage.setScene(new Scene(viewBuilder.build()));
    }
}