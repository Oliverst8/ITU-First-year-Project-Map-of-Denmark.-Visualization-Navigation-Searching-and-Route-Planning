package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point implements Drawable{
    private float x, y;
    private final String primaryType;
    public Point(float x, float y, String primaryType){
        this.x = x;
        this.y = y;
        this.primaryType = primaryType;
    }

    public void draw(GraphicsContext gc, float zoom, int skipAmount, Theme theme){
        gc.setStroke(Color.BEIGE);

        double size = calcLineWidth(0.003f, zoom);
        double x = (this.x * 0.56f) - size / 2;
        double y = (this.y * -1) - size / 2;
        gc.fillOval( x, y, size, size);
    }

    @Override
    public String getPrimaryType() {
        return primaryType;
    }

    public float[] getCoords(){
        return new float[]{y, x};
    }
}
