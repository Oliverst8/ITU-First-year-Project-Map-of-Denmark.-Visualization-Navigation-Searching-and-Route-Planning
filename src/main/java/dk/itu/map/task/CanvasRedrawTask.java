package dk.itu.map.task;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.Drawable;

import java.util.Set;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class CanvasRedrawTask {// extends Task<Void> {
    private final Canvas canvas;
    private final Affine trans;
    private final float zoom;
    private Set<Drawable> ways = null;
    Theme theme;

    public CanvasRedrawTask(Canvas canvas, Set<Drawable> ways, Affine trans, float zoom, Theme theme) {
        this.canvas = canvas;
        this.ways = ways;
        this.trans = trans;
        this.zoom = zoom;
        this.theme = theme;
    }

    // @Override
    public Void run() {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);

        // Draw the chunks
        for (Drawable way : ways) {
            way.draw(gc, zoom, theme);
        }

        return null;
    }

    private void wipeCanvas(GraphicsContext gc) {
        gc.setTransform(new Affine());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);
    }
}
