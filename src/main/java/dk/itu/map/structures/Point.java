package dk.itu.map.structures;

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

    public void draw(GraphicsContext gc, float zoom, int themeNumber){
        gc.setStroke(Color.BEIGE);
        float width = 0.0003f;
        float height = 0.0003f;
        float x = (this.x*0.56f) - width/2;
        float y = (this.y*-1) - height/2;
        gc.fillOval( x, y, width, height);
    }

    @Override
    public String getPrimaryType() {
        return primaryType;
    }

    public float[] getCoords(){
        return new float[]{y, x};
    }
}
