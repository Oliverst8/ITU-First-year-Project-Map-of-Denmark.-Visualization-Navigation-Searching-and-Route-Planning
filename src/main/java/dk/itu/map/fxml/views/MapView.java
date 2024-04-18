package dk.itu.map.fxml.views;

import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Point;
import dk.itu.map.task.CanvasRedrawTask;
import dk.itu.map.utility.Navigation;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.MapModel;

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
    private float zoomLevel;
    // Initial distance between two points
    private float startDist;

    // Amount of chunks seen
    private float currentChunkAmountSeen = 1;

    private AnimationTimer render;
    private int themeNumber = 0;
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
        }

        trans = new Affine();
        trans.prependTranslation(-0.56 * model.getMinLon(), model.getMaxLat()); //Calling the code of pan, to prevent redraw before zoom has been run

        // Zoom to the initial view
        Platform.runLater(() -> {
            zoom(0, 0, canvas.get("building").getHeight() / (this.model.getMaxLat() - this.model.getMinLat()));

            redraw();
        });

        startDist = getZoomDistance();


        render = new AnimationTimer() {
            @Override
            public void handle(long now) {
                redraw();
            }
        };
    }

    @FXML
    void canvasClicked(MouseEvent e){
        if(setStartPoint){
            float[] startPoint = new float[]{(float) e.getX(), (float) e.getY()};
            startPoint = convertToLatLon(startPoint);
            System.out.println("Start point set to: " + startPoint[0] + ", " + startPoint[1]);
            setStartPoint = false;
            Point point = new Point(startPoint[0], startPoint[1], "navigation");
            model.setStartPoint(point);
            model.removeRoute();
            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, getZoomDistance()/startDist*100, themeNumber).run();
        } else if(setEndPoint){
            float[] endPoint = new float[]{(float) e.getX(), (float) e.getY()};
            endPoint = convertToLatLon(endPoint);
            System.out.println("End point set to: " + endPoint[0] + ", " + endPoint[1]);
            setEndPoint = false;
            Point point = new Point(endPoint[0], endPoint[1],"navigation");
            model.setEndPoint(point);
            model.removeRoute();

            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, getZoomDistance()/startDist*100, themeNumber).run();
        }
    }

    @FXML
    void canvasPressed(MouseEvent e) {
        if(e.isPrimaryButtonDown()) {
            lastX = (float) e.getX();
            lastY = (float) e.getY();

            render.start();
        } else if(e.isSecondaryButtonDown()) {
            Navigation navigation = new Navigation(model.getGraph(), vehicleCode);
            DrawableWay path = navigation.getPath(814157l,2395042472l); //this works
            System.out.println(path);
            path.draw(canvas.get("highway").getGraphicsContext2D(), getZoomDistance()/startDist*100, themeNumber);
        }
    }

    @FXML
    void canvasReleased(MouseEvent e) {
        render.stop();
    }

    @FXML
    void switchToStandardTheme(){
        themeNumber = 0;
        redraw();
    }
    @FXML
    void switchToDarkTheme(){
        themeNumber = 1;
        redraw();
    }
    @FXML
    void switchToYetAnotherTheme(){
        themeNumber = 2;
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
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
    }

    /**
     * Redraws the map
     */
    private void redraw() {
        //If you remove the first updateZoomLevel it takes double the amount of time to load the chunks, we dont know why (mvh August & Oliver)
        updateZoomLevel();
        currentChunkAmountSeen = controller.updateChunks(getDetailLevel(), getUpperLeftCorner(), getLowerRightCorner());
        updateZoomLevel();

        Map<String, Set<Drawable>> ways = new HashMap<>();
        
        for(String key : mapLayers){
            ways.put(key, new HashSet<>());
        }
        Set<Drawable> navigationSet = getNavigationDrawables();


        ways.put("navigation", navigationSet);

        float zoom = getZoomDistance() / startDist * 100;

        for(int i = getDetailLevel(); i <= 4; i++) {
            Map<Integer, List<Drawable>> chunkLayer = model.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                List<Drawable> chunkLayerList = chunkLayer.get(chunk);
                for(int j = 0; j < chunkLayerList.size(); j++) {
                    Drawable way = chunkLayerList.get(j);

                    switch (way.getPrimaryType()) {
                        case "building", "navigation", "highway", "amenity", "leisure", "aeroway", "landuse", "natural", "place":
                            ways.get(way.getPrimaryType()).add(way);
                            break;
                    }
                }
            }
        }

        for (Map.Entry<String, Set<Drawable>> entry : ways.entrySet()) {
            
            Canvas canvas = this.canvas.get(entry.getKey());

            new CanvasRedrawTask(canvas, entry.getValue(), trans, zoom, themeNumber).run();
        }
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
    private int getDetailLevel(){
        if(zoomLevel > 0.07) return 4;
        if(zoomLevel > 0.01) return 3;
        if(zoomLevel > 0.01) return 2;
        if(zoomLevel > 6.9539517E-4) return 1;
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
    private void updateZoomLevel() {
        float newZoom = getZoomDistance();
        zoomLevel = (newZoom / startDist) * 100;
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
    }

    @FXML
    void setEndPoint(ActionEvent event){
        System.out.println("Can now set end point");
        setEndPoint = true;
    }

    @FXML
    void navigateNow(ActionEvent event){
        controller.navigate();
        redraw();
    }
}
