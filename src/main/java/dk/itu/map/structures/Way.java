package dk.itu.map.structures;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Way {

    private byte zoomLevel;
    private List<Long> outerRef;
    private List<Long> innerRef;
    private final FloatArrayList outerCoords;
    private final FloatArrayList innerCoords;
    private final String[] tags;

    private Way[] tempWays;

    private long id;
    private long[] nodeIDs;

    /**
     * Only use for FileHandler
     */
    public Way(List<Float> nodes, List<String> tags, List<Long> outerRef, List<Long> innerRef) {
        outerCoords = new FloatArrayList(10);
        innerCoords = new FloatArrayList(10);
        this.outerRef = outerRef;
        this.innerRef = innerRef;
        this.tempWays = new Way[outerRef.size() + innerRef.size()];
        this.tags = new String[tags.size()];

        for (float node : nodes) {
            outerCoords.add(node);
        }

        for (int i = 0; i < this.tags.length; i++) {
            String tag = tags.get(i);

            // If the tag is an id, set the id of the way, and dont add it to the array,
            // otherwise add it to the array
            if (tag.equals("id")) {
                this.id = Long.parseLong(tags.get(i + 1));
                i++;
            } else {
                this.tags[i] = tag;
            }
        }
    }

    public Way(List<Float> nodes, List<String> tags, List<Long> outerRef, List<Long> innerRef, long[] nodeIds) {
        this(nodes, tags, outerRef, innerRef);
        this.nodeIDs = nodeIds;
    }

    /**
     * Only used for ChunkHandler
     */
    public Way(float[] outerCoords, float[] innerCoords, String[] tags) {
        this.outerCoords = new FloatArrayList(outerCoords);
        this.innerCoords = new FloatArrayList(innerCoords);
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        builder.append(outerCoords.size());
        builder.append("\n");
        for (int i = 0; i < outerCoords.size(); i += 2) {
            builder.append(outerCoords.get(i));
            builder.append(" ");
            builder.append(outerCoords.get(i + 1));
            builder.append("\n");
        }
        builder.append("Inner nodes:\n");
        builder.append(innerCoords.size());
        builder.append("\n");
        for (int i = 0; i < innerCoords.size(); i += 2) {
            builder.append(innerCoords.get(i));
            builder.append(" ");
            builder.append(innerCoords.get(i + 1));
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
        stream.writeInt(outerCoords.size());
        for (int i = 0; i < outerCoords.size(); i++) {
            stream.writeFloat(outerCoords.get(i));
        }
        stream.writeInt(innerCoords.size());
        for (int i = 0; i < innerCoords.size(); i++) {
            stream.writeFloat(innerCoords.get(i));
        }
        stream.writeInt(tags.length);
        for (int i = 0; i < tags.length; i++) {
            stream.writeUTF(tags[i]);
        }
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(0.56f * outerCoords.get(1), -outerCoords.get(0));
        for (int i = 2; i < outerCoords.size(); i += 2) {
            gc.lineTo(0.56f * outerCoords.get(i + 1), -outerCoords.get(i));
        }
        if (innerCoords.size() > 0) {
            gc.moveTo(0.56f * innerCoords.get(1), -innerCoords.get(0));
            for (int i = 2; i < innerCoords.size(); i += 2) {
                gc.lineTo(0.56f * innerCoords.get(i + 1), -innerCoords.get(i));
            }
        }
        if (innerCoords.size() == 0) {
            gc.stroke();
        } else {
            gc.stroke();
            gc.fill();
        }
    }

    /**
     * Also only used in fileHandler
     */
    public void addRelatedWay(Way way, long id) {
        if (tempWays == null)
            throw new RuntimeException("Related ways cannot be added to fully filled ways");

        if (outerRef.contains(id)) {
            tempWays[outerRef.indexOf(id)] = way;
        }
        if (innerRef.contains(id)) {
            tempWays[outerRef.size() + innerRef.indexOf(id)] = way;
        }

        boolean filled = true;
        for (int i = 0; i < tempWays.length; i++) {
            if (tempWays[i] == null) {
                filled = false;
                break;
            }
        }
        if (filled) {
            for (int i = 0; i < outerRef.size(); i++) {
                this.outerCoords.addAll(tempWays[i].outerCoords.toArray());
            }
            for (int i = 0; i < innerRef.size(); i++) {
                this.innerCoords.addAll(tempWays[outerRef.size()+i].outerCoords.toArray());
                // outercoords are used here cause Ways use outer by default.
                // FIXME: what happens if relation has relation with inner ways inside
            }

        }
    }
    
    public void setZoomLevel(byte zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public float[] getCoords() {
        return outerCoords.toArray();
    }

    public String[] getTags() {
        return tags;
    }

    public long getId() {
        return id;
    }

    public long[] getNodeIDs() {
        return nodeIDs;
    }

}
