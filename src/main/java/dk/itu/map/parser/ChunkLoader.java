package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;

import java.io.IOException;
import java.io.InputStream;
import java.io.EOFException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import javafx.application.Platform;
import javafx.geometry.Point2D;

public class ChunkLoader extends Thread {
    private final MapConfig config;
    
    private Map<Integer, Map<Integer, List<Drawable>>> finishedChunks;
    private final List<Integer>[] chunkIndexQueue;
    private final HashSet<Integer>[] chunkQueueSet;
    private int queueSize;

    private Runnable callback;
    
    /**
     * Initialises the filehandler
     *
     */
    @SuppressWarnings("unchecked")
    public ChunkLoader(String mapType) {
        this.config = new MapConfig(mapType);
        this.finishedChunks = new HashMap<>();
        this.chunkIndexQueue = new List[config.layerCount];
        this.chunkQueueSet = new HashSet[config.layerCount];
        for (int i = 0; i < config.layerCount; i++) {
            chunkIndexQueue[i] = Collections.synchronizedList(new LinkedList<>());
            chunkQueueSet[i] = new HashSet<>();
        }
        queueSize = 0;

        Thread thread = new Thread(this);
        thread.start();
    }

    // TODO: Write javadoc
    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    /**
     * Reads the files from the chunks
     * @param chunks    The chunks to read
     * @param zoomLevel The zoom level
     */
    public void readFiles(int[] chunks, int zoomLevel) {
        for (int chunk : chunks) {
            if (chunk < 0 || chunk >= config.getChunkAmount(zoomLevel)) continue;
            if (chunkQueueSet[zoomLevel].contains(chunk)) continue;
            chunkIndexQueue[zoomLevel].add(chunk);
            chunkQueueSet[zoomLevel].add(chunk);
            queueSize++;
        }
    }

    /**
     * Get the finished chunks
     * @return The finished chunks
     */
    public Map<Integer, Map<Integer, List<Drawable>>> getFinishedChunks() {
        Map<Integer, Map<Integer, List<Drawable>>> temp = finishedChunks;
        finishedChunks = new HashMap<>();
        return temp;
    }

    /**
     * Run the chunkloader thread
     */
    public void run() {
        while (true) {
            try {
                if (queueSize <= 0) {
                    Thread.sleep(100);
                    continue;
                }
            } catch (Exception e) {
                System.out.println(e);
                continue;
            }
            int chunkIndex = -1;
            int zoomLayer = -1;
            for (int i = config.layerCount-2; i >= 0; i--) {
                if (chunkIndexQueue[i].size() > 0) {
                    chunkIndex = chunkIndexQueue[i].remove(0);
                    chunkQueueSet[i].remove(chunkIndex);
                    zoomLayer = i;
                    queueSize--;
                    break;
                }
            }
            if (chunkIndex == -1) continue;
            List<Drawable> chunk = new ArrayList<>();
            
            InputStream file = config.locateFile("zoom" + zoomLayer + "/chunk" + chunkIndex + ".txt");

            long id;
            CoordArrayList outerCoords;
            CoordArrayList innerCoords;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(file))) {
                while (true) {
                    id = stream.readLong();
                    int outerCoordsLength = stream.readInt();
                    outerCoords = new CoordArrayList(outerCoordsLength);
                    for (int j = 0; j < outerCoordsLength; j++) {
                        outerCoords.add(stream.readFloat(), stream.readFloat());
                    }
                    int innerCoordsLength = stream.readInt();
                    innerCoords = new CoordArrayList(innerCoordsLength);
                    for (int j = 0; j < innerCoordsLength; j++) {
                        innerCoords.add(stream.readFloat(), stream.readFloat());
                    }

                    String primaryType = stream.readUTF();
                    String secondaryType = stream.readUTF();
                    chunk.add(new DrawableWay(outerCoords, innerCoords, id, primaryType, secondaryType));
                }
                /*
                 * The stream will throw an end of file exception when its done,
                 * this way we can skip checking if we are done reading every loop, and save
                 * time
                 */
            } catch (EOFException e) {
                // End of file reached
                Map<Integer, List<Drawable>> temp = finishedChunks.getOrDefault(zoomLayer, new HashMap<>());
                temp.put(chunkIndex, chunk);
                finishedChunks.put(zoomLayer, temp);

                if (queueSize % 20 == 0) Platform.runLater(callback);
                continue;
            } catch (IOException e) {
                // Since we run it in parallel we need to return a runtime exception since we
                // can throw the IOException out of the scope
                throw new RuntimeException(e);
            }
        }
    }

    public Set<Drawable> readLandLayer(){
        Set<Drawable> list = new HashSet<>();
        int zoomLayer = config.layerCount-1;
        int landChunkAmount = config.getChunkAmount(zoomLayer);
        for(int i = 0; i < landChunkAmount; i++){
            InputStream file = config.locateFile("zoom" + zoomLayer + "/chunk" + i + ".txt");

            long id;
            CoordArrayList outerCoords;
            CoordArrayList innerCoords;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(file))) {
                while (true) {
                    id = stream.readLong();
                    int outerCoordsLength = stream.readInt();
                    outerCoords = new CoordArrayList(outerCoordsLength);
                    for (int j = 0; j < outerCoordsLength; j++) {
                        outerCoords.add(stream.readFloat(), stream.readFloat());
                    }
                    int innerCoordsLength = stream.readInt();
                    innerCoords = new CoordArrayList(innerCoordsLength);
                    for (int j = 0; j < innerCoordsLength; j++) {
                        innerCoords.add(stream.readFloat(), stream.readFloat());
                    }

                    String primaryType = stream.readUTF();
                    String secondaryType = stream.readUTF();
                    list.add(new DrawableWay(outerCoords, innerCoords, id, primaryType, secondaryType));
                }
                /*
                 * The stream will throw an end of file exception when its done,
                 * this way we can skip checking if we are done reading every loop, and save
                 * time
                 */
            } catch (EOFException e) {
            } catch (IOException e) {
                // Since we run it in parallel we need to return a runtime exception since we
                // can throw the IOException out of the scope
                throw new RuntimeException(e);
            }
        }

        return list;
    }

    /**
     * Load the data from the chunkfiles
     *
     * @param chunks    The chunks to load
     * @param zoomLevel The zoom level
     * @return The ways
     */
    public Map<Integer, List<Drawable>> readFilesold(int[] chunks, int zoomLevel) {
        Map<Integer, List<Drawable>> ways = Collections.synchronizedMap(new HashMap<>());
        // long starTime = System.currentTimeMillis();
        IntStream.of(chunks).parallel().forEach(chunk -> {
            if (chunk < 0 || chunk >= config.getChunkAmount(zoomLevel)) return;

            ways.putIfAbsent(chunk, new ArrayList<>());

            InputStream file = config.locateFile("zoom" + zoomLevel + "/chunk" + chunk + ".txt");

            long id;
            CoordArrayList outerCoords;
            CoordArrayList innerCoords;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(file))) {
                while (true) {
                    id = stream.readLong();
                    int outerCoordsLength = stream.readInt();
                    outerCoords = new CoordArrayList(outerCoordsLength);
                    for (int j = 0; j < outerCoordsLength; j++) {
                        outerCoords.add(stream.readFloat(), stream.readFloat());
                    }
                    int innerCoordsLength = stream.readInt();
                    innerCoords = new CoordArrayList(innerCoordsLength);
                    for (int j = 0; j < innerCoordsLength; j++) {
                        innerCoords.add(stream.readFloat(), stream.readFloat());
                    }

                    String primaryType = stream.readUTF();
                    String secondaryType = stream.readUTF();
                    ways.get(chunk).add(new DrawableWay(outerCoords, innerCoords, id, primaryType, secondaryType));
                }
                /*
                 * The stream will throw an end of file exception when its done,
                 * this way we can skip checking if we are done reading every loop, and save
                 * time
                 */
            } catch (EOFException e) {
                // End of file reached
                return;
            } catch (IOException e) {
                // Since we run it in parallel we need to return a runtime exception since we
                // can throw the IOException out of the scope
                throw new RuntimeException(e);
            }
        });

        // long endTime = System.currentTimeMillis();
        // System.out.println("Reading " + chunks.length + " chunks in " + (endTime - starTime) + "ms");
        // MapView.overridePrint = true;
        return ways;
    }

    /**
     * Converts a point to a chunk index
     * @param point     The point to convert
     * @param zoomLevel The zoom level to be used
     * @return The chunk index
     */
    public int pointToChunkIndex(Point2D point, int zoomLevel) {
        return config.pointToChunkIndex(point, zoomLevel);
    }

    /**
     * Get the config
     * @return The config
     */
    public MapConfig getConfig() {
        return config;
    }
}
