package dk.itu.map.fxml.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.Way;

public class MapModel {
    
    // The chunk loader
    public ChunkLoader chunkLoader;
    // A list that holds the different zoom levels, and their chunks
    private final List<Map<Integer, List<Way>>> chunkLayers;

    public int height;
    public int width;

    public MapModel() {
        chunkLayers = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
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

    /**
     * Gets the chunks in the given zoom level
     * 
     * @param zoomLevel the zoom level of the chunks
     * @return A map of the chunks in the given zoom level
     *         The key is the chunk index, and the value is a list of ways in the
     *         chunk
     */
    public Map<Integer, List<Way>> getChunksInZoomLevel(int zoomLevel) {
        return chunkLayers.get(zoomLevel);
    }
}