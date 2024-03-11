package dk.itu.map.structures;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.skin.ButtonSkin;

public class Way {

    private final float[] coords;
    private final String[] tags;

    public Way(List<Float> nodes, List<String> tags) {
        this.coords = new float[nodes.size()];
        this.tags = new String[tags.size()];

        for(int i = 0; i < this.coords.length; i += 2){
            this.coords[i] = nodes.get(i);
            this.coords[i+1] = nodes.get(i+1);
        }
        for(int i = 0; i < this.tags.length; i++){
            this.tags[i] = tags.get(i);
        }
    }

    public Way(float[] coords, String[] tags) {
        this.coords = coords;
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        builder.append(coords.length);
        builder.append("\n");
        for(int i = 0; i < coords.length; i += 2){
            builder.append(coords[i]);
            builder.append(" ");
            builder.append(coords[i+1]);
            builder.append("\n");
        }
        
        builder.append("Tags:\n");
        builder.append(tags.length);
        builder.append("\n");
        for(int i = 0; i < tags.length; i += 2){
            builder.append(tags[i]);
            builder.append(" ");
            builder.append(tags[i+1]);
            builder.append("\n");
        }

        return builder.toString();
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(0.56f * coords[1], -coords[0]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(0.56f * coords[i+1], -coords[i]);
        }
        gc.stroke();
    }

    public float[] getCoords() {
        return coords;
    }
}
