package dk.itu.map;

import javafx.geometry.Point2D;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Affine;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.NonInvertibleTransformException;

public class View {
    Canvas canvas = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    Affine trans = new Affine();

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
        redraw();
    }

    void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);

        model.updateChunk(2);
        long start = System.nanoTime();
        for (int chunk : model.chunks.keySet()) {
            for(int j = 0; j < model.chunks.get(chunk).size(); j++){
                model.chunks.get(chunk).get(j).draw(gc);
            }
        }
        long end = System.nanoTime();
        // System.out.println("Time to draw current chunks: " + (end - start) / 1000000000.0 + "s");
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

    public Point2D mousetoModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }
}
