package dk.itu.map.fxml.views;

import dk.itu.map.fxml.controllers.MapController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Affine;

public class MapView {
    
    // JavaFX canvas
    @FXML
    private Canvas canvas;
    private MapController controller;
    
    // Last mouse position
    private float lastX;
    // Last mouse position
    private float lastY;

    public MapView(MapController controller) {
        this.controller = controller;
    }

    /**
     * Initializes the graphics context, and set rules for drawing
     * Sets up start zoom, and pans to start location
     * Draws the map
     * Sets up event listeners for panning, zooming and scrolling
     */
    @FXML
    public void initialize() {
        controller.setup(canvas);

        canvas.setOnMousePressed(e -> {
            lastX = (float)e.getX();
            lastY = (float)e.getY();
        });

        canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                controller.pan(dx, dy);
            }

            lastX = (float)e.getX();
            lastY = (float)e.getY();
        });

        canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            controller.zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });
    }
    
    @FXML
    void quitApplication(ActionEvent event) {
        Platform.exit();
    }

    // @FXML
    // void openRecent(ActionEvent event){
    //     loadMaps();
    // }
}
