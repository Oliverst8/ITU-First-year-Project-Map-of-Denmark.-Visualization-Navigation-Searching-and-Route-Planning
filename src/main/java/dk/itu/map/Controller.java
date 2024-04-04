package dk.itu.map;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    private final Stage stage;
    private final Model model;
    private final ViewBuilder viewBuilder;


    public Controller(Stage stage) {

        this.stage = stage;
        this.model = new Model();
        this.viewBuilder = new ViewBuilder(this, this.model, "home");

        setView("home");
    }

    public void setView(String view) {
        viewBuilder.setView(view);
        stage.setScene(new Scene(viewBuilder.build()));
    }
}