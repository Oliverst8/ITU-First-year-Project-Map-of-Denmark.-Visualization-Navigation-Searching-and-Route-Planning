package dk.itu.map;

import javafx.scene.layout.Region;

public class Controller {

    private final ViewBuilder viewBuilder;
    private Model model = new Model();

    public Controller() {
        viewBuilder = new ViewBuilder(model);
    }

    public Region getView() {
        return viewBuilder.build();
    }
}