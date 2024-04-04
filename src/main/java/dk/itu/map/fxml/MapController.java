package dk.itu.map.fxml;

import dk.itu.map.Model;
import dk.itu.map.Controller;
import dk.itu.map.structures.Way;


import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

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

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;
    private Affine trans;
    private float zoomLevel;
    private float startZoom;

    private float lastX;
    private float lastY;

    private float currentChunkAmountSeen = 1;

    public MapController(Controller controller, Model viewModel) {
        super(controller, viewModel);
    }

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);
        trans = new Affine();

        trans.prependTranslation(-0.56*this.viewModel.chunkLoader.minlon, this.viewModel.chunkLoader.maxlat); //Calling the code of pan, to prevent redraw before zoom has been run
        //This is done to avoid getheight and getwidth from canvas, returning way to big values
        zoom(0, 0, canvas.getHeight() / (this.viewModel.chunkLoader.maxlat - this.viewModel.chunkLoader.minlat));

        startZoom = getZoomDistance();
        redraw();

        canvas.setOnMousePressed(e -> {
            lastX = (float) e.getX();
            lastY = (float) e.getY();
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

    private void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

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
        updateChunks();
        updateZoomLevel();

        for(int i = getDetailLevel(); i <= 4; i++){
            Map<Integer, List<Way>> chunkLayer = viewModel.chunkLayers.get(i);
            for (int chunk : chunkLayer.keySet()) {
                for(int j = 0; j < chunkLayer.get(chunk).size(); j++){
                    chunkLayer.get(chunk).get(j).draw(gc, getZoomDistance()/startZoom*100);
                }
            }
        }
    }

    private Point2D getUpperLeftCorner() {
        return convertTo2DPoint(0, 0);
    }

    private Point2D getLowerRightCorner() {
        return convertTo2DPoint(canvas.getWidth(), canvas.getHeight());
    }

    private void updateChunks() {
        Point2D upperLeftCorner = getUpperLeftCorner();
        Point2D lowerRightCorner = getLowerRightCorner();

        int upperLeftChunk = viewModel.chunkLoader.pointToChunkIndex(upperLeftCorner);
        int lowerRightChunk = viewModel.chunkLoader.pointToChunkIndex(lowerRightCorner);

        Set<Integer> chunks = getSmallestRect(upperLeftChunk, lowerRightChunk, viewModel.chunkLoader.chunkColumnAmount, viewModel.chunkLoader.chunkRowAmount);

        currentChunkAmountSeen = (float) (Math.abs(upperLeftCorner.getY() - lowerRightCorner.getY()) / viewModel.chunkLoader.CHUNK_SIZE);

        viewModel.updateChunks(chunks, getDetailLevel());
    }

    private Set<Integer> getSmallestRect(int chunk1, int chunk2, int columAmount, int rowAmount){
        Set<Integer> chunks = new HashSet<>();

        int a = Math.min(chunk1, chunk2);
        int b = Math.max(chunk1, chunk2);

        int height = b/columAmount - a/columAmount;
        int width = Math.abs(a%columAmount-b%columAmount);

        int rightMost = height > 0 ? a : b;

        for(int i = 0; i <= height; i++){
            int c = rightMost + i*columAmount;
            for(int j = 0; j <= width; j++){
                chunks.add(c-j);
            }
        }

        return chunks;
    }

    private int getDetailLevel(){
        if(zoomLevel > 55000) return 4;
        if(zoomLevel > 2300) return 3;
        if(zoomLevel > 115) return 2;
        if(zoomLevel > 10)return 1;
        return 0;
    }

    private void zoom(double dx, double dy, double factor) {
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
        redraw();
    }

    private float getZoomDistance(){
        Point2D p1 = convertTo2DPoint(0,0);
        Point2D p2 = convertTo2DPoint(0,100);
        return (float) p1.distance(p2);
    }

    private void updateZoomLevel(){
        float newZoom = getZoomDistance();
        zoomLevel = (newZoom/startZoom) * 100 * currentChunkAmountSeen * viewModel.chunkLoader.chunkAmount;
    }

    private Point2D convertTo2DPoint(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
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
