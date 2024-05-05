package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Point used to draw a point on a canvas
 */
public class Point implements Drawable{
    private final float x;
    private final float y;
    private final String primaryType;
    private final Color color;

    /**
     * Create a new point
     * @param x the x coordinate
     * @param y the y coordinate
     * @param primaryType the primary type of the point
     * @param color the color of the point
     */
    public Point(float x, float y, String primaryType, Color color){
        this.x = x;
        this.y = y;
        this.primaryType = primaryType;
        this.color = color;
    }

    @Override
    public String getPrimaryType() {
        return primaryType;
    }

    /**
     * Draw the point on the given canvas
     * @param gc the graphics context to draw on
     * @param zoom the zoom level used to calculate the line width
     * @param skipAmount the amount of chunks to skip (Not used)
     * @param theme the theme to use when (Not used)
     */
    public void draw(GraphicsContext gc, float zoom, int skipAmount, Theme theme){
        gc.setFill(color);

        double size = calcLineWidth(0.003f, zoom);
        double x = (this.x * 0.56f) - size / 2;
        double y = (this.y * -1) - size / 2;
        gc.fillOval(x, y, size, size);
    }

    /**
     * @return the coordinates of the point
     */
    public float[] getCoords(){
        return new float[]{y, x};
    }
}
