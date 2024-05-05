package dk.itu.map.fxml.models;

import java.util.*;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.*;
import dk.itu.map.structures.TernaryTree;
import javafx.scene.paint.Color;

public class MapModel {
    // A list that holds the different zoom levels, and their chunks
    public final List<Map<Integer, List<Drawable>>> chunkLayers;
    public Set<Drawable> landLayer;
    // The chunk loader
    public ChunkLoader chunkLoader;
    private Graph graph;
    private TernaryTree address;
    public final Theme theme = new Theme();
    private final String mapType;

    private final Drawable[] navigationWays;

    /**
     * Creates a new MapModel
     * @param mapType The type of the map
     */
    public MapModel(String mapType) {
        graph = new Graph();
        this.mapType = mapType;
        chunkLayers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            chunkLayers.add(new HashMap<>());
        }
        landLayer = new HashSet<>();
        navigationWays = new Drawable[]{null, null, null, null, null};
    }

    /**
     * Get the type of the map
     * @return The type of the map
     */
    public String getMapType() {
        return mapType;
    }

    /**
     * @return the minimum longitude of the map
     */
    public float getMinLon() {
        return chunkLoader.getConfig().minLon;
    }

    /**
     * @return the maximum longitude of the map
     */
    public float getMaxLon() {
        return chunkLoader.getConfig().maxLon;
    }

    /**
     * @return the minimum latitude of the map
     */
    public float getMinLat() {
        return chunkLoader.getConfig().minLat;
    }

    /**
     * @return the maximum latitude of the map
     */
    public float getMaxLat() {
        return chunkLoader.getConfig().maxLat;
    }

    /**
     * @return the maximum latitude of the map
     */
    public int getChunkAmount() {
        return chunkLoader.getConfig().chunkAmount;
    }

    /**
     * Gets the amount of layers in the map
     * @return The amount of layers in the map
     */
    public int getLayerCount() {
        return chunkLoader.getConfig().layerCount-1;
    }

    /**
     * Gets the size of chunks
     */
    public float getChunkSize() {
        return chunkLoader.getConfig().CHUNK_SIZE;
    }

    /**
     * Gets the navigation start point
     * @return The navigation start point
     */
    public Point getStartPoint() {
        return ((Point) navigationWays[0]);
    }

    /**
     * Sets the navigation start point
     * @param startPoint The navigation start point to be set
     */
    public void setStartPoint(Point startPoint) {
        this.navigationWays[0] = startPoint;
    }

    /**
     * Gets the navigation end point
     * @return The navigation end point
     */
    public Point getEndPoint() {
        return ((Point) navigationWays[1]);
    }

    /**
     * Sets the navigation end point
     * @param endPoint The navigation end point to be set
     */
    public void setEndPoint(Point endPoint) {
        this.navigationWays[1] = endPoint;
    }

    /**
     * Sets the navigation route
     * @param endPoint The navigation end point to be set
     */
    public void setRoute(DrawableWay[] routes) {
        this.navigationWays[2] = routes[0]; //sets the navigation route from nearest usable road to nearest usable road, from start- to endpoint
        this.navigationWays[3] = routes[1]; //sets a path from the start point to the nearest usable road
        this.navigationWays[4] = routes[2]; //sets a path from the endpoint to the nearest usable road
    }

    /**
     * Removes the navigation route
     */
    public void removeRoute() {
        this.navigationWays[2] = null;
        this.navigationWays[3] = null;
        this.navigationWays[4] = null;
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

    /**
     * Gets the current graph
     * @return the current graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Sets the graph
     * @param graph The graph to be set
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
     * Get the navigation ways
     * @return The navigation ways
     */
    public Drawable[] getNavigationWays(){
        return navigationWays;
    }

    /**
     * Set the address
     * @param address The address to be set
     */
    public void setAddress(TernaryTree address) {
        this.address = address;
    }

    /**
     * Get the address
     * @return The address
     */
    public TernaryTree getAddress() {
        return address;
    }

    /**
     * Themes available for the map
     */
    public enum Themes {
        LIGHT,
        DARK,
        RANDOM,
    }

    public class Theme {
        private String name;
        private Map<String, Map<String, Map<String, Color>>> color; // Theme - primary - secondary - color

        /**
         * Creates an instance of the Theme class
         */
        public Theme() {
            this.name = "light";
            this.color = new HashMap<>();
            setLight();
            setDark();
            setRandom();
        }

        /**
         * Sets the theme used for the map
         * @param newTheme The theme to be set
         */
        public void setTheme(Themes newTheme) {
            switch (newTheme) {
                case LIGHT:
                    name = "light";
                    break;
                case DARK:
                    name = "dark";
                    break;
                case RANDOM:
                    setRandom();
                    name = "random";
                    break;
            }
        }

        /**
         * Get the color associated with the primary and secondary keys
         * @param primary The primary key
         * @param secondary The secondary key
         * @return The color associated with the primary and secondary keys
         */
        public Color getColor(String primary, String secondary) {
            return color.get(name).get(primary).get(secondary);
        }

        private void setLight() {
            Map<String, Map<String, Color>> primary = new HashMap<>();
            Map<String, Color> secondary;

            // Navigation
            secondary = new HashMap<>();
            secondary.put("path", Color.TURQUOISE);
            secondary.put("pathToRoad", Color.GREY);
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

        private void setDark() {
            Map<String, Map<String, Color>> primary = new HashMap<>();
            Map<String, Color> secondary;

            // Navigation
            secondary = new HashMap<>();
            secondary.put("path", Color.DARKCYAN);
            secondary.put("pathToRoad", Color.DARKGRAY);
            primary.put("navigation", secondary);

            // Aeroway
            secondary = new HashMap<>();
            secondary.put("aerodrome", Color.web("#2B2B2B"));
            secondary.put("apron", Color.web("#2B2B2B"));
            secondary.put("runway", Color.web("#3C3F41"));
            secondary.put("taxiway", Color.web("#3C3F41"));
            primary.put("aeroway", secondary);

            // Highway
            secondary = new HashMap<>();
            secondary.put("motorway", Color.web("#5C6370"));
            secondary.put("motorway_link", Color.web("#5C6370"));
            secondary.put("tertiary", Color.web("#6E7681"));
            secondary.put("tertiary_link", Color.web("#6E7681"));
            secondary.put("service", Color.web("#6E7681"));
            secondary.put("residential", Color.web("#6E7681"));
            secondary.put("unclassified", Color.web("#6E7681"));
            secondary.put("trunk", Color.web("#5C6370"));
            secondary.put("trunk_link", Color.web("#5C6370"));
            secondary.put("primary", Color.web("#5C6370"));
            secondary.put("primary_link", Color.web("#5C6370"));
            secondary.put("secondary", Color.web("#6E7681"));
            secondary.put("secondary_link", Color.web("#6E7681"));
            primary.put("highway", secondary);

            // Natural
            secondary = new HashMap<>();
            secondary.put("scrub", Color.web("#7F848E"));
            secondary.put("beach", Color.web("#7F848E"));
            secondary.put("water", Color.web("#4B5263"));
            secondary.put("peninsula", Color.web("#6E7681"));
            primary.put("natural", secondary);

            // Place
            secondary = new HashMap<>();
            secondary.put("island", Color.web("#6E7681"));
            secondary.put("islet", Color.web("#6E7681"));
            primary.put("place", secondary);

            // Landuse
            secondary = new HashMap<>();
            secondary.put("allotments", Color.web("#3C3F41"));
            secondary.put("industrial", Color.web("#3C3F41"));
            secondary.put("residential", Color.web("#3C3F41"));
            primary.put("landuse", secondary);

            // Building
            secondary = new HashMap<>();
            secondary.put("yes", Color.web("#4B5263"));
            secondary.put("shed", Color.web("#4B5263"));
            secondary.put("office", Color.web("#4B5263"));
            secondary.put("college", Color.web("#4B5263"));
            secondary.put("detached", Color.web("#4B5263"));
            secondary.put("dormitory", Color.web("#4B5263"));
            secondary.put("university", Color.web("#4B5263"));
            secondary.put("apartments", Color.web("#4B5263"));
            secondary.put("allotment_house", Color.web("#4B5263"));
            primary.put("building", secondary);

            color.put("dark", primary);
        }

        private void setRandom() {
            Map<String, Map<String, Color>> primary = new HashMap<>();
            Map<String, Color> secondary;

            // Navigation
            secondary = new HashMap<>();
            secondary.put("path", getRandomColor());
            secondary.put("pathToRoad", getRandomColor());
            primary.put("navigation", secondary);

            // Aeroway
            secondary = new HashMap<>();
            secondary.put("aerodrome", getRandomColor());
            secondary.put("apron", getRandomColor());
            secondary.put("runway", getRandomColor());
            secondary.put("taxiway", getRandomColor());
            primary.put("aeroway", secondary);

            // Highway
            secondary = new HashMap<>();
            secondary.put("motorway", getRandomColor());
            secondary.put("motorway_link", getRandomColor());
            secondary.put("tertiary", getRandomColor());
            secondary.put("tertiary_link", getRandomColor());
            secondary.put("service", getRandomColor());
            secondary.put("residential", getRandomColor());
            secondary.put("unclassified", getRandomColor());
            secondary.put("trunk", getRandomColor());
            secondary.put("trunk_link", getRandomColor());
            secondary.put("primary", getRandomColor());
            secondary.put("primary_link", getRandomColor());
            secondary.put("secondary", getRandomColor());
            secondary.put("secondary_link", getRandomColor());
            primary.put("highway", secondary);

            // Natural
            secondary = new HashMap<>();
            secondary.put("scrub", getRandomColor());
            secondary.put("beach", getRandomColor());
            secondary.put("water", getRandomColor());
            secondary.put("peninsula", getRandomColor());
            primary.put("natural", secondary);

            // Place
            secondary = new HashMap<>();
            secondary.put("island", getRandomColor());
            secondary.put("islet", getRandomColor());
            primary.put("place", secondary);

            // Landuse
            secondary = new HashMap<>();
            secondary.put("allotments", getRandomColor());
            secondary.put("industrial", getRandomColor());
            secondary.put("residential", getRandomColor());
            primary.put("landuse", secondary);

            // Building
            secondary = new HashMap<>();
            secondary.put("yes", getRandomColor());
            secondary.put("shed", getRandomColor());
            secondary.put("office", getRandomColor());
            secondary.put("college", getRandomColor());
            secondary.put("detached", getRandomColor());
            secondary.put("dormitory", getRandomColor());
            secondary.put("university", getRandomColor());
            secondary.put("apartments", getRandomColor());
            secondary.put("allotment_house", getRandomColor());
            primary.put("building", secondary);

            color.put("random", primary);
        }

        private Color getRandomColor() {
            return Color.color(Math.random(), Math.random(), Math.random());
        }
    }
}
