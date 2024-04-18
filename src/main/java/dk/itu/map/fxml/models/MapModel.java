package dk.itu.map.fxml.models;

import java.util.*;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Graph;

public class MapModel {
    
    // A list that holds the different zoom levels, and their chunks
    public final List<Map<Integer, List<DrawableWay>>> chunkLayers;
    // The chunk loader
    public ChunkLoader chunkLoader;
    private Graph graph;
    private float[] startPoint, endPoint;

    private Set<DrawableWay> navigationWays;

    public MapModel() {
        graph = new Graph();
        chunkLayers = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
        navigationWays = new HashSet<>();
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

    public float[] getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(float[] startPoint) {
        this.startPoint = startPoint;
    }

    public float[] getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(float[] endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * Gets the chunks in the given zoom level
     * 
     * @param zoomLevel the zoom level of the chunks
     * @return A map of the chunks in the given zoom level
     *         The key is the chunk index, and the value is a list of ways in the
     *         chunk
     */
    public Map<Integer, List<DrawableWay>> getChunksInZoomLevel(int zoomLevel) {
        return chunkLayers.get(zoomLevel);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public void setNavigationWays(Set<DrawableWay> set){
        navigationWays = set;
    }

    public Set<DrawableWay> getNavigationWays(){
        return navigationWays;
    }

}