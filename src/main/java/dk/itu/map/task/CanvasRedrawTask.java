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

    /**
     * Create a new CanvasRedrawTask
     * The task is responsible for redrawing the canvas with the given drawables
     * @param canvas The canvas to be redrawn on
     * @param drawable The drawables to be drawn
     * @param trans The transformation to be applied
     * @param zoomAmount The zoom amount to be applied
     * @param zoomLevel The zoom level to be used when drawing
     * @param theme The theme to be used when drawing
     */
    public CanvasRedrawTask(Canvas canvas, Set<Drawable> drawable, Affine trans, float zoomAmount, int zoomLevel, Theme theme) {
        this.canvas = canvas;
        this.drawables = drawable;
        this.trans = trans;
        this.zoomAmount = zoomAmount;
        this.zoomLevel = zoomLevel;
        this.theme = theme;
    }

    /**
     * Run the canvas redraw task
     */
    public Void run() {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);
        int skipAmount = (int)Math.pow(3, zoomLevel);
        // Draw the chunks
        for (Drawable drawable : drawables) {
            drawable.draw(gc, zoomAmount, skipAmount, theme);
        }

        return null;
    }

    /**
     * Wipe the canvas
     * @param gc The graphics context to be used
     */
    private void wipeCanvas(GraphicsContext gc) {
        gc.setTransform(new Affine());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);
    }
}
