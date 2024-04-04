package dk.itu.map;

import dk.itu.map.ViewBuilder.Views;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    private final ViewBuilder viewBuilder;
    private Model model = new Model();
    private Stage stage;

    public Controller(Stage stage) {
        viewBuilder = new ViewBuilder(this, model, "home");
        this.stage = stage;

        setView(Views.Home);
    }

    public void setView(Views view) {
        viewBuilder.setView(view);
        stage.setScene(new Scene(viewBuilder.build()));
    }
}