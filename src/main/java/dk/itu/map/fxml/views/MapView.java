package dk.itu.map.fxml.views;

import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.Point;
import dk.itu.map.task.CanvasRedrawTask;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.models.MapModel.Themes;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapView {
    @FXML
    private VBox root;

    @FXML
    private AnchorPane canvasParent;

    @FXML
    private Slider zoomSlider;
    private String[] mapLayers;
    private Map<String, Canvas> canvas;
    
    // Affine transformation for panning and zooming
    private Affine trans;
    private MapController controller;
    private MapModel model;

    // Last mouse position
    private float lastX;
    // Last mouse position
    private float lastY;
    // Zoom level
    private float zoomAmount;

    private AnimationTimer render;
    private int vehicleCode = 4;
    private boolean setStartPoint = false, setEndPoint = false;

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
        mapLayers = new String[]{"building", "navigation", "highway", "amenity", "leisure", "aeroway", "landuse", "natural", "place"};

        canvas = new HashMap<>();
        for(String key : mapLayers) {
            canvas.put(key, (Canvas) canvasParent.lookup("#canvas" + key.substring(0, 1).toUpperCase() + key.substring(1)));

            GraphicsContext gc = canvas.get(key).getGraphicsContext2D();

            gc.setFillRule(FillRule.EVEN_ODD);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineJoin(StrokeLineJoin.ROUND);

            canvas.get(key).widthProperty().bind(root.widthProperty());
            canvas.get(key).heightProperty().bind(root.heightProperty());
            canvas.get(key).widthProperty().addListener(e -> redraw());
            canvas.get(key).heightProperty().addListener(e -> redraw());
        }

        trans = new Affine();
        trans.prependTranslation(-0.56 * model.getMinLon(), model.getMaxLat()); //Calling the code of pan, to prevent redraw before zoom has been run

        // Zoom to the initial view, this has to be run later to make sure view has been initialized
        Platform.runLater(() -> {
            zoom(0, 0, canvas.get("building").getHeight() / (this.model.getMaxLat() - this.model.getMinLat()));

            redraw();
        });


        render = new AnimationTimer() {
            @Override
            public void handle(long now) {
                redraw();
            }
        };

        zoomSlider.setMin(0);
        zoomSlider.setMax(15);
        zoomSlider.setMouseTransparent(true);
        zoomSlider.setFocusTraversable(false);
    }

    @FXML
    void canvasClicked(MouseEvent e){
        updateZoomAmount();
        if(setStartPoint){
            float[] startPoint = new float[]{(float) e.getX(), (float) e.getY()};
            startPoint = convertToLatLon(startPoint);
            System.out.println("Start point set to: " + startPoint[0] + ", " + startPoint[1]);
            setStartPoint = false;
            Point point = new Point(startPoint[0], startPoint[1], "navigation");
            model.setStartPoint(point);
            model.removeRoute();
            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, zoomAmount, getZoomLevel(), model.theme).run();
        } else if(setEndPoint){
            float[] endPoint = new float[]{(float) e.getX(), (float) e.getY()};
            endPoint = convertToLatLon(endPoint);
            System.out.println("End point set to: " + endPoint[0] + ", " + endPoint[1]);
            setEndPoint = false;
            Point point = new Point(endPoint[0], endPoint[1],"navigation");
            model.setEndPoint(point);
            model.removeRoute();

            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, zoomAmount, getZoomLevel(), model.theme).run();
        }
    }

    @FXML
    void canvasPressed(MouseEvent e) {
        if(e.isPrimaryButtonDown()) {
            lastX = (float) e.getX();
            lastY = (float) e.getY();

            render.start();
        }
    }

    @FXML
    void canvasReleased(MouseEvent e) {
        render.stop();
    }

    @FXML
    void switchToStandardTheme(){
        model.theme.setTheme(Themes.LIGHT);
        redraw();
    }
    @FXML
    void switchToDarkTheme(){
        model.theme.setTheme(Themes.DARK);
        redraw();
    }
    @FXML
    void switchToYetAnotherTheme(){
        model.theme.setTheme(Themes.Wierd);
        redraw();
    }
    @FXML
    void switchToCarNavigation(){
        vehicleCode = 4;
    }
    @FXML
    void switchToBikeNavigation(){
        vehicleCode = 2;
    }
    @FXML
    void switchToWalkNavigation(){
        vehicleCode = 1;
    }

    @FXML
    void canvasDragged(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {

            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            pan(dx, dy);
        }

        lastX = (float) e.getX();
        lastY = (float) e.getY();
    }

    @FXML
    void canvasScroll(ScrollEvent e) {
        double factor = e.getDeltaY();
        zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        redraw();
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

    /**
     * Pans the map
     * 
     * @param dx the upper left X corner to be panned to
     * @param dy the upper left Y corner to be panned to
     */
    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * Zooms the map
     * 
     * @param dx     the upper left X corner to be zoomed to
     * @param dy     the upper left Y corner to be zoomed to
     * @param factor the factor to zoom by
     */
    public void zoom(double dx, double dy, double factor) {
        if (Math.log(trans.getMxx()) > zoomSlider.getMax() && factor > 1) return;
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
        zoomSlider.setValue(zoomSlider.getMax()-Math.log(trans.getMxx()));
    }

    /**
     * Redraws the map
     */
    
    public static boolean overridePrint = false;
    long prevTime = 0;
    public void redraw() {
        //If you remove the first updateZoomLevel it takes double the amount of time to load the chunks, we dont know why (mvh August & Oliver)
        updateZoomAmount();
        boolean print = false;
        long totalStart = System.currentTimeMillis();
        if (System.currentTimeMillis() - prevTime > 300) {
            prevTime = System.currentTimeMillis();
            print = true;
            overridePrint = false;
        }
        controller.updateChunks(getZoomLevel(), getUpperLeftCorner(), getLowerRightCorner()/*, print*/);
        updateZoomAmount();
        if (overridePrint) {
            print = true;
            overridePrint = false;
        }

        controller.getWrittenChunks();

        Map<String, Set<Drawable>> layers = new HashMap<>();
        
        for(String key : mapLayers){
            layers.put(key, new HashSet<>());
        }
        Set<Drawable> navigationSet = getNavigationDrawables();


        layers.put("navigation", navigationSet);

        updateZoomAmount();

        for(int i = getZoomLevel(); i <= 4; i++) {
            Map<Integer, List<Drawable>> chunkLayer = model.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                List<Drawable> chunkLayerList = chunkLayer.get(chunk);
                for(int j = 0; j < chunkLayerList.size(); j++) {
                    Drawable way = chunkLayerList.get(j);

                    switch (way.getPrimaryType()) {
                        case "building", "navigation", "highway", "amenity", "leisure", "aeroway", "landuse", "natural", "place":
                            layers.get(way.getPrimaryType()).add(way);
                            break;
                    }
                }
            }
        }
        long wastedTime = System.currentTimeMillis();
        Map<String, Long> renderTimes = new HashMap<>();

        for (Map.Entry<String, Set<Drawable>> entry : layers.entrySet()) {

            long startTime = System.currentTimeMillis();

            Canvas canvas = this.canvas.get(entry.getKey());
            new CanvasRedrawTask(canvas, entry.getValue(), trans, zoomAmount, getZoomLevel(), model.theme).run();

            long endTime = System.currentTimeMillis();

            renderTimes.put(entry.getKey(), endTime - startTime);
        }
        
        if (!print) return;
        long drawTimes = 0;
        // System.out.println("Render times: ");
        // for (Map.Entry<String, Long> entry : renderTimes.entrySet()) {
        //     String layer = String.format("%-15s", entry.getKey());
        //     long renderTime = entry.getValue();
        //     drawTimes += renderTime;
            
        //     System.out.println(layer + ": " + renderTime + " ");
        // }
        // System.out.println("Current zoomLevel: " + getZoomLevel());
        // System.out.println("Currently skipping: " + (int)Math.pow(3, getZoomLevel()));
        // System.out.println("Total draw time: " + drawTimes + "ms");
        // System.out.println("Total wasted time: " + (wastedTime - totalStart) + "ms");
        // System.out.println("Total render time: " + (System.currentTimeMillis() - totalStart) + "ms");
        // System.out.println();
    }

    private Set<Drawable> getNavigationDrawables() {
        Set<Drawable> navigationSet = new HashSet<>();

        for(Drawable drawable : model.getNavigationWays()){
            if(drawable == null) continue;
            navigationSet.add(drawable);
        }
        return navigationSet;
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
        return convertTo2DPoint(canvas.get("building").getWidth(), canvas.get("building").getHeight());
    }

    /**
     * @return int the detail level of the map
     */
    private int getZoomLevel(){
        if(zoomAmount > 10) return 4;
        if(zoomAmount > 5) return 3;
        if(zoomAmount > 3) return 2;
        if(zoomAmount > 1) return 1;
        return 0;
    }

    /**
     * @return float the current distance between the two points (0,0) & (0,100)
     */
    private float getZoomDistance() {
        Point2D p1 = convertTo2DPoint(0, 0); 
        Point2D p2 = convertTo2DPoint(0, 100);
        return (float) p1.distance(p2);
    }

    /**
     * Updates the zoom level
     */
    private void updateZoomAmount() {
        zoomAmount = getZoomDistance() * 100;
    }

    /**
     * Converts from canvas to JavaFx 2D point
     * 
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

    private float[] convertToLatLon(float[] startPoint) {
        Point2D point = convertTo2DPoint(startPoint[0], startPoint[1]);
        return new float[]{(float) point.getX()/0.56f, (float) point.getY()*(-1)};
    }

    @FXML
    void setStartPoint(ActionEvent event){
        System.out.println("Can now set start point");
        setStartPoint = true;
        setEndPoint = false;
    }

    @FXML
    void setEndPoint(ActionEvent event){
        System.out.println("Can now set end point");
        setEndPoint = true;
        setStartPoint = false;
    }

    @FXML
    void navigateNow(ActionEvent event){
        controller.navigate(vehicleCode);
        redraw();
    }
}
