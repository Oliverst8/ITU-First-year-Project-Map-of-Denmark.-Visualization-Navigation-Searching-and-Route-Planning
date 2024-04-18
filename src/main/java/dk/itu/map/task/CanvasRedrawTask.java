package dk.itu.map.task;

import dk.itu.map.structures.DrawableWay;

import java.util.Set;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class CanvasRedrawTask extends Task<Void> {
    private final Canvas canvas;
    private final Affine trans;
    private final float zoom;
    private Set<DrawableWay> ways = null;

    public CanvasRedrawTask(Canvas canvas, Set<DrawableWay> ways, Affine trans, float zoom) {
        this.canvas = canvas;
        this.ways = ways;
        this.trans = trans;
        this.zoom = zoom;
    }

    @Override
    protected Void call() throws Exception {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);

        // Draw the chunks
        for (DrawableWay way : ways) {
            way.draw(gc, zoom);
        }

        return null;
    }

    private void wipeCanvas(GraphicsContext gc) {
        gc.setTransform(new Affine());
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);
    }
}
