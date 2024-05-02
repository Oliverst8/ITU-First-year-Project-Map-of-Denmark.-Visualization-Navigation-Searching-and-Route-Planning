package dk.itu.map.fxml.controllers;

import dk.itu.map.App;
import dk.itu.map.fxml.models.MapModel;
import dk.itu.map.fxml.views.MapView;
import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.UtilityLoader;
import dk.itu.map.structures.TernaryTree;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import dk.itu.map.structures.Point;
import dk.itu.map.utility.Navigation;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;

public class MapController {

    private MapModel model;
    private MapView view;

    /**
     * Constructor for the MapController, set the following variables
     * 
     * @param model
     */
    public MapController(MapModel model) {
        this.model = model;
    }

    public void setView(MapView view) {
        if (this.view != null) {
            throw new RuntimeException("cannot reset view");
        }
        this.view = view;
    }

    /**
     * Imports a map from a file
     * 
     * @param osmFile The path to the file To be deleted
     * @param mapName The name of the map to be saved to
     */
    public void importMap(String osmFile, String mapName) {
        App.mapPath = App.DATA_PATH + mapName + "/";

        UtilityLoader utilityLoader = new UtilityLoader(mapName);
        utilityLoader.start();

        model.chunkLoader = new ChunkLoader();
        model.chunkLoader.setCallback(() -> {
            boolean shouldRedraw = getWrittenChunks();

            if (view != null && shouldRedraw) view.redraw();
        });

        setUtilities(utilityLoader);
    }

    public boolean getWrittenChunks() {
        Map<Integer, Map<Integer, List<Drawable>>> newChunks = model.chunkLoader.getFinishedChunks();
        for (int i = 0; i < model.getLayerCount(); i++) {
            Map<Integer, List<Drawable>> chunks = model.chunkLayers.get(i);
            Map<Integer, List<Drawable>> newLayer = newChunks.get(i);
            if (newLayer == null) {
                continue;
            }
            chunks.putAll(newLayer);
        }
        return !newChunks.isEmpty();
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
     * @param detailLevel      The current detail level
     * @param upperLeftCorner  The upper left corner of the current view
     * @param lowerRightCorner The lower right corner of the current view
     * @return The amount of chunks seen in the current view in the y direction
     */
    public void updateChunks(int detailLevel, Point2D upperLeftCorner, Point2D lowerRightCorner/*, boolean print*/) {
        // int count = 0;
        for (int i = detailLevel; i < model.getLayerCount(); i++) {
            int upperLeftChunk = model.chunkLoader.pointToChunkIndex(upperLeftCorner, i);
            int lowerRightChunk = model.chunkLoader.pointToChunkIndex(lowerRightCorner, i);
            Map<Integer, List<Drawable>> chunks = model.chunkLayers.get(i);

            Set<Integer> visibleChunks = getChunksInRect(upperLeftChunk, lowerRightChunk, model.chunkLoader.getConfig().getColumnAmount(i));
            // count += visibleChunks.size()*(model.getLayerCount()-detailLevel);

            chunks.keySet().retainAll(visibleChunks);

            visibleChunks.removeAll(chunks.keySet());
    
            int[] newChunks = new int[visibleChunks.size()];
    
            int c = 0;
            for (int chunk : visibleChunks) {
                newChunks[c++] = chunk;
            }
    
            if (visibleChunks.isEmpty())
                continue;
    
            model.chunkLoader.readFiles(newChunks, i);
        }
        // if (print || MapView.overridePrint) {
        //     System.out.println("Loaded chunks: " + count);
        // }
    }

    private void setUtilities(UtilityLoader utilityLoader) {
        try {
            utilityLoader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        model.setGraph(utilityLoader.getGraph());
        model.setAddress(utilityLoader.getAddress());
    }

    public void navigate(int vehicleCode) {
        Point startPoint = model.getStartPoint();
        Point endPoint = model.getEndPoint();
        if(startPoint == null || endPoint == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No way found");
            alert.setHeaderText("Start and end cant be connected");
            alert.setContentText("This might be because you cant drive a connecting road, please try another road.");
            alert.showAndWait();
            return;
        }
        Navigation navigation = new Navigation(model.getGraph(), vehicleCode);
        DrawableWay[] path = navigation.getPath(startPoint.getCoords(), endPoint.getCoords());
        if(path == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No path found");
            alert.setContentText("No path found between the selected points");
            alert.showAndWait();
            return;
        }
        model.setRoute(path);
    }

    public List<TernaryTree.searchAddress> searchAddress(String input) {
        return model.getAddress().autoComplete(input, 10);
    }

    public List<TernaryTree.searchAddress> fillAddress(TernaryTree.searchAddress node, String currentText){
        return model.getAddress().fillAddress(new String[]{node.streetName, node.zip},node.node, currentText);
    }
}
