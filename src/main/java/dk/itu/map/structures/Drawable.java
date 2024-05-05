package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import javafx.scene.canvas.GraphicsContext;

public interface Drawable {
    public void draw(GraphicsContext gc, float zoom, int skipAmount, Theme theme);
    public String getPrimaryType();

    default double calcLineWidth(float lineWidth, float scaleFactor) {
        if (scaleFactor > 38.92) {
            return lineWidth * scaleFactor * 0.178;
        } else if (scaleFactor > 5.31) {
            return lineWidth * scaleFactor * 0.238;
        } else if (scaleFactor > 0.220) {
            return lineWidth * scaleFactor * 0.510;
        } else if (scaleFactor > 0.066) {
            return lineWidth * scaleFactor * 0.86;
        } else {
            return lineWidth * 0.05131;
        }
    }

}
