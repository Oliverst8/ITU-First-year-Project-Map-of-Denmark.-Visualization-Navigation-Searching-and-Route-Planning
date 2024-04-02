package dk.itu.map;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {

    private final ViewBuilder viewBuilder;
    private Model model = new Model();
    private Stage stage;

    public Controller(Stage stage) {
        viewBuilder = new ViewBuilder(this, model, "home");
        this.stage = stage;

        setView("home");
    }

    public void setView(String view) {
        viewBuilder.setView(view);
        stage.setScene(new Scene(viewBuilder.build()));
    }
}