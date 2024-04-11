package dk.itu.map.structures;

import java.io.Serializable;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawableWay implements Serializable {
    private final CoordArrayList outerCoords;
    private final CoordArrayList innerCoords;
    private final String[] tags;
    private String primaryType;

    private final long id;
    private long[] nodeIDs;

    /**
     * Only used for ChunkHandler
     */
    public DrawableWay(float[] outerCoords, float[] innerCoords, String[] tags, long id, String primaryType) {
        this.id = id;
        this.primaryType = primaryType;
        this.outerCoords = new CoordArrayList(outerCoords);
        this.innerCoords = new CoordArrayList(innerCoords);
        this.tags = tags;
    }

    // public DrawableWay(CoordArrayList coords){
    //     this.outerCoords = coords;
    // }

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

    public String getPrimaryType(){
        return primaryType;
    }

    public void draw(GraphicsContext gc, float scaleFactor) {
        gc.beginPath();
        drawCoords(gc, outerCoords);
        // drawCoords(gc, outerCoords);

        setColors(gc, tags, scaleFactor);

        gc.closePath();
    }

    public void drawCoords(GraphicsContext gc, CoordArrayList coords) {
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

    private void setColors(GraphicsContext gc, String[] tags, float scaleFactor) {
        // 0 = ingen stroke, ingen fill
        // 1 = ingen stroke, fill
        // 2 = stroke, ingen fill
        // 3 = stroke, fill
        float lineWidth = 0.00001f;
        for (int i = 0; i < tags.length; i += 2) {
            switch (tags[i]) {
                case "navigationPath":
                    lineWidth = 0.003f;
                    gc.setStroke(Color.TURQUOISE);
                    gc.stroke();
                    continue;

                case "aeroway":
                    switch (tags[i + 1]) {
                        case "aerodrome":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#E6EDF8"));
                            gc.fill();
                            continue;

                        case "runway":
                            lineWidth = 0.005f;
                            gc.setStroke(Color.web("#F3F6FF"));
                            gc.stroke();
                            continue;

                        case "taxiway":
                            lineWidth = 0.001f;
                            gc.setStroke(Color.web("#F3F6FF"));
                            gc.stroke();
                            continue;
                        
                        case "apron":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#E6EDF8"));
                            gc.fill();
                            continue;
                    }

                case "highway":
                    switch (tags[i + 1]) {
                        case "motorway", "motorway_link":
                            lineWidth = 0.001f;
                            gc.setStroke(Color.web("#8BA5C1"));
                            gc.stroke();
                            continue;

                        case "tertiary", "tertiary_link":
                            lineWidth = 0.0005f;
                            gc.setStroke(Color.web("#B1C0CF"));
                            gc.stroke();
                            continue;
                        
                        case "service", "residential", "unclassified":
                            lineWidth = 0.0005f;
                            gc.setStroke(Color.web("#B1C0CF"));
                            gc.stroke();
                            continue;

                        case "trunk", "trunk_link", "primary", "primary_link":
                            lineWidth = 0.001f;
                            gc.setStroke(Color.web("#B1C0CF"));
                            gc.stroke();
                            continue;
                        
                        case "secondary", "secondary_link":
                            lineWidth = 0.0005f;
                            gc.setStroke(Color.web("#B1C0CF"));
                            gc.stroke();
                            continue;
                    }

                case "natural":
                    switch (tags[i + 1]) {                        
                        case "scrub", "beach":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#F7ECCF"));
                            gc.fill();
                            continue;
                        
                        case "water":
                            lineWidth = 0.00001f;
                            gc.setFill(Color.web("#90DAEE"));
                            gc.fill();
                            continue;

                        case "peninsula":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#C9F5DB"));
                            gc.fill();
                            continue;
                    }
                
                case "place":
                    switch (tags[i + 1]) {
                        case "island", "islet":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#C9F5DB"));
                            gc.fill();
                            continue;
                    }
                
                case "landuse":
                    switch (tags[i + 1]) {
                        case "allotments", "industrial", "residential":
                            lineWidth = 0.0001f;
                            gc.setFill(Color.web("#F5F3F3"));
                            gc.fill();
                            continue;
                    }
                
                case "building":
                    switch (tags[i + 1]) {
                        case "yes", "shed", "office", "college", "detached", "dormitory", "university", "apartments", "allotment_house":
                            lineWidth = 0.00001f;
                            gc.setStroke(Color.web("#DBDDE8"));
                            gc.setFill(Color.web("#E8E9ED"));
                            gc.stroke();
                            gc.fill();
                            continue;
                    }
            }
        }

        gc.setLineWidth(lineWidth * Math.max(Math.log(scaleFactor) * 0.75, 0.1));
    }

    public boolean containsTag(String tag) {
        for (int i = 0; i < tags.length; i += 2) {
            if (tags[i].equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRelation() {
        return innerCoords.size() != 0;
    }

    public float[] getOuterCoords() {
        return outerCoords.toArray();
    }

    public float[] getInnerCoords() {
        return innerCoords.toArray();
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

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DrawableWay) {
            return ((DrawableWay) obj).id == id;
        }
        return false;
    }
}
