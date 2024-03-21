package dk.itu.map;

import dk.itu.map.structures.Way;
import javafx.geometry.Point2D;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Affine;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.List;
import java.util.Map;

public class View {
    Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    Affine trans = new Affine();
    private float zoomLevel;
    private float startZoom;

    public int chunkToBeloaded = 2;

    Model model;

    public View(Model model, Stage primaryStage) {
        this.model = model;

        primaryStage.setTitle("Draw Lines");
        BorderPane pane = new BorderPane(canvas);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();



        pan(-0.56*model.chunkHandler.minlon, model.chunkHandler.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.chunkHandler.maxlat - model.chunkHandler.minlat));


        startZoom = getZoomDistance();

        System.out.println("StartZoom level: " + startZoom);

        updateZoomLevel();

        redraw();
    }

    private float getZoomDistance(){
        Point2D p1 = convertTo2DPoint(0,0);
        Point2D p2 = convertTo2DPoint(0,100);
        return (float) p1.distance(p2);
    }

    private int getDetailLevel(){
        if(zoomLevel > 50) return 4;
        if(zoomLevel > 30) return 3;
        if(zoomLevel > 20) return 2;
        if(zoomLevel > 15) return 1;
        return 0;
    }

    void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));

        gc.setStroke(Color.BLACK);
        updateZoomLevel();
        model.updateChunk(chunkToBeloaded, getDetailLevel());
        System.out.println("Drawing with detail level: " + getDetailLevel());
        int count = 0;
        long start = System.nanoTime();
        for(int i = getDetailLevel(); i <= 4; i++){
            Map<Integer, List<Way>> chunkLayer = model.chunkLayers.get(i);
            for (int chunk : chunkLayer.keySet()) {
                for(int j = 0; j < chunkLayer.get(chunk).size(); j++){
                    chunkLayer.get(chunk).get(j).draw(gc);
                    count++;
                }
            }
        }
        long end = System.nanoTime();
        System.out.println("Time to draw current chunks: " + (end - start) / 1000000000.0 + "s");
        System.out.println("Number of ways drawn: " + count);
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    void zoom(double dx, double dy, double factor) {

        //System.out.println("Factor: " + factor);
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
        redraw();
    }

    public void updateZoomLevel(){
        float newZoom = getZoomDistance();
        zoomLevel = (newZoom/startZoom) * 100;
        System.out.println("Current Zoom Level: " + zoomLevel);
    }



    public Point2D convertTo2DPoint(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }
}
