package dk.itu.map.structures;

import dk.itu.map.fxml.models.MapModel.Theme;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import javafx.scene.canvas.GraphicsContext;

public class DrawableWay implements Drawable {
    private final CoordArrayList outerCoords;
    private final CoordArrayList innerCoords;
    private String primaryType;
    private String secondaryType;
    private final long id;

    /**
     * Only used for navigation
     */
    public DrawableWay(CoordArrayList outerCoords, long id, String primaryType, String secondaryType) {
        this.id = id;
        this.outerCoords = outerCoords;
        this.innerCoords = new CoordArrayList();
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
    }

    /**
     * Create a new DrawableWay
     * @param outerCoords the outer coordinates of the way
     * @param innerCoords the inner coordinates of the way
     * @param id the id of the way
     * @param primaryType the primary type of the way used for coloring
     * @param secondaryType the secondary type of the way used for coloring
     */
    public DrawableWay(CoordArrayList outerCoords, CoordArrayList innerCoords, long id, String primaryType, String secondaryType) {
        this.id = id;
        this.primaryType = primaryType;
        this.secondaryType = secondaryType;
        this.outerCoords = outerCoords;
        this.innerCoords = innerCoords;
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

        return builder.toString();
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

    /**
     * @return the primary type of the way
     */
    public String getPrimaryType(){
        return primaryType;
    }

    /**
     * @return the secondary type of the way
     */
    public String getSecondaryType(){
        return secondaryType;
    }

    /**
     * Draw the way on the canvas using the given graphics context
     */
    public void draw(GraphicsContext gc, float scaleFactor, int skipAmount, Theme theme) {
        gc.beginPath();
        drawCoords(gc, outerCoords, skipAmount);

        setColors(gc, scaleFactor, theme);

        gc.closePath();
    }

    /**
     * Draw the coordinates on the canvas
     * @param gc the graphics context to draw on
     * @param coords the coordinates to draw
     * @param skipAmount the amount of coordinates to skip
     */
    private void drawCoords(GraphicsContext gc, CoordArrayList coords, int skipAmount) {
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
            if (i % skipAmount == 0 || i == coords.size()-1) gc.lineTo(0.56f * coord[1], -coord[0]);
            if (startX == coord[1] && startY == coord[0]) {
                startNew = true;
            }
        }
    }

    /**
     * Set the colors of the way
     * @param gc the graphics context to apply the colors to
     * @param scaleFactor the scale factor of the map to be used
     * @param theme the theme to be used
     */
    private void setColors(GraphicsContext gc, float scaleFactor, Theme theme ) {
        gc.setStroke(theme.getColor("highway", "residential"));
        colorSelect:
        switch(primaryType) {
            case "navigation":
                switch(secondaryType) {
                    case "path", "pathToRoad":
                        gc.setLineWidth(calcLineWidth(0.003f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "aeroway":
                switch(secondaryType) {
                    case "aerodrome", "apron":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "runway":
                        gc.setLineWidth(calcLineWidth(0.005f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                    
                    case "taxiway":
                        gc.setLineWidth(calcLineWidth(0.001f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "highway":
                switch(secondaryType) {
                    case "motorway", "motorway_link", "trunk", "trunk_link", "primary", "primary_link":
                        gc.setLineWidth(calcLineWidth(0.001f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                    
                    case "secondary", "secondary_link", "tertiary", "tertiary_link", "service", "residential", "unclassified", "living_street", "pedestrian", "road", "track", "path", "footway", "bridleway", "steps", "cycleway", "corridor":
                        gc.setLineWidth(calcLineWidth(0.0005f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        break colorSelect;
                }

            case "natural":
                switch(secondaryType) {
                    case "scrub", "beach":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "water":
                        gc.setLineWidth(calcLineWidth(0.00001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                    
                    case "peninsula":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "place":
                switch(secondaryType) {
                    case "island", "islet":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "landuse":
                switch(secondaryType) {
                    case "allotments", "industrial", "residential", "military":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }
            
            case "leisure":
                switch(secondaryType) {
                    case "park":
                        gc.setLineWidth(calcLineWidth(0.0001f, scaleFactor));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.fill();
                        break colorSelect;
                }

            case "building":
                switch(secondaryType) {
                    case "yes", "shed", "office", "college", "detached", "dormitory", "university", "apartments", "allotment_house", "commercial", "school":
                        gc.setLineWidth(calcLineWidth(0.00001f, scaleFactor));
                        gc.setStroke(theme.getColor(primaryType, secondaryType));
                        gc.setFill(theme.getColor(primaryType, secondaryType));
                        gc.stroke();
                        gc.fill();
                        break colorSelect;
                }
        }
    }

    /**
     * @return true if the way is a relation, false otherwise
     */
    public boolean isRelation() {
        return innerCoords.size() != 0;
    }

    /**
     * @return the outer coordinates of the way
     */
    public CoordArrayList getOuterCoords() {
        return outerCoords;
    }

    /**
     * @return the inner coordinates of the way
     */
    public CoordArrayList getInnerCoords() {
        return innerCoords;
    }

    /**
     * @return the id of the way
     */
    public long getId() {
        return id;
    }
}
