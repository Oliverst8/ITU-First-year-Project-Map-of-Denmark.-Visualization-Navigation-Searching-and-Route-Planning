package dk.itu.map.structures;

import java.io.Serializable;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawableWay implements Drawable {
    private final CoordArrayList outerCoords;
    private final CoordArrayList innerCoords;
    private final String[] tags;
    private String primaryType;
    public boolean randomColors = false;
    private final long id;

    /**
     * Only used for navigation
     */
    public DrawableWay(CoordArrayList outerCoords, String[] tags, long id){
        this.id = id;
        this.outerCoords = outerCoords;
        this.tags = tags;
        this.innerCoords = new CoordArrayList();
    }

    /**
     * Only used for ChunkHandler
     */
    public DrawableWay(CoordArrayList outerCoords, CoordArrayList innerCoords, String[] tags, long id, String primaryType) {
        this.id = id;
        this.primaryType = primaryType;
        this.outerCoords = outerCoords;
        this.innerCoords = innerCoords;
        this.tags = tags;
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

    public String getPrimaryType(){
        return primaryType;
    }

    public void draw(GraphicsContext gc, float scaleFactor, int themeNumber) {
        gc.beginPath();
        drawCoords(gc, outerCoords);
        // drawCoords(gc, outerCoords);

        setColors(gc, tags, scaleFactor, themeNumber);

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

    private void setColors(GraphicsContext gc, String[] tags, float scaleFactor, int themeNumber) {
        // 0 = ingen stroke, ingen fill
        // 1 = ingen stroke, fill
        // 2 = stroke, ingen fill
        // 3 = stroke, fill
        float lineWidth = 0.00001f;
        for (int i = 0; i < tags.length; i += 2) {
            switch (tags[i]) {
                case "navigationPath":
                    lineWidth = 0.003f;
                    if (themeNumber == 0) {
                        gc.setStroke(Color.TURQUOISE);
                    }
                    if (themeNumber == 2) {
                        gc.setStroke(Color.web("#00FF00"));
                    }
                    gc.stroke();
                    continue;

                case "pathToRoad":
                    lineWidth = 0.003f;
                    if (themeNumber == 0) {
                        gc.setStroke(Color.GREY);
                    }
                    if (themeNumber == 2) {
                        gc.setStroke(Color.GREY);
                    }
                    gc.stroke();
                    continue;

                case "aeroway":
                    switch (tags[i + 1]) {
                        case "aerodrome":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#E6EDF8"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#FF69B4"));
                            }
                            gc.fill();
                            continue;

                        case "runway":
                            lineWidth = 0.005f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#F3F6FF"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#00BFFF"));
                            }
                            gc.stroke();
                            continue;

                        case "taxiway":
                            lineWidth = 0.001f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#F3F6FF"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#00BFFF"));
                            }
                            gc.stroke();
                            continue;
                        
                        case "apron":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#E6EDF8"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#FF69B4"));
                            }
                            gc.fill();
                            continue;
                    }

                case "highway":
                    switch (tags[i + 1]) {
                        case "motorway", "motorway_link":
                            lineWidth = 0.001f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#8BA5C1"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#FFD700"));
                            }
                            gc.stroke();
                            continue;

                        case "tertiary", "tertiary_link":
                            lineWidth = 0.0005f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#B1C0CF"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#FFA07A"));
                            }
                            gc.stroke();
                            continue;
                        
                        case "service", "residential", "unclassified":
                            lineWidth = 0.0005f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#B1C0CF"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#FFA07A"));
                            }
                            gc.stroke();
                            continue;

                        case "trunk", "trunk_link", "primary", "primary_link":
                            lineWidth = 0.001f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#8BA5C1"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#FFD700"));
                            }
                            gc.stroke();
                            continue;
                        
                        case "secondary", "secondary_link":
                            lineWidth = 0.0005f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#B1C0CF"));
                            }
                            if (themeNumber == 2) {
                                gc.setStroke(Color.web("#FFA07A"));
                            }
                            gc.stroke();
                            continue;
                    }

                case "natural":
                    switch (tags[i + 1]) {                        
                        case "scrub", "beach":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#F7ECCF"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#FFD700"));
                            }
                            gc.fill();
                            continue;
                        
                        case "water":
                            lineWidth = 0.00001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#90DAEE"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#00CED1"));
                            }
                            gc.fill();
                            continue;

                        case "peninsula":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#C9F5DB"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#7FFFD4"));
                            }
                            gc.fill();
                            continue;
                    }
                
                case "place":
                    switch (tags[i + 1]) {
                        case "island", "islet":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#C9F5DB"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#7FFFD4"));
                            }
                            gc.fill();
                            continue;
                    }
                
                case "landuse":
                    switch (tags[i + 1]) {
                        case "allotments", "industrial", "residential":
                            lineWidth = 0.0001f;
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#F5F3F3"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#FF4500"));
                            }
                            gc.fill();
                            continue;
                    }
                
                case "building":
                    switch (tags[i + 1]) {
                        case "yes", "shed", "office", "college", "detached", "dormitory", "university", "apartments", "allotment_house":
                            lineWidth = 0.00001f;
                            if (themeNumber == 0) {
                                gc.setStroke(Color.web("#DBDDE8"));
                            }
                            if (themeNumber == 0) {
                                gc.setFill(Color.web("#E8E9ED"));
                            }
                            if (themeNumber == 2) {
                                gc.setFill(Color.web("#FF007F"));
                            }
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

    public void setRandomColors(){ randomColors = !randomColors; }

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
