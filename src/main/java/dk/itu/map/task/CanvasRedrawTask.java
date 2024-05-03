package dk.itu.map.task;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.Drawable;

import java.util.Set;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class CanvasRedrawTask {
    private final Canvas canvas;
    private final Affine trans;
    private final float zoomAmount;
    private final int zoomLevel;
    private Set<Drawable> drawables = null;
    Theme theme;

    public CanvasRedrawTask(Canvas canvas, Set<Drawable> drawable, Affine trans, float zoomAmount, int zoomLevel, Theme theme) {
        this.canvas = canvas;
        this.drawables = drawable;
        this.trans = trans;
        this.zoomAmount = zoomAmount;
        this.zoomLevel = zoomLevel;
        this.theme = theme;
    }

    public Void run() {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);
        int skipAmount = (int)Math.pow(3, zoomLevel);
        // Draw the chunks
        for (Drawable drawable : drawables) {
            if(drawable.getPrimaryType().equals("place") || drawable.getPrimaryType().equals("natural")) skipAmount = 243;
            drawable.draw(gc, zoomAmount, skipAmount, theme);
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
