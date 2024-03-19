package dk.itu.map.structures;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Way {

    private byte zoomLevel;
    private Way[] outer;
    private Way[] inner;
    private List<Long> outerRef;
    private List<Long> innerRef;
    private final float[] coords;
    private final String[] tags;

    /**
     * Only use for FileHandler
     */
    public Way(List<Float> nodes, List<String> tags, List<Long> outerRef, List<Long> innerRef) {
        this.coords = new float[nodes.size()];
        this.tags = new String[tags.size()];
        this.outer = new Way[outerRef.size()];
        this.inner = new Way[innerRef.size()];

        for (int i = 0; i < this.coords.length; i += 2) {
            this.coords[i] = nodes.get(i);
            this.coords[i + 1] = nodes.get(i + 1);
        }
        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i] = tags.get(i);
        }
    }

    /**
     * Only used for ChunkHandler
     */
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

    public void stream(DataOutputStream stream) throws IOException {
        stream.writeInt(coords.length);
        for(int i = 0; i < coords.length; i++) {
            stream.writeFloat(coords[i]);
        }
        stream.writeInt(tags.length);
        for(int i = 0; i < tags.length; i++) {
            stream.writeUTF(tags[i]);
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

    /**
     * Also only used in fileHandler
     */
    public void addRelation(Way way, long id) {
        if (outerRef.contains(id)) {
            outer[outerRef.indexOf(id)] = way;
        }
        if (innerRef.contains(id)) {
            inner[innerRef.indexOf(id)] = way;
        }
    }

    public float[] getCoords() {
        return coords;
    }
    public String[] getTags() {
        return tags;
    }
    public void setZoomLevel(byte zoomLevel){ this.zoomLevel = zoomLevel;}

}
