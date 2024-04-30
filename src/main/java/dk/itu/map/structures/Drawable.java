package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    public void draw(GraphicsContext gc, float zoom, int skipAmount, Theme theme);
    public String getPrimaryType();
}
