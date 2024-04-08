package dk.itu.map.parser;

import dk.itu.map.structures.Way;

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
    private final String dataPath;

    public float minLat, maxLat, minLon, maxLon;
    public int chunkColumnAmount, chunkRowAmount, chunkAmount;
    public float CHUNK_SIZE;


    /**
     * Initialises the filehandler
     *
     * @param dataPath
     */
    public ChunkLoader(String dataPath) {
        this.dataPath = dataPath;
        loadConfig();
    }
    /**
     * Load the config file, and set the variables
     */
    private void loadConfig() {
        try {
            File file = new File(this.dataPath + "/config");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            this.minLat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxLat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.minLon = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxLon = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.chunkColumnAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.chunkRowAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.chunkAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.CHUNK_SIZE = Float.parseFloat(reader.readLine().split(" ")[1]);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Converts a latitude and longitude to a chunk index
     *
     * @param lat The latitude
     * @param lon The longitude
     * @return The chunk index
     */

    public int latLonToChunkIndex(float lat, float lon) {
        return (int) Math.floor((lon - minLon) / CHUNK_SIZE) +
            (int) Math.floor((lat - minLat) / CHUNK_SIZE) * chunkColumnAmount;
    }
    /**
     * Converts a javaFX-point to a chunk index
     *
     * @param p The point
     * @return The chunk index
     */

    public int pointToChunkIndex(Point2D p) {
        float X = (float) p.getX()/0.56f;
        X = Math.min(X, maxLon);
        X = Math.max(X, minLon);
        float Y = (float) p.getY()*-1;
        Y = Math.min(Y, maxLat);
        Y = Math.max(Y, minLat);
        int chunkIndex = latLonToChunkIndex(Y, X);
        chunkIndex = Math.min(chunkIndex, chunkAmount-1);
        chunkIndex = Math.max(chunkIndex, 0);
        return chunkIndex;
    }
    /**
     * Load the data from the chunkfiles
     *
     * @param chunks The chunks to load
     * @param zoomLevel The zoom level
     * @return The ways
     */

    public Map<Integer, List<Way>> loadBytes(int[] chunks, int zoomLevel) {
        Map<Integer, List<Way>> ways = Collections.synchronizedMap(new HashMap<>());

        IntStream.of(chunks).parallel().forEach(chunk -> {
            if (chunk < 0 || chunk >= chunkAmount) return;

            ways.putIfAbsent(chunk, new ArrayList<>());

            File file = new File(this.dataPath + "/zoom" + zoomLevel + "/chunk" + chunk + ".txt");

            float[] outerCoords;
            float[] innerCoords;
            String[] tags;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                while (true) {
                    outerCoords = new float[stream.readInt()];
                    for (int j = 0; j < outerCoords.length; j++) {
                        outerCoords[j] = stream.readFloat();
                    }
                    innerCoords = new float[stream.readInt()];
                    for (int j = 0; j < innerCoords.length; j++) {
                        innerCoords[j] = stream.readFloat();
                    }
                    tags = new String[stream.readInt()];
                    for (int j = 0; j < tags.length; j++) {
                        tags[j] = stream.readUTF();
                    }
                    ways.get(chunk).add(new Way(outerCoords, innerCoords, tags));
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
}
