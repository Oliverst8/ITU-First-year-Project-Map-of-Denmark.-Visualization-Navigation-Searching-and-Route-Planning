package dk.itu.map.fxml.views;

import dk.itu.map.structures.TernaryTree;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.Point;
import dk.itu.map.task.CanvasRedrawTask;
import dk.itu.map.fxml.controllers.MapController;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.models.MapModel.Themes;

import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
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
    private final TernaryTree.searchAddress startAddress = new TernaryTree.searchAddress(null, null, null);
    private final TernaryTree.searchAddress endAddress = new TernaryTree.searchAddress(null, null, null);

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

        /*autocompleteStartPoint.setCompleter(textInput -> {
            System.out.println("Autocompleting start");
            Map<String[], TernaryTree.AddressNode> results = controller.searchAddress(textInput);
            List<String> suggestions = new ArrayList<>();
            int i = 0;
            for(String[] result : results.keySet()){
                suggestions.add(result[0] + ": " + result[1]);
            }
            System.out.println("Suggestions: " + suggestions);
            return suggestions;
            //return Arrays.asList("Jonathan", "Jose");
        });

        autocompleteStartPoint.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("New value selected from the auto-complete popup: " + newValue);
        });
*/
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
            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, getZoomDistance() / startDist * 100, model.theme).run();
        } else if(setEndPoint){
            float[] endPoint = new float[]{(float) e.getX(), (float) e.getY()};
            endPoint = convertToLatLon(endPoint);
            System.out.println("End point set to: " + endPoint[0] + ", " + endPoint[1]);
            setEndPoint = false;
            Point point = new Point(endPoint[0], endPoint[1],"navigation");
            model.setEndPoint(point);
            model.removeRoute();

            new CanvasRedrawTask(canvas.get("navigation"), getNavigationDrawables(), trans, getZoomDistance() / startDist * 100, model.theme).run();
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
        walkButton.setStyle("-fx-border-color: transparent");
        bikeButton.setStyle("-fx-border-color: transparent");
        carButton.setStyle("-fx-border-color:  #00CED1");
    }
    @FXML
    void switchToBikeNavigation(){
        vehicleCode = 2;
        walkButton.setStyle("-fx-border-color: transparent");
        bikeButton.setStyle("-fx-border-color: #00CED1");
        carButton.setStyle("-fx-border-color:  transparent");
    }
    @FXML
    void switchToWalkNavigation(){
        vehicleCode = 1;
        walkButton.setStyle("-fx-border-color: #00CED1");
        bikeButton.setStyle("-fx-border-color: transparent");
        carButton.setStyle("-fx-border-color: transparent");
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

    /*@FXML
    void startPointTyped(KeyEvent event) {
        System.out.println("Start point typed");
        //System.out.println(autocompleteStartPoint.getText());
        autocompleteTextField(autocompleteStartPoint);
    }

    @FXML
    void endPointTyped(KeyEvent event) {
        System.out.println("End point typed");
        autocompleteTextField(autocompleteEndPoint);
    }*/



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

            new CanvasRedrawTask(canvas, entry.getValue(), trans, zoom, model.theme).run();
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
    }
    @FXML
    void hideNavigation(){
        navigationPane.setVisible(false);
        navigationPane.setDisable(true);
        startNavigationButton.setVisible(true);
        startNavigationButton.setDisable(false);
    }

    @FXML
    void setTextToCoords(float x, float y){

        textFieldStart.setText(x + ", " + y);

    }


    private void addressSelected(TextField textField, ComboBox<TernaryTree.searchAddress> comboBox, TernaryTree.searchAddress address){
        TernaryTree.searchAddress selected = comboBox.getSelectionModel().getSelectedItem();
        if(address.streetName == null){
            if(selected == null) throw new RuntimeException("No address selected");
            textField.setText(selected.streetName);
        } else {
            if(selected == null) return;
            textField.setText(selected.toString());
            if(textField == this.textFieldStart) model.setStartPoint(selected.point);
            else model.setEndPoint(selected.point);
            redraw();
        }
        address.clone(selected);
        System.out.println(address);
        System.out.println(startAddress);
    }

    private void searchAddress(TextField textField, ComboBox<TernaryTree.searchAddress> comboBox, TernaryTree.searchAddress address){

        List<TernaryTree.searchAddress> addresses;

        String currentText = textField.getText();

        boolean shouldRestartSearch = false;

        if (address.streetName == null) addresses = searchSteet(currentText);
        else{
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

    private List<TernaryTree.searchAddress> searchSteet(String searchWord){
        return controller.searchAddress(searchWord);
    }

    private List<TernaryTree.searchAddress> searchFullAddress(TernaryTree.searchAddress node, String currentText){
        return controller.fillAddress(node, currentText);
    }


}
