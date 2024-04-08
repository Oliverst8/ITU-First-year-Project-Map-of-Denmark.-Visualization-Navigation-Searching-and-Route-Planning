package dk.itu.map.fxml;

import dk.itu.map.Model;
import dk.itu.map.Controller;
import dk.itu.map.structures.Way;
import dk.itu.map.task.CanvasRedrawTask;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class MapController extends ViewController {

    //JavaFX canvas
    @FXML
    private Canvas canvasBuilding;
    @FXML
    private Canvas canvasHighway;
    @FXML
    private Canvas canvasAmenity;
    @FXML
    private Canvas canvasLeisure;
    @FXML
    private Canvas canvasAeroway;
    @FXML
    private Canvas canvasLanduse;
    @FXML
    private Canvas canvasNatural;
    @FXML
    private Canvas canvasPlace;

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
        GraphicsContext gcBuilding = canvasBuilding.getGraphicsContext2D();
        gcBuilding.setFillRule(FillRule.EVEN_ODD);
        gcBuilding.setLineCap(StrokeLineCap.ROUND);
        gcBuilding.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcHighway = canvasHighway.getGraphicsContext2D();
        gcHighway.setFillRule(FillRule.EVEN_ODD);
        gcHighway.setLineCap(StrokeLineCap.ROUND);
        gcHighway.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcAmenity = canvasAmenity.getGraphicsContext2D();
        gcAmenity.setFillRule(FillRule.EVEN_ODD);
        gcAmenity.setLineCap(StrokeLineCap.ROUND);
        gcAmenity.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcLeisure = canvasLeisure.getGraphicsContext2D();
        gcLeisure.setFillRule(FillRule.EVEN_ODD);
        gcLeisure.setLineCap(StrokeLineCap.ROUND);
        gcLeisure.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcAeroway = canvasAeroway.getGraphicsContext2D();
        gcAeroway.setFillRule(FillRule.EVEN_ODD);
        gcAeroway.setLineCap(StrokeLineCap.ROUND);
        gcAeroway.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcLanduse = canvasLanduse.getGraphicsContext2D();
        gcLanduse.setFillRule(FillRule.EVEN_ODD);
        gcLanduse.setLineCap(StrokeLineCap.ROUND);
        gcLanduse.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcNatural = canvasNatural.getGraphicsContext2D();
        gcNatural.setFillRule(FillRule.EVEN_ODD);
        gcNatural.setLineCap(StrokeLineCap.ROUND);
        gcNatural.setLineJoin(StrokeLineJoin.ROUND);

        GraphicsContext gcPlace = canvasPlace.getGraphicsContext2D();
        gcPlace.setFillRule(FillRule.EVEN_ODD);
        gcPlace.setLineCap(StrokeLineCap.ROUND);
        gcPlace.setLineJoin(StrokeLineJoin.ROUND);
        
        trans = new Affine();

        trans.prependTranslation(-0.56*this.viewModel.getMinLon(), this.viewModel.getMaxLat()); //Calling the code of pan, to prevent redraw before zoom has been run
        //This is done to avoid getheight and getwidth from canvas, returning way to big values
        zoom(0, 0, canvasPlace.getHeight() / (this.viewModel.getMaxLat() - this.viewModel.getMinLat()));

        startDist = getZoomDistance();
        redraw();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                redraw();
            }
        };

        canvasBuilding.setOnMousePressed(e -> {
            lastX = (float) e.getX();
            lastY = (float) e.getY();

            animationTimer.start();
        });

        canvasBuilding.setOnMouseReleased(e -> {
            animationTimer.stop();
        });

        canvasBuilding.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {

                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                pan(dx, dy);
            }

            lastX = (float) e.getX();
            lastY = (float) e.getY();
        });

        canvasBuilding.setOnScroll(e -> {
            double factor = e.getDeltaY();
            zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
            redraw();
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
        return convertTo2DPoint(canvasPlace.getWidth(), canvasPlace.getHeight());
    }

    /**
     * Pans the map
     * @param dx the upper left X corner to be panned to
     * @param dy the upper left Y corner to be panned to
     */
    private void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
    }

    /**
     * Redraws the map
     */
    private void redraw() {
        //If you remove the first updateZoomLevel it takes double the amount of time to load the chunks, we dont know why (mvh August & Oliver)
        updateZoomLevel();
        currentChunkAmountSeen = this.viewModel.updateChunks(getDetailLevel(), getUpperLeftCorner(), getLowerRightCorner());
        updateZoomLevel();

        Set<Way> waysPlace = new HashSet<>();
        Set<Way> waysNatural = new HashSet<>();
        Set<Way> waysLanduse = new HashSet<>();
        Set<Way> waysAeroway = new HashSet<>();
        Set<Way> waysLeisure = new HashSet<>();
        Set<Way> waysAmenity = new HashSet<>();
        Set<Way> waysHighway = new HashSet<>();
        Set<Way> waysBuilding = new HashSet<>();

        float zoom = getZoomDistance() / startDist * 100;

        for(int i = getDetailLevel(); i <= 4; i++) {
            Map<Integer, List<Way>> chunkLayer = viewModel.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                List<Way> chunkLayerList = chunkLayer.get(chunk);
                for(int j = 0; j < chunkLayerList.size(); j++) {
                    Way way = chunkLayerList.get(j);

                    switch (way.getPrimaryType()) {
                        case "building":
                            waysBuilding.add(way);
                            break;
                        case "highway":
                            waysHighway.add(way);
                            break;
                        case "amenity":
                            waysAmenity.add(way);
                            break;
                        case "leisure":
                            waysLeisure.add(way);
                            break;
                        case "aeroway":
                            waysAeroway.add(way);
                            break;
                        case "landuse":
                            waysLanduse.add(way);
                            break;
                        case "natural":
                            waysNatural.add(way);
                            break;
                        case "place":
                            waysPlace.add(way);
                            break;
                    }
                }
            }
        }

        new CanvasRedrawTask(canvasPlace, waysPlace, trans, zoom).run();
        new CanvasRedrawTask(canvasNatural, waysNatural, trans, zoom).run();
        new CanvasRedrawTask(canvasLanduse, waysLanduse, trans, zoom).run();
        new CanvasRedrawTask(canvasAeroway, waysAeroway, trans, zoom).run();
        new CanvasRedrawTask(canvasAmenity, waysAmenity, trans, zoom).run();
        new CanvasRedrawTask(canvasLeisure, waysLeisure, trans, zoom).run();
        new CanvasRedrawTask(canvasHighway, waysHighway, trans, zoom).run();
        new CanvasRedrawTask(canvasBuilding, waysBuilding, trans, zoom).run();
    }

    /**
     * @return int the detail level of the map
     */
    private int getDetailLevel(){
        if(zoomLevel > 300000) return 4;
        if(zoomLevel > 150000) return 3;
        if(zoomLevel > 29000) return 2;
        if(zoomLevel > 250) return 1;
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
