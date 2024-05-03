package dk.itu.map.task;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.Drawable;

import java.util.Set;

import dk.itu.map.structures.DrawableWay;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import dk.itu.map.fxml.views.MapView;

public class CanvasRedrawTask {
    private final Canvas canvas;
    private final Affine trans;
    private final float zoomAmount;
    private final int zoomLevel;
    private Set<Drawable> drawables = null;
    Theme theme;
    MapView mapView;

    public CanvasRedrawTask(Canvas canvas, Set<Drawable> drawable, Affine trans, float zoomAmount, int zoomLevel, Theme theme, MapView mapWiew) {
        this.canvas = canvas;
        this.drawables = drawable;
        this.trans = trans;
        this.zoomAmount = zoomAmount;
        this.zoomLevel = zoomLevel;
        this.theme = theme;
        this.mapView = mapWiew;
    }

    public Void run() {
        // Redraw the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        wipeCanvas(gc);
        int skipAmount = (int)Math.pow(3, zoomLevel);
        // Draw the chunks
        for (Drawable drawable : drawables) {
            if(drawable instanceof DrawableWay){
                float[] coord = ((DrawableWay) drawable).getOuterCoords().get(0);
                if((drawable.getPrimaryType().equals("place") || drawable.getPrimaryType().equals("natural")) && !isCoordInView(coord)) skipAmount = 243;
            }
            drawable.draw(gc, zoomAmount, skipAmount, theme);
        }
        return null;
    }

    public boolean isCoordInView(float[] coord) {
        float[] min = convertToLatLon(mapView.getUpperLeftCorner());
        float[] max = convertToLatLon(mapView.getLowerRightCorner());
        return coord[0] >= min[0] && coord[0] <= max[0] && coord[1] >= min[1] && coord[1] <= max[1];
    }

    public float[] convertToLatLon(Point2D point) {
        return new float[]{(float) point.getX()/0.56f, (float) point.getY()*(-1)};
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
