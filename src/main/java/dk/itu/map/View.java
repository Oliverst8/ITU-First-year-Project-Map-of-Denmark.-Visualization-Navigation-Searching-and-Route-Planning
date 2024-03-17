package dk.itu.map;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.transform.Affine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.transform.NonInvertibleTransformException;

public class View {
    public Canvas canvas;
    public GraphicsContext gc;

    public Button zoomIn;
    public Button zoomOut;

    Affine trans = new Affine();

    Model model;

    public View(Model model, Stage primaryStage) {
        this.model = model;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/scenes/map.fxml"));
    
            Scene scene = new Scene(root);

            primaryStage.setTitle("MapIT");
            primaryStage.setScene(scene);
            primaryStage.show();

            canvas = (Canvas) scene.lookup("#canvas");
            gc = canvas.getGraphicsContext2D();

            zoomIn = (Button) scene.lookup("#zoomIn");
            zoomOut = (Button) scene.lookup("#zoomOut");

            pan(-0.56*model.minlon, model.maxlat);
            zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
            redraw();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));

        gc.setStroke(Color.BLACK);

        for (int i = 0; i < model.ways.size(); i++) {
            model.ways.get(i).draw(gc);
        }
    }

    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    public void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
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
