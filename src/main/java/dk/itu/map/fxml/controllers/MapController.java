package dk.itu.map.fxml.controllers;

import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.structures.Way;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class MapController {

    // Zoom level
    private float zoomLevel;
    // Initial distance between two points
    private float startDist;

    // Amount of chunks seen
    private float currentChunkAmountSeen = 1;

    private MapModel model;

    /**
     * Constructor for the MapController, set the following variables
     * 
     * @param model
     */
    public MapController(MapModel model) {
        this.model = model;
    }

    public void setup(Canvas canvas) {
    }

    /**
     * Imports a map from a file
     * 
     * @param osmFile The path to the file To be deleted
     * @param mapName The name of the map to be saved to
     */
    public void importMap(String osmFile, String mapName) {
        model.chunkLoader = new ChunkLoader("maps/" + mapName);
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
        Map<Integer, List<Way>> chunks = model.chunkLayers.get(zoomLevel);

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

    /**
     * Updates the given zoom level with the given chunks
     * 
     * @param chunks    the chunks to be updated
     * @param zoomLevel the zoom level to be updated
     */
    private void updateZoomLayer(Set<Integer> chunks, int zoomLevel) {
        for (Map<Integer, List<Way>> chunkLayers : model.chunkLayers) {
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

    /**
     * @return Point2D the upper left corner of the canvas
     */
    private Point2D getUpperLeftCorner() {
        return convertTo2DPoint(0, 0);
    }

    /**
     * @return Point2D the lower right corner of the canvas
     */
    private Point2D getLowerRightCorner() {
        return convertTo2DPoint(model.width, model.height);
    }

    /**
     * Pans the map
     * 
     * @param dx the upper left X corner to be panned to
     * @param dy the upper left Y corner to be panned to
     */
    public void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * Redraws the map
     */
    private void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, model.width, model.height);
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);

        // If you remove the first updateZoomLevel it takes double the amount of time to
        // load the chunks, we dont know why (mvh August & Oliver)
        updateZoomLevel();
        currentChunkAmountSeen = updateChunks(getDetailLevel(), getUpperLeftCorner(), getLowerRightCorner());
        updateZoomLevel();

        for (int i = getDetailLevel(); i <= 4; i++) {
            Map<Integer, List<Way>> chunkLayer = model.getChunksInZoomLevel(i);
            for (int chunk : chunkLayer.keySet()) {
                List<Way> currentChunk = chunkLayer.get(chunk);
                for (int j = 0; j < chunkLayer.get(chunk).size(); j++) {
                    currentChunk.get(j).draw(gc, getZoomDistance() / startDist * 100);
                }
            }
        }
    }

    /**
     * @return int the detail level of the map
     */
    private int getDetailLevel() {
        if (zoomLevel > 55000)
            return 4;
        if (zoomLevel > 2300)
            return 3;
        if (zoomLevel > 115)
            return 2;
        if (zoomLevel > 10)
            return 1;
        return 0;
    }

    /**
     * Zooms the map
     * 
     * @param dx     the upper left X corner to be zoomed to
     * @param dy     the upper left Y corner to be zoomed to
     * @param factor the factor to zoom by
     */
    public void zoom(double dx, double dy, double factor) {
        trans.prependTranslation(-dx, -dy);
        trans.prependScale(factor, factor);
        trans.prependTranslation(dx, dy);
        redraw();
    }

    /**
     * @return float the current distance between the two points (0,0) & (0,100)
     */
    private float getZoomDistance() {
        Point2D p1 = convertTo2DPoint(0, 0);
        Point2D p2 = convertTo2DPoint(0, 100);
        return (float) p1.distance(p2);
    }

    /**
     * Updates the zoom level
     */
    private void updateZoomLevel() {
        float newZoom = getZoomDistance();
        zoomLevel = (newZoom / startDist) * 100 * currentChunkAmountSeen * model.getChunkAmount();
    }

    /**
     * Converts from canvas to JavaFx 2D point
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return Point2D the converted point
     */
    private Point2D convertTo2DPoint(double x, double y) {
        try {
            return trans.inverseTransform(x, y);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }
}
