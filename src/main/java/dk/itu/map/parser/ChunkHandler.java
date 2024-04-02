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

public class ChunkHandler {
    private final String dataPath;

    public float minlat, maxlat, minlon, maxlon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;
    public float CHUNK_SIZE;

    // Temp variable to save loaded ways
    public ArrayList<Way> ways;

    /**
     * Initialises the filehandler
     *
     * @param dataPath
     */
    public ChunkHandler(String dataPath) {
        this.dataPath = dataPath;

        try {
            File file = new File(this.dataPath + "/config");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            this.minlat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxlat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.minlon = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxlon = Float.parseFloat(reader.readLine().split(" ")[1]);
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

        ways = new ArrayList<>();
    }

    public int latLonToChunkIndex(float lat, float lon) {
        return (int) Math.floor((lon - minlon) / CHUNK_SIZE) +
            (int) Math.floor((lat - minlat) / CHUNK_SIZE) * chunkColumnAmount;
    }

    public int pointToChunkIndex(Point2D p) {
        float X = (float) p.getX()/0.56f;
        X = Math.min(X,maxlon);
        X = Math.max(X,minlon);
        float Y = (float) p.getY()*-1;
        Y = Math.min(Y,maxlat);
        Y = Math.max(Y,minlat);
        int chunkIndex = latLonToChunkIndex(Y, X);
        chunkIndex = Math.min(chunkIndex, chunkAmount-1);
        chunkIndex = Math.max(chunkIndex, 0);
        return chunkIndex;
    }

    public Map<Integer, List<Way>> loadBytes(int chunk, int zoomLevel) {
        return loadBytes(new int[] { chunk }, zoomLevel);
    }

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
