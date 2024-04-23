package dk.itu.map.parser;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.App;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.EOFException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

import javafx.geometry.Point2D;

public class ChunkLoader {

    private final MapConfig config;
    
    /**
     * Initialises the filehandler
     *
     */
    public ChunkLoader() {
        this.config = new MapConfig();
    }


    /**
     * Load the data from the chunkfiles
     *
     * @param chunks    The chunks to load
     * @param zoomLevel The zoom level
     * @return The ways
     */

    public Map<Integer, List<Drawable>> readFiles(int[] chunks, int zoomLevel) {
        Map<Integer, List<Drawable>> ways = Collections.synchronizedMap(new HashMap<>());

        IntStream.of(chunks).parallel().forEach(chunk -> {
            if (chunk < 0 || chunk >= config.getChunkAmount(zoomLevel)) return;

            ways.putIfAbsent(chunk, new ArrayList<>());

            File file = new File(App.mapPath + "zoom" + zoomLevel + "/chunk" + chunk + ".txt");

            long id;
            CoordArrayList outerCoords;
            CoordArrayList innerCoords;
            String[] tags;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
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
                    tags = new String[stream.readInt()];
                    for (int j = 0; j < tags.length; j++) {
                        tags[j] = stream.readUTF();
                    }

                    String primaryType = stream.readUTF();
                    ways.get(chunk).add(new DrawableWay(outerCoords, innerCoords, tags, id, primaryType));
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

        return ways;
    }

    public int pointToChunkIndex(Point2D point, int zoomLevel) {
        return config.pointToChunkIndex(point, zoomLevel);
    }

    public MapConfig getConfig() {
        return config;
    }


}
