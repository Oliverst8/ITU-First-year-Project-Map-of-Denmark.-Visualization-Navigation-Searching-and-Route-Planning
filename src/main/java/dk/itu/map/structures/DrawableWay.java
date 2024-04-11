package dk.itu.map.structures;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.SimpleLinkedList.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawableWay implements Serializable {
    private List<Long> outerRef;
    private List<Long> innerRef;
    private DrawableWay[] tempOuterDrawableWays;
    private DrawableWay[] tempInnerDrawableWays;
    private final CoordArrayList outerCoords;
    private CoordArrayList innerCoords;
    private String[] tags;


    private long id;
    private long[] nodeIDs;

    /**
     * Only use for OSMParser
     */
    public DrawableWay(List<Float> nodes, List<String> tags, List<Long> outerRef, List<Long> innerRef) {
        outerCoords = new CoordArrayList();
        innerCoords = new CoordArrayList();
        this.outerRef = outerRef;
        this.innerRef = innerRef;
        this.tempOuterDrawableWays = new DrawableWay[outerRef.size()];
        this.tempInnerDrawableWays = new DrawableWay[innerRef.size()];
        this.tags = new String[tags.size()];

        for(int i = 0; i < nodes.size(); i += 2) {
            outerCoords.add(nodes.get(i), nodes.get(i + 1));
        }

        for (int i = 0; i < this.tags.length; i++) {
            this.tags[i] = tags.get(i);
        }
    }

    /**
     * Only used for ChunkHandler
     */
    public DrawableWay(CoordArrayList outerCoords, CoordArrayList innerCoords, String[] tags) {
        this.outerCoords = outerCoords;
        this.innerCoords = innerCoords;
        this.tags = tags;
    }

    public DrawableWay(CoordArrayList coords){
        this.outerCoords = coords;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DrawableWay) {
            DrawableWay other = (DrawableWay) obj;
            if (other.outerCoords.size() != outerCoords.size() || other.innerCoords.size() != innerCoords.size()) {
                return false;
            }
            for (int i = 0; i < outerCoords.size(); i++) {
                if (other.outerCoords.get(i) != outerCoords.get(i)) {
                    return false;
                }
            }
            for (int i = 0; i < innerCoords.size(); i++) {
                if (other.innerCoords.get(i) != innerCoords.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Nodes:\n");
        builder.append(outerCoords.size());
        builder.append("\n");
        for (int i = 0; i < outerCoords.size(); i++) {
            float[] coord = outerCoords.get(i);
            builder.append(coord[0]);
            builder.append(" ");
            builder.append(coord[1]);
            builder.append("\n");
        }
        builder.append("Inner nodes:\n");
        builder.append(innerCoords.size());
        builder.append("\n");
        for (int i = 0; i < innerCoords.size(); i++) {
            float[] coord = innerCoords.get(i);
            builder.append(coord[0]);
            builder.append(" ");
            builder.append(coord[1]);
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
            float[] coord = outerCoords.get(i);
            stream.writeFloat(coord[0]);
            stream.writeFloat(coord[1]);
        }
        stream.writeInt(innerCoords.size());
        for (int i = 0; i < innerCoords.size(); i++) {
            float[] coord = innerCoords.get(i);
            stream.writeFloat(coord[0]);
            stream.writeFloat(coord[1]);
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
     
    public void drawCoords(GraphicsContext gc, CoordArrayList coords) {
        if (coords.size() == 0) return;
        float startX = 0f, startY = 0f;
        boolean startNew = true;
        for (int i = 0; i < coords.size(); i++) {
            float[] coord = coords.get(i);
            if (startNew) {
                gc.moveTo(0.56f * coord[1], -coord[0]);
                startNew = false;
                startX = coord[1];
                startY = coord[0];
                continue;
            }
            gc.lineTo(0.56f * coord[1], -coord[0]);
            if (startX == coord[1] && startY == coord[0]) {
                startNew = true;
            }
        }
    }

    /**
     * Also only used in fileHandler gg
     */
    public void addRelatedWay(DrawableWay drawableWay, long id) {
        if (tempOuterDrawableWays == null || tempInnerDrawableWays == null)
            throw new RuntimeException("Related ways cannot be added to fully filled ways");

        if (outerRef.contains(id)) {
            tempOuterDrawableWays[outerRef.size() - (outerRef.indexOf(id) + 1)] = drawableWay;
        }
        if (innerRef.contains(id)) {
            tempInnerDrawableWays[innerRef.size() - (innerRef.indexOf(id) + 1) ] = drawableWay;
        }

        boolean filled = true;
        for (int i = 0; i < tempOuterDrawableWays.length; i++) {
            if (tempOuterDrawableWays[i] == null) {
                filled = false;
                break;
            }
        }
        for (int i = 0; i < tempInnerDrawableWays.length; i++) {
            if (tempInnerDrawableWays[i] == null) {
                filled = false;
                break;
            }
        }
        if (filled) {
            SimpleLinkedList<DrawableWay> queuedWays = new SimpleLinkedList<>(Arrays.asList(tempOuterDrawableWays));
            if (tempOuterDrawableWays.length == 0) return;

            Node<DrawableWay> current = queuedWays.getFirst();
            DrawableWay currentDrawableWay;
            DrawableWay initialDrawableWay = current.getValue();
            while (current != null) {
                currentDrawableWay = current.getValue();
                Node<DrawableWay> preSearch = current;
                Node<DrawableWay> search = current;

                if (current.getNext() == null) {
                    this.outerCoords.addAll(current.getValue().getCoords());
                    break;
                }

                if (currentDrawableWay.outerCoords.get(-2) == initialDrawableWay.outerCoords.get(0) &&
                currentDrawableWay.outerCoords.get(-1) == initialDrawableWay.outerCoords.get(1)) {
                    this.outerCoords.addAll(current.getValue().getCoords());
                    initialDrawableWay = current.getNext().getValue();
                    current = current.getNext();

                    continue;
                }

                while (currentDrawableWay.outerCoords.get(-2) != search.getValue().outerCoords.get(0) ||
                currentDrawableWay.outerCoords.get(-1) != search.getValue().outerCoords.get(1)) {
                    // Check if way fits if it is reversed.
                    if (currentDrawableWay.outerCoords.get(-2) == search.getValue().outerCoords.get(-2) &&
                    currentDrawableWay.outerCoords.get(-1) == search.getValue().outerCoords.get(-1) && currentDrawableWay != search.getValue()) {
                        search.getValue().outerCoords.reverse();
                        break;
                    }

                    preSearch = search;
                    search = search.getNext();
                    if (search == null) {
                        System.out.println("Could not find next way in relation");
                    }
                }

                if (search != current) {
                    queuedWays.move(current, preSearch);
                }

                this.outerCoords.addAll(current.getValue().getCoords());
                current = current.getNext();
            }
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

                case "navigationPath":
                    lineWidth = 0.003f;
                    gc.setStroke(Color.TURQUOISE);
                    shouldFill = false;
                    break forLoop;

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
                case "island":
                    lineWidth = 0.00001f;
                    gc.setStroke(Color.LIGHTGREEN);
                    gc.setFill(Color.LIGHTGREEN);
                    shouldFill = true;
                    break forLoop;
                case "highway":
            }
            // switch (tag) {

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
