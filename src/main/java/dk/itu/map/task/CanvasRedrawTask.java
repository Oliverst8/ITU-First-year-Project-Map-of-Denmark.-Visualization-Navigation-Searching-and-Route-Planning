package dk.itu.map.task;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
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
        // Draw the chunks
        for (Drawable drawable : drawables) {
            int skipAmount = (int)Math.pow(3, zoomLevel);
            if(drawable instanceof DrawableWay && (drawable.getPrimaryType().equals("place") || drawable.getPrimaryType().equals("natural"))){
                boolean isInView = false;
                CoordArrayList outerCoords = ((DrawableWay) drawable).getOuterCoords();
                for(int i = 0; i < outerCoords.size(); i+= skipAmount){
                    if(isCoordInView(outerCoords.get(i))){
                        isInView = true;
                        break;
                    }
                }

                if(!isInView) skipAmount = 243;
            }
            drawable.draw(gc, zoomAmount, skipAmount, theme);
        }
        return null;
    }

    public boolean isCoordInView(float[] coord) {
        float[] topLeft = convertToLatLon(0f,0f);
        float[] bottomRight = convertToLatLon((float) canvas.getHeight(), (float) canvas.getWidth());
        return coord[1] >= topLeft[0] && coord[1] <= bottomRight[0] && coord[0] <= topLeft[1] && coord[0] >= bottomRight[1];
    }

    private Point2D convertTo2DPoint(double x, double y) {
        try {
            return trans.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    private float[] convertToLatLon(float x, float y) {
        Point2D point = convertTo2DPoint(x, y);
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
