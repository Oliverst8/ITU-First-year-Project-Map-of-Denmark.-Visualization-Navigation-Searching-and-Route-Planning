package dk.itu.map;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Way;
import dk.itu.map.parser.OSMParser;
import dk.itu.map.parser.ChunkLoader;
import javafx.geometry.Point2D;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import java.util.*;

import javax.xml.stream.XMLStreamException;

public class Model implements Serializable {
    // The path to the data folder
    private final String dataPath;
    // A list that holds the different zoom levels, and their chunks
    private final List<Map<Integer,List<Way>>> chunkLayers;
    // The chunk loader
    private ChunkLoader chunkLoader;
    private final Graph graph;

    /**
     * Constructor for the Model class
     * Initializes the dataPath and the chunkLayers
     */
    public Model() {
        graph = new Graph();
        dataPath = "maps";
        chunkLayers = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
    }

    /**
     * Imports a map from a file
     * @param filePath The path to the file
     * @param name The name of the map to be saved to
     */
    public void importMap(String filePath, String name) {
        try {            
            if (!new File(dataPath + "/" + name + "/config").exists()) {
                OSMParser OSMParser = new OSMParser(new File(filePath), dataPath + "/" + name);
                OSMParser.load();
                System.out.println("Finished importing map!");
            };
            String path = dataPath + "/" + name;
            chunkLoader = new ChunkLoader(dataPath + "/" + name);
            graph.loadFromDataPath(path + "/utilities");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the chunks in the smallest rectangle that contains both chunk1 and chunk2
     * @param chunk1 the first chunk in the rectangle
     * @param chunk2 the second chunk in the rectangle
     * @param columAmount the amount of chunks in a column
     * @return a set of chunks in the smallest rectangle
     */
    private Set<Integer> getChunksInRect(int chunk1, int chunk2, int columAmount){
        Set<Integer> chunks = new HashSet<>();

        int a = Math.min(chunk1, chunk2);
        int b = Math.max(chunk1, chunk2);

        int height = b/columAmount - a/columAmount;
        int width = Math.abs(a%columAmount-b%columAmount);

        int rightMost = height > 0 ? a : b;

        for(int i = 0; i <= height; i++){
            int c = rightMost + i*columAmount;
            for(int j = 0; j <= width; j++){
                chunks.add(c-j);
            }
        }

        return chunks;
    }

    /**
     * Reads the chunks from the set on at the zoom level
     * and puts them in the chunkLayers
     * Chunks are not read if they already are read
     * @param chunkSet the set of chunks to be read
     * @param zoomLevel the zoom level of the chunks
     */
    private void readChunks(Set<Integer> chunkSet, int zoomLevel) {
        Map<Integer, List<Way>> chunks = chunkLayers.get(zoomLevel);

        chunkSet.removeAll(chunks.keySet());

        int[] newChunks = new int[chunkSet.size()];

        int c = 0;
        for (int chunk : chunkSet) {
            newChunks[c++] = chunk;
        }

        if(chunkSet.isEmpty()) return;

        chunks.putAll(chunkLoader.loadBytes(newChunks, zoomLevel));
    }

    /**
     * Updates the given zoom level with the given chunks
     * @param chunks the chunks to be updated
     * @param zoomLevel the zoom level to be updated
     */
    private void updateZoomLayer(Set<Integer> chunks, int zoomLevel) {
        for(Map<Integer, List<Way>> chunkLayers : chunkLayers) {
            chunkLayers.keySet().retainAll(chunks);
        }

        for(int i = zoomLevel; i <= 4; i++) {
            readChunks(chunks, i);
        }
    }

    /**
     * Gets the minimum longitude of the map
     * @return the minimum longitude of the map
     */
    public float getMinLon(){
        return chunkLoader.minLon;
    }

    /**
     * Gets the maximum longitude of the map
     * @return the maximum longitude of the map
     */
    public float getMinLat(){
        return chunkLoader.minLat;
    }

    /**
     * Gets the minimum latitude of the map
     * @return the minimum latitude of the map
     */
    public float getMaxLat(){
        return chunkLoader.maxLat;
    }

    /**
     * Gets the maximum latitude of the map
     * @return the maximum latitude of the map
     */
    public int getChunkAmount(){
        return chunkLoader.chunkAmount;
    }

    /**
     * @param detailLevel The current detail level
     * @param upperLeftCorner The upper left corner of the current view
     * @param lowerRightCorner The lower right corner of the current view
     * @return The amount of chunks seen in the current view in the y direction
     */
    public float updateChunks(int detailLevel, Point2D upperLeftCorner, Point2D lowerRightCorner) {
        int upperLeftChunk = chunkLoader.pointToChunkIndex(upperLeftCorner);
        int lowerRightChunk = chunkLoader.pointToChunkIndex(lowerRightCorner);

        Set<Integer> chunks = getChunksInRect(upperLeftChunk, lowerRightChunk, chunkLoader.chunkColumnAmount);

        float currentChunkAmountSeen = (float) (Math.abs(upperLeftCorner.getY() - lowerRightCorner.getY()) / chunkLoader.CHUNK_SIZE);

        updateZoomLayer(chunks, detailLevel);

        return currentChunkAmountSeen;
    }

    /**
     * Gets the chunks in the given zoom level
     * @param zoomLevel the zoom level of the chunks
     * @return A map of the chunks in the given zoom level
     * The key is the chunk index, and the value is a list of ways in the chunk
     */
    public Map<Integer, List<Way>> getChunksInZoomLevel(int zoomLevel) {
        return chunkLayers.get(zoomLevel);
    }

    public Graph getGraph() {
        return graph;
    }
}
