package dk.itu.map;

import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Way {

    private final float[] coords;
    private final String[] tags;

    public Way(List<Float> nodes, List<String> tags){
        this.coords = new float[nodes.size()];
        this.tags = new String[tags.size()];

        for(int i = 0; i < this.coords.length; i++){
            this.coords[i] = nodes.get(i);
        }
        for(int i = 0; i < this.tags.length; i++){
            this.tags[i] = tags.get(i);
        }
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        for(int i = 0; i < coords.length; i += 2){
            builder.append("Node: ");
            builder.append(coords[i]);
            builder.append(" ");
            builder.append(coords[i+1]);
            builder.append("\n");
        }

        builder.append("Tags:\n");
        for(int i = 0; i < tags.length; i += 2){
            builder.append("Tag: k=");
            builder.append(tags[i]);
            builder.append(" v=");
            builder.append(tags[i+1]);
            builder.append("\n");
        }

        return builder.toString();
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }
}
