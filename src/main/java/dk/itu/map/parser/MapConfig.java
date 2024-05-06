package dk.itu.map.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import dk.itu.map.App;
import javafx.geometry.Point2D;

public class MapConfig {
    private final boolean isInternal;

    public final float CHUNK_SIZE;
    public final byte layerCount;

    public final float minLat, maxLat, minLon, maxLon;

    public final int columnAmount, rowAmount, chunkAmount;

    /**
     * Creates a new MapConfig
     * @param minLat The minimum latitude
     * @param maxLat The maximum latitude
     * @param minLon The minimum longitude
     * @param maxLon The maximum longitude
     */
    public MapConfig(float minLat, float maxLat, float minLon, float maxLon) {
        isInternal = false;
        this.CHUNK_SIZE = 0.01f;
        this.layerCount = 6;
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;
        this.columnAmount = (int) Math.ceil(Math.abs(maxLon - minLon) / CHUNK_SIZE);
        this.rowAmount = (int) Math.ceil(Math.abs(maxLat - minLat) / CHUNK_SIZE);
        this.chunkAmount = columnAmount * rowAmount;
    }

    /**
     * Load the config file, and set the variables
     */
    public MapConfig(String mapType) {
        try {
            isInternal = mapType.equals("internal") ? true : false;
            InputStream file = locateFile("config");
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            this.minLat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxLat = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.minLon = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.maxLon = Float.parseFloat(reader.readLine().split(" ")[1]);
            this.columnAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.rowAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.chunkAmount = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.layerCount = Byte.parseByte(reader.readLine().split(" ")[1]);
            this.CHUNK_SIZE = Float.parseFloat(reader.readLine().split(" ")[1]);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts coordinates to a chunk index
     *
     * @param lat The latitude
     * @param lon The longitude
     * @return The chunk index
     */
    public int coordsToChunkIndex(float lat, float lon, int zoomLevel) {
        return (int) Math.floor((lon - minLon) / (CHUNK_SIZE * Math.pow(2, zoomLevel))) +
            (int) Math.floor((lat - minLat) / (CHUNK_SIZE * Math.pow(2, zoomLevel))) * getColumnAmount(zoomLevel);
    }

    /**
     * Converts a javaFX-point to a chunk index
     *
     * @param p The point
     * @return The chunk index
     */
    public int pointToChunkIndex(Point2D p, int zoomLayer) {
        float X = (float) p.getX() / 0.56f;
        X = Math.min(X, maxLon);
        X = Math.max(X, minLon);
        float Y = (float) p.getY() * -1;
        Y = Math.min(Y, maxLat);
        Y = Math.max(Y, minLat);
        int chunkIndex = coordsToChunkIndex(Y, X, zoomLayer);
        chunkIndex = Math.min(chunkIndex, chunkAmount - 1);
        chunkIndex = Math.max(chunkIndex, 0);
        return chunkIndex;
    }

    /**
     * This function returns the column amount divided by 2^zoomLevel and rounds up
     * @param zoomLevel the current zoomLevel
     * @return
     */
    public int getColumnAmount(int zoomLevel) {
        int roundUp = (1<<zoomLevel)-1;
        return columnAmount + roundUp >> zoomLevel;
    }

    /**
     * Get the row amount in the map on a given zoom level
     * @param zoomLevel the desired zoom level
     * @return the row amount
     */
    public int getRowAmount(int zoomLevel) {
        int roundUp = (1<<zoomLevel)-1;
        return rowAmount + roundUp >> zoomLevel;
    }

    /**
     * Get the amount of chunks in the map on a given zoom level
     * @param zoomLevel the desired zoom level
     * @return the amount of chunks
     */
    public int getChunkAmount(int zoomLevel) {
        return getColumnAmount(zoomLevel) * getRowAmount(zoomLevel);
    }
    
    /**
     * Writes the config to a file
     */
    public void writeConfig() {
        try {
            FileWriter writer = new FileWriter(App.mapName + "/config");
            writer.write(
                "minLat: " + minLat + "\n" +
                "maxLat: " + maxLat + "\n" +
                "minLon: " + minLon + "\n" +
                "maxLon: " + maxLon + "\n" +
                "chunkColumnAmount: " + columnAmount + "\n" +
                "chunkRowAmount: " + rowAmount + "\n" +
                "chunkAmount: " + chunkAmount + "\n" +
                "LayerCount: " + layerCount + "\n" +
                "CHUNK_SIZE: " + CHUNK_SIZE + "\n");
                writer.close();
        } catch (IOException e) {
            System.out.println();
        }
    }

    /**
     * Finds the location of a file
     * @param filePath The path to the file
     * @return The file
     * @throws FileNotFoundException 
     */
    public InputStream locateFile(String filePath) {
        if(isInternal) {
            //return new File(getClass().getResource("/maps/" + App.mapName + "/" + filePath).getFile());
            return getClass().getResourceAsStream("/maps/" + App.mapName + "/" + filePath);
        } else {
            try {
                return new FileInputStream(new File(App.DATA_PATH + "/" + App.mapName + "/" + filePath));
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
                System.exit(1);
                return null;
            }
        }
    }
}
