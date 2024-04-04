package dk.itu.map.fxml;

import dk.itu.map.Model;
import dk.itu.map.Controller;
import dk.itu.map.structures.Way;


import java.util.Map;
import java.util.List;

import dk.itu.map.utility.Navigation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;


public class MapController extends ViewController {

    //JavaFX canvas
    @FXML
    private Canvas canvas;

    //GraphicsContext for drawing on the canvas
    private GraphicsContext gc;
    //Affine transformation for panning and zooming
    private Affine trans;
    //Zoom level
    private float zoomLevel;
    //Initial distance between two points
    private float startDist;

    //Last mouse position
    private float lastX;
    //Last mouse position
    private float lastY;
    //Amount of chunks seen
    private float currentChunkAmountSeen = 1;

    /**
     * Constructor for the MapController, set the following variables
     * @param controller
     * @param viewModel
     */
    public MapController(Controller controller, Model viewModel) {
        super(controller, viewModel);
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

        trans.prependTranslation(-0.56*this.viewModel.getMinLon(), this.viewModel.getMaxLat()); //Calling the code of pan, to prevent redraw before zoom has been run
        //This is done to avoid getheight and getwidth from canvas, returning way to big values
        zoom(0, 0, canvas.getHeight() / (this.viewModel.getMaxLat() - this.viewModel.getMinLat()));

        startDist = getZoomDistance();
        redraw();

        canvas.setOnMousePressed(e -> {
            lastX = (float) e.getX();
            lastY = (float) e.getY();
            Navigation navigation = new Navigation(this.viewModel.getGraph());
            Way path = navigation.getPath(814157l,2395042472l); //this works
            System.out.println(path);
            path.draw(gc, getZoomDistance()/startDist*100);
        });

        canvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                pan(dx, dy);
            }

            lastX = (float) e.getX();
            lastY = (float) e.getY();
        });

        canvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });
    }

    /**
     * @return Point2D the upper left corner of the canvas
     */
    private Point2D getUpperLeftCorner() {
        return convertTo2DPoint(0, 0);
    }

    /**
     * @return Point2D the lower right corner of the canvas
     */
    private Point2D getLowerRightCorner() {
        return convertTo2DPoint(canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Pans the map
     * @param dx the upper left X corner to be panned to
     * @param dy the upper left Y corner to be panned to
     */
    private void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * Redraws the map
     */
    private void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);

        //If you remove the first updateZoomLevel it takes double the amount of time to load the chunks, we dont know why (mvh August & Oliver)
        updateZoomLevel();
        currentChunkAmountSeen = this.viewModel.updateChunks(getDetailLevel(), getUpperLeftCorner(), getLowerRightCorner());
        updateZoomLevel();

        for(int i = getDetailLevel(); i <= 4; i++){
            Map<Integer, List<Way>> chunkLayer = viewModel.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                for(int j = 0; j < chunkLayer.get(chunk).size(); j++){
                    chunkLayer.get(chunk).get(j).draw(gc, getZoomDistance()/ startDist *100);
                }
            }
        }
    }

    /**
     * @return int the detail level of the map
     */
    private int getDetailLevel(){
        if(zoomLevel > 55000) return 4;
        if(zoomLevel > 2300) return 3;
        if(zoomLevel > 115) return 2;
        if(zoomLevel > 10)return 1;
        return 0;
    }

    /**
     * Zooms the map
     * @param dx the upper left X corner to be zoomed to
     * @param dy the upper left Y corner to be zoomed to
     * @param factor the factor to zoom by
     */
    private void zoom(double dx, double dy, double factor) {
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * @return float the current distance between the two points (0,0) & (0,100)
     */
    private float getZoomDistance(){
        Point2D p1 = convertTo2DPoint(0,0);
        Point2D p2 = convertTo2DPoint(0,100);
        return (float) p1.distance(p2);
    }

    /**
     * Updates the zoom level
     */
    private void updateZoomLevel(){
        float newZoom = getZoomDistance();
        zoomLevel = (newZoom/ startDist) * 100 * currentChunkAmountSeen * viewModel.getChunkAmount();
    }

    /**
     * Converts from canvas to JavaFx 2D point
     * @param x the x coordinate
     * @param y the y coordinate
     * @return Point2D the converted point
     */
    private Point2D convertTo2DPoint(double x, double y) {
        try {
            return trans.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void quitApplication(ActionEvent event){
        Platform.exit();
    }

    @FXML
    void openRecent(ActionEvent event){
        loadMaps();
    }

}
