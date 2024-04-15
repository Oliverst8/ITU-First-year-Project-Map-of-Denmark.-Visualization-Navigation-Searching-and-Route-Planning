package dk.itu.map.structures;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point implements Drawable{
    private float x, y;
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void draw(GraphicsContext gc){
        gc.setStroke(Color.BEIGE);
        float width = 0.03f;
        float height = 0.03f;
        float x = (this.x*0.56f) - width/2;
        float y = (this.y*-1) - height/2;
        gc.fillOval( x, y, width, height);
    }
}
