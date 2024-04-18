package dk.itu.map.fxml.controllers;

import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.UtilityLoader;
import dk.itu.map.structures.DrawableWay;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import javafx.geometry.Point2D;

public class MapController {

    private MapModel model;

    /**
     * Constructor for the MapController, set the following variables
     * 
     * @param model
     */
    public MapController(MapModel model) {
        this.model = model;
    }

    /**
     * Imports a map from a file
     * 
     * @param osmFile The path to the file To be deleted
     * @param mapName The name of the map to be saved to
     */
    public void importMap(String osmFile, String mapName) {
        
        UtilityLoader utilityLoader = new UtilityLoader("maps/" + mapName);
        utilityLoader.start();
        
        model.chunkLoader = new ChunkLoader("maps/" + mapName);

        setUtilities(utilityLoader);
    }

    /**
     * Gets the chunks in the smallest rectangle that contains both chunk1 and
     * chunk2
     * 
     * @param chunk1      the first chunk in the rectangle
     * @param chunk2      the second chunk in the rectangle
     * @param columAmount the amount of chunks in a column
     * @return a set of chunks in the smallest rectangle
     */
    private Set<Integer> getChunksInRect(int chunk1, int chunk2, int columAmount) {
        Set<Integer> chunks = new HashSet<>();

        int a = Math.min(chunk1, chunk2);
        int b = Math.max(chunk1, chunk2);

        int height = b / columAmount - a / columAmount;
        int width = Math.abs(a % columAmount - b % columAmount);

        int rightMost = height > 0 ? a : b;

        for (int i = 0; i <= height; i++) {
            int c = rightMost + i * columAmount;
            for (int j = 0; j <= width; j++) {
                chunks.add(c - j);
            }
        }

        return chunks;
    }

    /**
     * Reads the chunks from the set on at the zoom level
     * and puts them in the chunkLayers
     * Chunks are not read if they already are read
     * 
     * @param chunkSet  the set of chunks to be read
     * @param zoomLevel the zoom level of the chunks
     */
    private void readChunks(Set<Integer> chunkSet, int zoomLevel) {
        Map<Integer, List<DrawableWay>> chunks = model.chunkLayers.get(zoomLevel);

        chunkSet.removeAll(chunks.keySet());

        int[] newChunks = new int[chunkSet.size()];

        int c = 0;
        for (int chunk : chunkSet) {
            newChunks[c++] = chunk;
        }

        if (chunkSet.isEmpty())
            return;

        chunks.putAll(model.chunkLoader.readFiles(newChunks, zoomLevel));
    }

    private void setUtilities(UtilityLoader utilityLoader) {
        try {
            utilityLoader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        model.setGraph(utilityLoader.getGraph());
    }

    /**
     * Updates the given zoom level with the given chunks
     * 
     * @param chunks    the chunks to be updated
     * @param zoomLevel the zoom level to be updated
     */
    private void updateZoomLayer(Set<Integer> chunks, int zoomLevel) {
        for (Map<Integer, List<DrawableWay>> chunkLayers : model.chunkLayers) {
            chunkLayers.keySet().retainAll(chunks);
        }

        for (int i = zoomLevel; i <= 4; i++) {
            readChunks(chunks, i);
        }
    }

    /**
     * @param detailLevel      The current detail level
     * @param upperLeftCorner  The upper left corner of the current view
     * @param lowerRightCorner The lower right corner of the current view
     * @return The amount of chunks seen in the current view in the y direction
     */
    public float updateChunks(int detailLevel, Point2D upperLeftCorner, Point2D lowerRightCorner) {
        int upperLeftChunk = model.chunkLoader.pointToChunkIndex(upperLeftCorner);
        int lowerRightChunk = model.chunkLoader.pointToChunkIndex(lowerRightCorner);

        Set<Integer> chunks = getChunksInRect(upperLeftChunk, lowerRightChunk, model.chunkLoader.chunkColumnAmount);

        float currentChunkAmountSeen = (float) (Math.abs(upperLeftCorner.getY() - lowerRightCorner.getY())
                / model.chunkLoader.CHUNK_SIZE);

        updateZoomLayer(chunks, detailLevel);

        return currentChunkAmountSeen;
    }
}
