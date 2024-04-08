package dk.itu.map.task;

import dk.itu.map.structures.Way;

import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class CanvasAnimation extends AnimationTimer {
    private final Canvas canvas;
    private final Affine trans;
    private final float zoom;

    private Set<Way> ways = null;

    public CanvasAnimation(Canvas canvas, Affine trans, float zoom, Set<Way> ways) {
        this.canvas = canvas;
        this.trans = trans;
        this.zoom = zoom;
        this.ways = ways;
    }

    @Override
    public void handle(long now) {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);

        // Draw the chunks

        for (Way way : ways) {
            way.draw(gc, zoom);
        }
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
