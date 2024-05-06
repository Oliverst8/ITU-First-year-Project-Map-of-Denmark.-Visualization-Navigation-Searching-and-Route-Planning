package dk.itu.map.fxml.views;

import dk.itu.map.structures.TernaryTree;
import dk.itu.map.App;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.Point;
import dk.itu.map.task.CanvasRedrawTask;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.models.MapModel.Themes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapView {
    @FXML
    private VBox root;
    @FXML
    private ComboBox<TernaryTree.searchAddress> startComboBox;
    @FXML
    private ComboBox<TernaryTree.searchAddress> endComboBox;
    @FXML
    private TextField textFieldStart;
    @FXML
    private TextField textFieldEnd;
    @FXML
    private AnchorPane canvasParent;
    @FXML
    private AnchorPane navigationPane;
    @FXML
    private Button startNavigationButton;
    @FXML
    private Button walkButton;
    @FXML
    private Button bikeButton;
    @FXML
    private Button carButton;
    @FXML
    private Button addMarkerButton;
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
    // Initial distance between two points
    private final TernaryTree.searchAddress startAddress = new TernaryTree.searchAddress(null, null, null);
    private final TernaryTree.searchAddress endAddress = new TernaryTree.searchAddress(null, null, null);

    // Amount of chunks seen
    private float zoomAmount;

    private AnimationTimer render;
    private int vehicleCode = 4;
    private boolean setStartPoint = false, setEndPoint = false, setPointOfInterest = false;
    private boolean showGrid = false;

    /**
     * Creates a new MapView
     * @param controller The controller to be used
     * @param model      The model to be used
     */
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
        mapLayers = new String[]{"landmass","building", "navigation", "highway", "amenity", "leisure", "aeroway", "landuse", "natural", "leisure", "place", "pointOfInterest"};

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
            Point point = new Point(startPoint[0], startPoint[1], "navigation", Color.RED);
            textFieldStart.setText(startPoint[0] + ", " + startPoint[1]);
            textFieldStart.setPromptText("Where from?");
            model.setStartPoint(point);
            model.removeRoute();
            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, zoomAmount, getZoomLevel(), model.theme).run();
        } else if(setEndPoint){
            float[] endPoint = new float[]{(float) e.getX(), (float) e.getY()};
            endPoint = convertToLatLon(endPoint);
            System.out.println("End point set to: " + endPoint[0] + ", " + endPoint[1]);
            textFieldEnd.setPromptText("Where to?");
            setEndPoint = false;
            Point point = new Point(endPoint[0], endPoint[1],"navigation", Color.RED);
            textFieldEnd.setText(endPoint[0] + ", " + endPoint[1]);
            model.setEndPoint(point);
            model.removeRoute();

            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, zoomAmount, getZoomLevel(), model.theme).run();
        } else if(setPointOfInterest){
            float[] pointOfInterest = new float[]{(float) e.getX(), (float) e.getY()};
            pointOfInterest = convertToLatLon(pointOfInterest);
            System.out.println("Point of interest set to: " + pointOfInterest[0] + ", " + pointOfInterest[1]);
            setPointOfInterest = false;
            startNavigationButton.setDisable(false);
            addMarkerButton.setStyle("-fx-border-color: transparent");

            String path = model.getMapType().equals("internal") ? App.DATA_PATH + App.mapName + "-internal/" : App.DATA_PATH + App.mapName + "/";
            File file = new File(path + "utilities/pointOfInterest.txt");

            if (!file.exists()) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            try (FileWriter writer = new FileWriter(path + "utilities/pointOfInterest.txt", true)) {
                writer.write(pointOfInterest[0] + ", " + pointOfInterest[1] + "\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            redraw();
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
    void switchToRandomTheme(){
        model.theme.setTheme(Themes.RANDOM);
        redraw();
    }
    @FXML
    void switchToCarNavigation(){
        vehicleCode = 4;
        walkButton.setStyle("-fx-border-color: transparent");
        bikeButton.setStyle("-fx-border-color: transparent");
        carButton.setStyle("-fx-border-color:  #00CED1");
        navigateNow(null);
    }
    @FXML
    void switchToBikeNavigation(){
        vehicleCode = 2;
        walkButton.setStyle("-fx-border-color: transparent");
        bikeButton.setStyle("-fx-border-color: #00CED1");
        carButton.setStyle("-fx-border-color:  transparent");
        navigateNow(null);

    }
    @FXML
    void switchToWalkNavigation(){
        vehicleCode = 1;
        walkButton.setStyle("-fx-border-color: #00CED1");
        bikeButton.setStyle("-fx-border-color: transparent");
        carButton.setStyle("-fx-border-color: transparent");
        navigateNow(null);

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
    @FXML
    void setStartPoint(ActionEvent event){
        setStartPoint = true;
        setEndPoint = false;
        textFieldStart.setText("");
        textFieldStart.setPromptText("Select a point");
        textFieldStart.setStyle("-fx-border-color: transparent");

    }

    @FXML
    void setEndPoint(ActionEvent event){
        setEndPoint = true;
        setStartPoint = false;
        textFieldEnd.setText("");
        textFieldStart.setPromptText("Select a point");
        textFieldEnd.setStyle("-fx-border-color: transparent");
    }

    @FXML
    void navigateNow(ActionEvent event){
        controller.navigate(vehicleCode);
        redraw();
    }

    @FXML
    void addPointOfInterest(ActionEvent event){
        setPointOfInterest = true;
        startNavigationButton.setDisable(true);
        addMarkerButton.setStyle("-fx-border-color: #7FFF00");
    }

    @FXML
    void searchStartAddress(KeyEvent event){
        searchAddress(textFieldStart, startComboBox, startAddress);
    }
    @FXML
    void searchEndAddress(KeyEvent event){
        searchAddress(textFieldEnd, endComboBox, endAddress);
    }

    @FXML
    void addressSelectedStart(ActionEvent event){
        addressSelected(textFieldStart, startComboBox, startAddress);
    }

    @FXML
    void addressSelectedEnd(ActionEvent event){
        addressSelected(textFieldEnd, endComboBox, endAddress);
    }
    @FXML
    void showNavigation(){
        navigationPane.setVisible(true);
        navigationPane.setDisable(false);
        startNavigationButton.setVisible(false);
        startNavigationButton.setDisable(true);
        addMarkerButton.setVisible(false);
        addMarkerButton.setDisable(true);
    }
    @FXML
    void hideNavigation(){
        navigationPane.setVisible(false);
        navigationPane.setDisable(true);
        startNavigationButton.setVisible(true);
        startNavigationButton.setDisable(false);
        addMarkerButton.setVisible(true);
        addMarkerButton.setDisable(false);
    }

    @FXML
    void setTextToCoords(float x, float y){

        textFieldStart.setText(x + ", " + y);

    }

    @FXML
    void toggleGrid() {
        showGrid = !showGrid;
        redraw();
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
        if (System.currentTimeMillis() - prevTime > 300) {
            prevTime = System.currentTimeMillis();
            overridePrint = false;
        }
        controller.updateChunks(getZoomLevel(), getUpperLeftCorner(), getLowerRightCorner()/*, print*/);
        updateZoomAmount();
        if (overridePrint) {
            overridePrint = false;
        }

        controller.getWrittenChunks();

        Map<String, Set<Drawable>> layers = new HashMap<>();
        
        for(String key : mapLayers){
            layers.put(key, new HashSet<>());
        }
        Set<Drawable> navigationSet = getNavigationDrawables();
        Set<Drawable> pointOfInterests = getPointOfInterests();

        layers.put("navigation", navigationSet);
        layers.put("pointOfInterest", pointOfInterests);
        layers.put("landmass", model.landLayer);

        updateZoomAmount();

        for(int i = getZoomLevel(); i < 5; i++) {
            Map<Integer, List<Drawable>> chunkLayer = model.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                List<Drawable> chunkLayerList = chunkLayer.get(chunk);
                for(int j = 0; j < chunkLayerList.size(); j++) {
                    Drawable way = chunkLayerList.get(j);

                    switch (way.getPrimaryType()) {
                        case "building", "navigation", "highway", "amenity", "leisure", "aeroway", "landuse", "natural", "place", "pointOfInterest":
                            layers.get(way.getPrimaryType()).add(way);
                            break;
                    }
                }
            }
        }
        Map<String, Long> renderTimes = new HashMap<>();

        for (Map.Entry<String, Set<Drawable>> entry : layers.entrySet()) {

            long startTime = System.currentTimeMillis();

            Canvas canvas = this.canvas.get(entry.getKey());
            new CanvasRedrawTask(canvas, entry.getValue(), trans, zoomAmount, getZoomLevel(), model.theme).run();

            long endTime = System.currentTimeMillis();

            renderTimes.put(entry.getKey(), endTime - startTime);
        }

        if (showGrid) {
            GraphicsContext gc = canvas.get("pointOfInterest").getGraphicsContext2D();
            for (float x = model.getMinLat(); x < model.getMaxLat(); x += model.getChunkSize()*Math.pow(2, getZoomLevel())) {
                gc.setLineWidth(0.0003 * (getZoomLevel() + 1));
                gc.setStroke(Color.BLACK);
                gc.beginPath();
                gc.moveTo(model.getMinLon()*0.56, -x);
                gc.lineTo(model.getMaxLon()*0.56, -x);
                gc.stroke();
            }
            for (float y = model.getMinLon(); y < model.getMaxLon(); y += model.getChunkSize()*Math.pow(2, getZoomLevel())) {
                gc.setLineWidth(0.0003 * (getZoomLevel() + 1));
                gc.setStroke(Color.BLACK);
                gc.beginPath();
                gc.moveTo(y*0.56, -model.getMinLat());
                gc.lineTo(y*0.56, -model.getMaxLat());
                gc.stroke();
            }
        }
    }

    /**
     * Gets the navigation drawables
     * @return Set<Drawable> the navigation drawables
     */
    private Set<Drawable> getNavigationDrawables() {
        Set<Drawable> navigationSet = new HashSet<>();

        for(Drawable drawable : model.getNavigationWays()){
            if(drawable == null) continue;
            navigationSet.add(drawable);
        }
        return navigationSet;
    }

    /**
     * Gets the point of interests
     * @return Set<Drawable> the point of interests
     */
    private Set<Drawable> getPointOfInterests() {
        Set<Drawable> pointOfInterests = new HashSet<>();

        String path = model.getMapType().equals("internal") ? App.DATA_PATH + App.mapName + "-internal/" : App.DATA_PATH + App.mapName + "/";
        File file = new File(path + "utilities/pointOfInterest.txt");

        if (!file.exists()) return pointOfInterests;

        try (BufferedReader reader = new BufferedReader(new FileReader(path + "utilities/pointOfInterest.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coords = line.split(", ");
                float lat = Float.parseFloat(coords[0]);
                float lon = Float.parseFloat(coords[1]);
                pointOfInterests.add(new Point(lat, lon, "pointOfInterest", Color.YELLOW));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pointOfInterests;
    }

    /**
     * @return Point2D the upper left corner of the canvas
     */
    public Point2D getUpperLeftCorner() {
        return convertTo2DPoint(0, 0);
    }

    /**
     * @return Point2D the lower right corner of the canvas
     */
    public Point2D getLowerRightCorner() {
        return convertTo2DPoint(canvas.get("building").getWidth(), canvas.get("building").getHeight());
    }

    /**
     * @return int the detail level of the map
     */
    private int getZoomLevel(){
        if(zoomAmount > 20) return 5;
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

    /**
     * Converts from lat/lon to canvas coordinates
     * @param startPoint the start point
     * @return float[] the converted point
     */
    private float[] convertToLatLon(float[] startPoint) {
        Point2D point = convertTo2DPoint(startPoint[0], startPoint[1]);
        return new float[]{(float) point.getX()/0.56f, (float) point.getY()*(-1)};
    }

    // TODO: Write javadoc
    private void addressSelected(TextField textField, ComboBox<TernaryTree.searchAddress> comboBox, TernaryTree.searchAddress address){
        TernaryTree.searchAddress selected = comboBox.getSelectionModel().getSelectedItem();
        textField.setStyle("-fx-border-color: transparent");

        if(address.streetName == null){
            if(selected == null) return;
            textField.setText(selected.streetName);
        } else {
            if(selected == null) return;
            textField.setText(selected.toString());
            if(textField == this.textFieldStart) model.setStartPoint(selected.point);
            else model.setEndPoint(selected.point);
            textField.setStyle("-fx-border-color: #7FFF00");
            redraw();
        }

        address.clone(selected);
    }

    // TODO: Write javadoc
    private void searchAddress(TextField textField, ComboBox<TernaryTree.searchAddress> comboBox, TernaryTree.searchAddress address){

        List<TernaryTree.searchAddress> addresses;

        String currentText = textField.getText();

        boolean shouldRestartSearch = false;

        if (address.streetName == null) {
            addresses = searchSteet(currentText);
        } else {
            int i = 0;
            for(char c : address.streetName.toCharArray()){
                if(i >= currentText.length() || c != currentText.charAt(i++)){
                    shouldRestartSearch = true;
                    break;
                }
            }
            if (shouldRestartSearch) {
                address.reset();
                addresses = searchSteet(currentText);
            } else addresses = searchFullAddress(address, currentText);
        }

        comboBox.getItems().clear();
        comboBox.getItems().addAll(addresses);
        comboBox.setVisibleRowCount(10);
        comboBox.show();
    }

    // TODO: Write javadoc
    private List<TernaryTree.searchAddress> searchSteet(String searchWord){
        return controller.searchAddress(searchWord);
    }

    // TODO: Write javadoc
    private List<TernaryTree.searchAddress> searchFullAddress(TernaryTree.searchAddress node, String currentText){
        return controller.fillAddress(node, currentText);
    }
}
