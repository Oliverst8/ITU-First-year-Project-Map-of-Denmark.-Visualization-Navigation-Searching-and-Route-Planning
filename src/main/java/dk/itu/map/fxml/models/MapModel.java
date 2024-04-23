package dk.itu.map.fxml.models;

import java.util.*;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Point;
import javafx.scene.paint.Color;

public class MapModel {
    
    // A list that holds the different zoom levels, and their chunks
    public final List<Map<Integer, List<Drawable>>> chunkLayers;
    // The chunk loader
    public ChunkLoader chunkLoader;
    private Graph graph;
    public final Theme theme = new Theme();

    private final Drawable[] navigationWays;

    public MapModel() {
        graph = new Graph();
        chunkLayers = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
        navigationWays = new Drawable[]{null,null,null};
    }

    

    /**
     * Gets the minimum longitude of the map
     * 
     * @return the minimum longitude of the map
     */
    public float getMinLon() {
        return chunkLoader.getConfig().minLon;
    }

    /**
     * Gets the maximum longitude of the map
     * 
     * @return the maximum longitude of the map
     */
    public float getMinLat() {
        return chunkLoader.getConfig().minLat;
    }

    /**
     * Gets the minimum latitude of the map
     * 
     * @return the minimum latitude of the map
     */
    public float getMaxLat() {
        return chunkLoader.getConfig().maxLat;
    }

    /**
     * Gets the maximum latitude of the map
     * 
     * @return the maximum latitude of the map
     */
    public int getChunkAmount() {
        return chunkLoader.getConfig().chunkAmount;
    }

    public Point getStartPoint() {
        return ((Point) navigationWays[0]);
    }

    public void setStartPoint(Point startPoint) {
        this.navigationWays[0] = startPoint;
    }

    public Point getEndPoint() {
        return ((Point) navigationWays[1]);
    }

    public void setEndPoint(Point endPoint) {
        this.navigationWays[1] = endPoint;
    }

    public void setRoute(DrawableWay route) {
        this.navigationWays[2] = route;
    }

    public void removeRoute() {
        this.navigationWays[2] = null;
    }

    /**
     * Gets the chunks in the given zoom level
     * 
     * @param zoomLevel the zoom level of the chunks
     * @return A map of the chunks in the given zoom level
     *         The key is the chunk index, and the value is a list of ways in the
     *         chunk
     */
    public Map<Integer, List<Drawable>> getChunksInZoomLevel(int zoomLevel) {
        return chunkLayers.get(zoomLevel);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Drawable[] getNavigationWays(){
        return navigationWays;
    }

    public enum Themes {
        LIGHT,
        DARK,
        Wierd,
    }

    public class Theme {
        private String name;
        private Map<String, Map<String, Map<String, Color>>> color; // Theme - primary - secondary - color

        public Theme() {
            this.name = "light";
            this.color = new HashMap<>();
            setLight();
            //setDark();
            //setWierd();
        }

        public void setTheme(Themes newTheme) {
            switch (newTheme) {
                case LIGHT:
                    name = "light";
                    break;
                case DARK:
                    name = "dark";
                    break;
                case Wierd:
                    name = "wierd";
                    break;
            }
        }

        public Color getColor(String primary, String secondary) {
            return color.get(name).get(primary).get(secondary);
        }

        private void setLight() {
            Map<String, Map<String, Color>> primary = new HashMap<>();
            Map<String, Color> secondary;

            // Navigation
            secondary = new HashMap<>();
            secondary.put("path", Color.TURQUOISE);
            primary.put("navigation", secondary);

            // Aeroway
            secondary = new HashMap<>();
            secondary.put("aerodrome", Color.web("#E6EDF8"));
            secondary.put("apron", Color.web("#E6EDF8"));
            secondary.put("runway", Color.web("#F3F6FF"));
            secondary.put("taxiway", Color.web("#F3F6FF"));
            primary.put("aeroway", secondary);

            // Highway
            secondary = new HashMap<>();
            secondary.put("motorway", Color.web("#8BA5C1"));
            secondary.put("motorway_link", Color.web("#8BA5C1"));
            secondary.put("tertiary", Color.web("#B1C0CF"));
            secondary.put("tertiary_link", Color.web("#B1C0CF"));
            secondary.put("service", Color.web("#B1C0CF"));
            secondary.put("residential", Color.web("#B1C0CF"));
            secondary.put("unclassified", Color.web("#B1C0CF"));
            secondary.put("trunk", Color.web("#8BA5C1"));
            secondary.put("trunk_link", Color.web("#8BA5C1"));
            secondary.put("primary", Color.web("#8BA5C1"));
            secondary.put("primary_link", Color.web("#8BA5C1"));
            secondary.put("secondary", Color.web("#B1C0CF"));
            secondary.put("secondary_link", Color.web("#B1C0CF"));
            primary.put("highway", secondary);

            // Natural
            secondary = new HashMap<>();
            secondary.put("scrub", Color.web("#F7ECCF"));
            secondary.put("beach", Color.web("#F7ECCF"));
            secondary.put("water", Color.web("#90DAEE"));
            secondary.put("peninsula", Color.web("#C9F5DB"));
            primary.put("natural", secondary);

            // Place
            secondary = new HashMap<>();
            secondary.put("island", Color.web("#C9F5DB"));
            secondary.put("islet", Color.web("#C9F5DB"));
            primary.put("place", secondary);

            // Landuse
            secondary = new HashMap<>();
            secondary.put("allotments", Color.web("#F5F3F3"));
            secondary.put("industrial", Color.web("#F5F3F3"));
            secondary.put("residential", Color.web("#F5F3F3"));
            primary.put("landuse", secondary);

            // Building
            secondary = new HashMap<>();
            secondary.put("yes", Color.web("#DBDDE8"));
            secondary.put("shed", Color.web("#DBDDE8"));
            secondary.put("office", Color.web("#DBDDE8"));
            secondary.put("college", Color.web("#DBDDE8"));
            secondary.put("detached", Color.web("#DBDDE8"));
            secondary.put("dormitory", Color.web("#DBDDE8"));
            secondary.put("university", Color.web("#DBDDE8"));
            secondary.put("apartments", Color.web("#DBDDE8"));
            secondary.put("allotment_house", Color.web("#DBDDE8"));
            primary.put("building", secondary);
            
            color.put("light", primary);
        }
    }
}