package dk.itu.map.structures;

import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    public void draw(GraphicsContext gc, float zoom, int themeNumber);
    public String getPrimaryType();
}
