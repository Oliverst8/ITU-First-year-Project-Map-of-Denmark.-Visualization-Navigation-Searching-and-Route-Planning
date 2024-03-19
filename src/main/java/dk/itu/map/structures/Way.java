package dk.itu.map.structures;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Way {

    private byte zoomLevel;
    private final float[] coords;
    private final String[] tags;

    private long id;
    private long[] nodeIds;

    public Way(List<Float> nodes, List<String> tags, long[] nodeIds) {
        this.coords = new float[nodes.size()];
        this.tags = new String[tags.size()];
        this.nodeIds = nodeIds;
        for (int i = 0; i < this.coords.length; i += 2) {
            this.coords[i] = nodes.get(i);
            this.coords[i + 1] = nodes.get(i + 1);
        }
        for (int i = 0; i < this.tags.length; i++) {
            String tag = tags.get(i);

            // If the tag is an id, set the id of the way, and dont add it to the array, otherwise add it to the array
            if(tag.equals("id")) {
                this.id = Long.parseLong(tags.get(i+1));
                i++;
            } else this.tags[i] = tags.get(i);
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
        for (int i = 0; i < coords.length; i += 2) {
            builder.append(coords[i]);
            builder.append(" ");
            builder.append(coords[i + 1]);
            builder.append("\n");
        }

        builder.append("Tags:\n");
        builder.append(tags.length);
        builder.append("\n");
        for (int i = 0; i < tags.length; i += 2) {
            builder.append(tags[i]);
            builder.append(" ");
            builder.append(tags[i + 1]);
            builder.append("\n");
        }

        return builder.toString();
    }

    public void stream(DataOutputStream stream) {
        try {

            stream.writeInt(coords.length);
            for(int i = 0; i < coords.length; i++) {
                stream.writeFloat(coords[i]);
            }
            stream.writeInt(tags.length);
            for(int i = 0; i < tags.length; i++) {
                stream.writeUTF(tags[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(0.56f * coords[1], -coords[0]);
        for (int i = 2; i < coords.length; i += 2) {
            gc.lineTo(0.56f * coords[i + 1], -coords[i]);
        }
        gc.stroke();
    }

    public float[] getCoords() {
        return coords;
    }
    public String[] getTags() {
        return tags;
    }

    public long getId() {
        return id;
    }
    public void setZoomLevel(byte zoomLevel){ this.zoomLevel = zoomLevel;}

}
