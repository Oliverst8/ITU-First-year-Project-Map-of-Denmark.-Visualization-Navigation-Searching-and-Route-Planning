package dk.itu.map.parser;

import dk.itu.map.App;
import dk.itu.map.structures.ArrayLists.CoordArrayList;

import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

class Chunk extends HashSet<MapElement> {}
class ZoomLayer extends ArrayList<Chunk> {}

public class ChunkGenerator implements Runnable {

    private MapConfig config;
    
    private ArrayList<ZoomLayer> zoomLayers;
    private final File[][] files;
    private List<MapElement> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;
    private final GraphBuilder graph;
    private final Thread graphThread;

    private final Thread chunkingThread;


    /**
     * Constructor for the ChunkGenerator class
     *
     * @param minLat The minimum latitude
     * @param maxLat The maximum latitude
     * @param minLon The minimum longitude
     * @param maxLon The maximum longitude
     */
    public ChunkGenerator(MapConfig config) {
        graph = new GraphBuilder();
        this.hasMoreWork = false;
        this.rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        this.chunkingThread = new Thread(this);
        this.chunkingThread.start();
        graphThread = new Thread(graph);
        graphThread.start();

        this.config = config;

        System.out.println("Beginning " + config.rowAmount + " " + config.columnAmount);

        resetChunks();

        files = new File[config.layerCount][];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File[config.getChunkAmount(i)];
        }

        createFiles(App.mapPath);
    }
    
    /**
     * Create the files for the chunks
     *
     * @param dataPath The path to the map folder
     */
    private void createFiles(String dataPath) {
        try {
            File folder = new File(dataPath);
            folder.mkdirs();
            for (int i = 0; i < config.layerCount; i++) {
                File innerFolder = new File(dataPath + "zoom" + i);
                innerFolder.mkdir();
                
                int chunkAmount = config.getChunkAmount(i);
                for (int j = 0; j < chunkAmount; j++) {
                    files[i][j] = new File(dataPath + "zoom" + i + "/chunk" + j + ".txt");
                    new FileOutputStream(files[i][j]).close();
                }
            }
            (new File(dataPath + "utilities")).mkdir();

        } catch (Exception e) {
            System.out.println("failed " + e.getMessage());
        }
    }
    /**
     * Removes the chunks that have been parsed, and makes room for new chunks to be added.
     */

    private void resetChunks() {
        zoomLayers = new ArrayList<>(config.layerCount);
        for (int i = 0; i < config.layerCount; i++) {
            zoomLayers.add(new ZoomLayer());
            int chunkAmount = config.getChunkAmount(i);
            for (int j = 0; j < chunkAmount; j++) {
                zoomLayers.get(i).add(new Chunk());
            }
        }
    }

    /**
     * Adds a way to the list of ways to be chunked
     *
     * @param way The way to be added
     */
    public void addWay(MapElement way) {
        rawWays.add(way);
    }
    
    /**
     * Sort ways into chunks and zoom levels
     */
    public void chunkWays() {
        List<MapElement> newWays = rawWays;
        System.out.println("chunking: " + newWays.size());
        rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        for (MapElement way : newWays) {
            byte zoomLevel = desiredZoomLevel(way);
            if (zoomLevel == -1) continue;

            CoordArrayList coords = way.getCoords();
            for (int i = 0; i < coords.size(); i++) {
                float[] coord = coords.get(i);
                float lat = coord[0];
                float lon = coord[1];

                int chunkIndex = config.coordsToChunkIndex(lat, lon, zoomLevel);

                if (chunkIndex < config.getChunkAmount(zoomLevel) && chunkIndex >= 0) {
                    zoomLayers.get(zoomLevel).get(chunkIndex).add(way);
                }
            }
        }
    }

    /**
     * Determines the desired zoom level for a way
     * @param way The way to determine the zoom level for
     * @return The desired zoom level
     */
    private byte desiredZoomLevel(MapElement way) {
        byte zoomLevel = -1;
        List<String> tags = way.getTags();

        for (int i = 0; i < tags.size(); i += 2) {
            switch (tags.get(i)) {
                case "route":
                    switch (tags.get(i + 1)) {
                        case "ferry", "ferry_link":
                            return -1;
                    }

                case "place":
                    switch (tags.get(i + 1)) {
                        case "island", "islet":
                            if (zoomLevel < 4) zoomLevel = 4;
                            break;
                    }
                    break;

                case "aeroway":
                    switch (tags.get(i + 1)) {
                        case "aerodrome":
                            if (zoomLevel < 3) zoomLevel = 3;
                            break;

                        case "apron", "runway", "taxiway":
                            if (zoomLevel < 2) zoomLevel = 2;
                            break;
                    }
                    break;
                
                case "highway":
                    if(way.getNodeIDs() != null) graph.addWay(way); //Not adding relations to the graph for now, so it dosnt work with walking
                    switch (tags.get(i + 1)) {
                        case "trunk", "primary", "secondary", "motorway":
                            if (zoomLevel < 4) zoomLevel = 4;
                            break;

                        case "tertiary", "tertiary_link", "motorway_link", "primary_link", "trunk_link":
                            if (zoomLevel < 3) zoomLevel = 3;
                            break;
                        
                        case "service", "residential", "unclassified":
                            if (zoomLevel < 1) zoomLevel = 0;
                            break;
                    }
                    break;

                case "natural":
                    switch (tags.get(i + 1)) {
                        case "peninsula":
                            if (zoomLevel < 4) zoomLevel = 4;
                            break;
                    }

                    switch (tags.get(i + 1)) {
                        case "water", "scrub", "beach":
                            if (zoomLevel < 3) zoomLevel = 3;
                            break;
                    }
                    break;
                
                case "landuse":
                    switch (tags.get(i + 1)) {
                        case "allotments", "industrial", "residential":
                            if (zoomLevel < 3) zoomLevel = 3;
                            break;
                    }
                    break;
                
                case "building":
                    switch (tags.get(i + 1)) {
                        case "yes", "shed", "office", "college", "detached", "dormitory", "university", "apartments", "allotment_house":
                            if (zoomLevel < 0) zoomLevel = 0;
                            break;
                    }
                    break;
                }
        }

        return zoomLevel;
    }

    /**
     * The main loop of the ChunkGenerator
     */
    @Override
    public void run() {
        hasMoreWork = true;
        while (hasMoreWork || !rawWays.isEmpty()) {
            if (rawWays.size() < 100_000 && hasMoreWork) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            resetChunks();

            chunkWays();

            writeFiles();
        }
        long startTime = System.nanoTime();
        graph.stop();
        try {
            graphThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeUtilities();

        long endTime = System.nanoTime();
        System.out.println("Writing graph to file took: " + (endTime-startTime)/1_000_000_000.0 + "s");
    }
    /**
     * Write the chunks to binaryfiles
     */
    public void writeFiles() {
        IntStream.range(0, config.layerCount).forEach(i -> {
            IntStream.range(0, zoomLayers.get(i).size()).parallel().forEach(j -> {
                try {
                    DataOutputStream stream = new DataOutputStream(
                            new BufferedOutputStream(
                                new FileOutputStream(files[i][j], true)));
                    for (MapElement way : zoomLayers.get(i).get(j)) {
                        way.stream(stream);
                    }
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("e.getMessage()");
                    e.printStackTrace();
                }
            });
        });

        config.writeConfig();
    }
    /**
     * Write the configuration file, with map constants
     */
    private void writeUtilities() {
        graph.writeToFile(App.mapPath + "utilities");
    }

    /**
     * Used to mark the finish of ChunkGenerator and prevent it from continuing
     */
    public void finishWork() {
        hasMoreWork = false;
        try {
            chunkingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
