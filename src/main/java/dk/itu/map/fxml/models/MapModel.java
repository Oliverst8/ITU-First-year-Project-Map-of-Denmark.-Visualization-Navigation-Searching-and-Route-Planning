package dk.itu.map.fxml.models;

import java.util.*;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Point;

public class MapModel {
    
    // A list that holds the different zoom levels, and their chunks
    public final List<Map<Integer, List<Drawable>>> chunkLayers;
    // The chunk loader
    public ChunkLoader chunkLoader;
    private Graph graph;

    private final Drawable[] navigationWays;

    public MapModel() {
        graph = new Graph();
        chunkLayers = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
        navigationWays = new Drawable[]{null,null,null,null,null};
    }

    /**
     * Gets the minimum longitude of the map
     * 
     * @return the minimum longitude of the map
     */
    public float getMinLon() {
        return chunkLoader.minLon;
    }

    /**
     * Gets the maximum longitude of the map
     * 
     * @return the maximum longitude of the map
     */
    public float getMinLat() {
        return chunkLoader.minLat;
    }

    /**
     * Gets the minimum latitude of the map
     * 
     * @return the minimum latitude of the map
     */
    public float getMaxLat() {
        return chunkLoader.maxLat;
    }

    /**
     * Gets the maximum latitude of the map
     * 
     * @return the maximum latitude of the map
     */
    public int getChunkAmount() {
        return chunkLoader.chunkAmount;
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

    public void setRoute(DrawableWay[] routes) {
        this.navigationWays[2] = routes[0]; //sets the navigation route from nearest usable road to nearest usable road, from start- to endpoint
        this.navigationWays[3] = routes[1]; //sets a path from the start point to the nearest usable road
        this.navigationWays[4] = routes[2]; //sets a path from the endpoint to the nearest usable road
    }

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

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Drawable[] getNavigationWays(){
        return navigationWays;
    }

}