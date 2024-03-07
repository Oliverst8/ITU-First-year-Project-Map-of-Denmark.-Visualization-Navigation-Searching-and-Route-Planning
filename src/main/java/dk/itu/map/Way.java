package dk.itu.map;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Way {

    private final float[] coords;
    private final String[] tags;

    /*public Way(List<Float> nodes, List<String> tags){
        this.coords = new float[nodes.size()];
        this.tags = new String[tags.size()];

        for(int i = 0; i < this.coords.length; i += 2){
            this.coords[i] = 0.56f * nodes.get(i);
            this.coords[i+1] = -nodes.get(i+1);
        }
        for(int i = 0; i < this.tags.length; i++){
            this.tags[i] = tags.get(i);
        }
    }*/

    public Way(ArrayList<Node> way, ArrayList<String> tags) {
        coords = new float[way.size() * 2];
        this.tags = new String[tags.size()];

        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = node.lat;
            coords[2 * i + 1] = node.lon;
        }
        for(int i = 0; i < this.tags.length; i++){
            this.tags[i] = tags.get(i);
        }
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        for(int i = 0; i < coords.length; i += 2){
            builder.append(coords[i]);
            builder.append(" ");
            builder.append(coords[i+1]);
            builder.append("\n");
        }

        builder.append("Tags:\n");
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


public float[] getCoords(){
    return coords;
}

}
