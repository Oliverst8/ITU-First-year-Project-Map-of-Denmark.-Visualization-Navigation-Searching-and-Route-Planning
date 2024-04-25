package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import javafx.scene.canvas.GraphicsContext;

public class DrawableWay implements Drawable {
    private final CoordArrayList outerCoords;
    private final CoordArrayList innerCoords;
    private final String[] tags;
    private String primaryType;
    private String secondaryType;
    public boolean randomColors = false;
    private final long id;

    /**
     * Only used for navigation
     */
    public DrawableWay(CoordArrayList outerCoords, String[] tags, long id, String primaryType, String secondaryType) {
        this.id = id;
        this.outerCoords = outerCoords;
        this.tags = tags;
        this.innerCoords = new CoordArrayList();
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }


    public DrawableWay(CoordArrayList outerCoords, CoordArrayList innerCoords, String[] tags, long id, String primaryType, String secondaryType) {
        this.id = id;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
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

    public String getSecondaryType(){
        return secondaryType;
    }

    public void draw(GraphicsContext gc, float scaleFactor, Theme theme) {
        gc.beginPath();
        drawCoords(gc, outerCoords);
        // drawCoords(gc, outerCoords);

        setColors(gc, tags, scaleFactor, theme);

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

    private void setColors(GraphicsContext gc, String[] tags, float scaleFactor, Theme theme ) {
        float lineWidth = 0.00001f;

        colorSelect:
        switch(primaryType) {
            case "navigation":
                switch(secondaryType) {
                    case "path", "pathToRoad":
                        lineWidth = 0.003f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "aeroway":
                switch(secondaryType) {
                    case "aerodrome", "apron":
                        lineWidth = 0.0001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "runway":
                        lineWidth = 0.005f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                    
                    case "taxiway":
                        lineWidth = 0.001f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "highway":
                switch(secondaryType) {
                    case "motorway", "motorway_link", "trunk", "trunk_link", "primary", "primary_link":
                        lineWidth = 0.001f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                    
                    case "secondary", "secondary_link", "tertiary", "tertiary_link", "service", "residential", "unclassified":
                        lineWidth = 0.0005f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "natural":
                switch(secondaryType) {
                    case "scrub", "beach":
                        lineWidth = 0.0001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "water":
                        lineWidth = 0.00001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "peninsula":
                        lineWidth = 0.0001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "place":
                switch(secondaryType) {
                    case "island", "islet":
                        lineWidth = 0.0001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "landuse":
                switch(secondaryType) {
                    case "allotments", "industrial", "residential":
                        lineWidth = 0.0001f;
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "building":
                switch(secondaryType) {
                    case "yes", "shed", "office", "college", "detached", "dormitory", "university", "apartments", "allotment_house":
                        lineWidth = 0.00001f;
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        gc.fill();
                        break colorSelect;
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
