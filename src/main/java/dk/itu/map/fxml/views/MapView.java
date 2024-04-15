package dk.itu.map.fxml.views;

import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.MapModel;
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
    // GraphicsContext for drawing on the canvas
    private GraphicsContext gc;
    // Affine transformation for panning and zooming
    private Affine trans;
    private MapController controller;
    private MapModel model;

    // Last mouse position
    private float lastX;
    // Last mouse position
    private float lastY;

    public MapView(MapController controller, MapModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * Initializes the graphics context, and set rules for drawing
     * Sets up start zoom, and pans to start location
     * Draws the map
     * Sets up event listeners for panning, zooming and scrolling
     */
    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        trans = new Affine();

        trans.prependTranslation(-0.56 * this.model.getMinLon(), this.model.getMaxLat());
        // Calling the code of pan, to prevent redraw before zoom has been run
        // This is done to avoid getheight and getwidth from canvas, returning way to
        // big values
        zoom(0, 0, canvas.getHeight() / (this.model.getMaxLat() - this.model.getMinLat()));

        startDist = getZoomDistance();
        redraw();

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

    @FXML
    void openRecent(ActionEvent event){
        // loadMaps();
    }

    @FXML
    void importMap(ActionEvent event) {
    }
}
