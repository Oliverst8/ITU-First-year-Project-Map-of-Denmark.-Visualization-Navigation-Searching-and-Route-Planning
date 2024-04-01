package dk.itu.map.structures;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dk.itu.map.structures.LinkedListSimple.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

public class Way {
    private List<Long> outerRef;
    private List<Long> innerRef;
    private Way[] tempOuterWays;
    private Way[] tempInnerWays;
    private final FloatArrayList outerCoords;
    private final FloatArrayList innerCoords;
    private final String[] tags;


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
        this.tempOuterWays = new Way[outerRef.size()];
        this.tempInnerWays = new Way[innerRef.size()];
        this.tags = new String[tags.size()];

        for (float node : nodes) {
            outerCoords.add(node);
        }

        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i] = tags.get(i);
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

    public void draw(GraphicsContext gc, float scaleFactor) {
        if (Arrays.asList(tags).contains("island")) return;
        gc.beginPath();
        drawCoords(gc, outerCoords);
        // drawCoords(gc, outerCoords);
        if (setColors(gc, tags, scaleFactor)) {
            gc.fill();
            gc.stroke();
        } else {
            gc.stroke();
        }
        gc.closePath();
        
    }

    public void drawCoords(GraphicsContext gc, FloatArrayList coords) {
        if (coords.size() == 0) return;
        float startX = 0f, startY = 0f;
        boolean startNew = true;
        for (int i = 0; i < coords.size(); i += 2) {
            if (startNew) {
                gc.moveTo(0.56f * coords.get(i + 1), -coords.get(i));
                startNew = false;
                startX = coords.get(i + 1);
                startY = coords.get(i);
                continue;
            }
            gc.lineTo(0.56f * coords.get(i + 1), -coords.get(i));
            if (startX == coords.get(i + 1) && startY == coords.get(i)) {
                startNew = true;
            }
        }
    }

    /**
     * Also only used in fileHandler
     */
    public void addRelatedWay(Way way, long id) {
        if (tempOuterWays == null || tempInnerWays == null)
            throw new RuntimeException("Related ways cannot be added to fully filled ways");

        if (outerRef.contains(id)) {
            tempOuterWays[outerRef.size() - (outerRef.indexOf(id) + 1)] = way;
        }
        if (innerRef.contains(id)) {
            tempInnerWays[innerRef.size() - (innerRef.indexOf(id) + 1) ] = way;
        }

        boolean filled = true;
        for (int i = 0; i < tempOuterWays.length; i++) {
            if (tempOuterWays[i] == null) {
                filled = false;
                break;
            }
        }
        for (int i = 0; i < tempInnerWays.length; i++) {
            if (tempInnerWays[i] == null) {
                filled = false;
                break;
            }
        }
        if (filled) {
            LinkedListSimple<Way> queuedWays = new LinkedListSimple<>(Arrays.asList(tempOuterWays));
            Node<Way> current = queuedWays.getFirst();
            Way prevWay = current.getValue();
            while (current != null) {
                Node<Way> preSearch = null;
                Node<Way> search = current;
                while (prevWay.outerCoords.get(-2) != search.getValue().outerCoords.get(0) &&
                prevWay.outerCoords.get(-1) != search.getValue().outerCoords.get(1)) {
                    // Check if way fits if it is reversed.
                    if (prevWay.outerCoords.get(-2) != search.getValue().outerCoords.get(-2) &&
                    prevWay.outerCoords.get(-1) != search.getValue().outerCoords.get(-1)) {
                        search.getValue().outerCoords.reverse();
                    }

                    preSearch = search;
                    search = search.getNext();
                }

                if (search != current) {
                    queuedWays.move(current, preSearch);
                }

                this.outerCoords.addAll(current.getValue().getCoords());
                current = current.getNext();
            }
            
            // for (int i = 0; i < innerRef.size(); i++) {
            //     this.innerCoords.addAll(tempWays[outerRef.size()+i].getCoords());
            //     // outercoords are used here cause Ways use outer by default.
            //     // FIXME: what happens if relation has relation with inner ways inside
            // }
        }
    }

    private boolean setColors(GraphicsContext gc, String[] tags, float scaleFactor) {
        // 0 = ingen stroke, ingen fill
        // 1 = ingen stroke, fill
        // 2 = stroke, ingen fill
        // 3 = stroke, fill
        gc.setStroke(Color.ORANGE);
        gc.setFill(Color.ORANGE);
        float lineWidth = 0.00001f;
        boolean shouldFill = false;
        forLoop:
        for (String tag : tags) {
            switch (tag) {

                case "motorway_link":
                case "motorway":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.DARKRED);
                    shouldFill = false;
                    break forLoop;
                case "trunk":
                case "trunk_link":
                
                case "primary":
                case "primary_link":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.DARKGRAY);
                    shouldFill = false;
                    break forLoop;

                    
                case "coastline":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.web("#332020"));
                    shouldFill = false;
                    break forLoop;
                    
                case "secondary":
                case "secondary_link":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.GRAY);
                    shouldFill = false;
                    break forLoop;
                case "rail":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.GRAY);
                    shouldFill = false;
                    break forLoop;
                case "light_rail":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.LIGHTGRAY);
                    shouldFill = false;
                    break forLoop;
                case "grassland":
                    lineWidth = 0.0003f;
                    gc.setStroke(Color.DARKGREEN);
                    gc.setFill(Color.GREEN);
                    shouldFill = true;
                    break forLoop;
                case "runway":
                    lineWidth = 0.0008f;
                    gc.setStroke(Color.GRAY);
                    shouldFill = false;
                    break forLoop;

                case "tertiary":
                case "tertiary_link":
                    lineWidth = 0.0001f;
                    gc.setStroke(Color.GRAY);
                    shouldFill = false;
                    break forLoop;
                case "heath":
                case "scrub":
                case "fell":
                    lineWidth = 0.0001f;
                    gc.setStroke(Color.DARKMAGENTA);
                    gc.setFill(Color.PURPLE);
                    shouldFill = true;
                    break forLoop;
                case "beach":
                    
                    lineWidth = 0.00000001f;
                    gc.setFill(Color.YELLOW);
                    shouldFill = true;
                    break forLoop;

                case "forest":
                    lineWidth = 0.00001f;
                    gc.setStroke(Color.GREEN);
                    gc.setFill(Color.LIGHTGREEN);
                    shouldFill = true;
                    break forLoop;
                case "water":
                    lineWidth = 0.00001f;
                    gc.setStroke(Color.BLUE);
                    gc.setFill(Color.LIGHTBLUE);
                    shouldFill = true;
                    break forLoop;
                case "unclassified":
                case "residential":
                    lineWidth = 0.0001f;
                    gc.setStroke(Color.LIGHTGRAY);
                    shouldFill = false;
                    break forLoop;
                case "building":
                    lineWidth = 0.00001f;
                    gc.setStroke(Color.LIGHTGRAY);
                    gc.setFill(Color.LIGHTGOLDENRODYELLOW);
                    shouldFill = true;
                    break forLoop;
                case "highway":
            }
            // switch (tag) {
            //     case "island":
            //         lineWidth = 0.00001f;
            //         gc.setStroke(Color.LIGHTGREEN);
            //         gc.setFill(Color.LIGHTGREEN);
            //         shouldFill = true;
            //         break forLoop;
                
            // }
        }
        gc.setLineWidth(lineWidth * scaleFactor * 0.50);
        return shouldFill;
    }

    public boolean isRelation() {
        return innerCoords.size() != 0;
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
