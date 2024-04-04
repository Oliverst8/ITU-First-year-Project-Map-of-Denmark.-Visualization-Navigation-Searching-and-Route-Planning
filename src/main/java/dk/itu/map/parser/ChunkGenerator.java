package dk.itu.map.parser;

import dk.itu.map.structures.CoordArrayList;

import java.io.File;
import java.io.FileWriter;
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

    private final String dataPath;
    // chunk size in coordinate size
    private final float CHUNK_SIZE = 0.25f;
    private final byte amountOfZoomLayers = 5;

    public float minLat, maxLat, minLon, maxLon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    private ArrayList<ZoomLayer> zoomLayers;
    private final File[][] files;
    private List<MapElement> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;

    private final Thread chunkingThread;

    public ChunkGenerator(String dataPath, float minLat, float maxLat, float minLon, float maxLon) {
        this.dataPath = dataPath;
        this.hasMoreWork = false;
        this.rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        this.chunkingThread = new Thread(this);
        this.chunkingThread.start();

        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.maxLon = maxLon;

        this.chunkColumnAmount = (int) Math.ceil(Math.abs(maxLon - minLon) / CHUNK_SIZE);
        this.chunkRowAmount = (int) Math.ceil(Math.abs(maxLat - minLat) / CHUNK_SIZE);

        this.chunkAmount = chunkColumnAmount * chunkRowAmount;

        System.out.println("Beginning" + chunkRowAmount + " " + chunkColumnAmount);

        resetChunks();

        files = new File[amountOfZoomLayers][chunkAmount];

        createFiles(dataPath);
    }

    private void createFiles(String dataPath) {
        try {
            File folder = new File(dataPath);
            folder.mkdirs();
            for (int i = 0; i < amountOfZoomLayers; i++) {
                File innerFolder = new File(dataPath + "/zoom" + i);
                innerFolder.mkdir();

                for (int j = 0; j < chunkAmount; j++) {
                    files[i][j] = new File(dataPath + "/zoom" + i + "/chunk" + j + ".txt");
                    new FileOutputStream(files[i][j]).close();
                }
            }

        } catch (Exception e) {
            System.out.println("failed " + e.getMessage());
        }
    }

    private void resetChunks() {
        zoomLayers = new ArrayList<>(chunkAmount);
        for (int i = 0; i < amountOfZoomLayers; i++) {
            zoomLayers.add(new ZoomLayer());
            for (int j = 0; j < chunkAmount; j++) {
                zoomLayers.get(i).add(new Chunk());
            }
        }
    }

    private int coordsToChunkIndex(float lat, float lon) {
        return (int) Math.floor((lon - minLon) / CHUNK_SIZE) +
            (int) Math.floor((lat - minLat) / CHUNK_SIZE) * chunkColumnAmount;
    }

    public void addWay(MapElement way) {
        rawWays.add(way);
    }

    public void chunkWays() {
        List<MapElement> newWays = rawWays;
        System.out.println("chunking: " + newWays.size());
        rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        forWay:
        for (MapElement way : newWays) {
            byte zoomLevel = -1;
            List<String> tags = way.getTags();
            for (String tag : tags) {
                switch (tag) {
                    case "ferry":
                        continue forWay;

                    case "motorway":
                    case "motorway_link":
                    case "trunk":
                    case "trunk_link":
                    case "primary":
                    case "primary_link":
                    case "island":
                    case "coastline":
                        zoomLevel = 4;
                        break;
                    case "aerodrome":
                    case "secondary":
                    case "secondary_link":
                    case "rail":
                    case "light_rail":
                        if (zoomLevel < 3) zoomLevel = 3;
                        break;
                    case "forest":
                    case "grassland":
                    case "wetland":
                    case "runway":
                    case "tertiary":
                    case "tertiary_link":
                    case "heath":
                    case "scrub":
                    case "fell":
                    case "beach":
                    case "water":
                        if (zoomLevel < 2) zoomLevel = 2;
                        break;
                    case "unclassified":
                    case "residential":
                        if (zoomLevel < 1) zoomLevel = 1;
                        break;
                    case "building":
                    case "highway":
                        if (zoomLevel < 0) zoomLevel = 0;
                        break;
                }
            }
            if (zoomLevel == -1) continue;

            CoordArrayList coords = way.getCoords();
            for (int i = 0; i < coords.size(); i += 2) {
                float lat = coords.get(i);
                float lon = coords.get(i + 1);

                int chunkIndex = coordsToChunkIndex(lat, lon);

                if (chunkIndex < chunkAmount && chunkIndex >= 0) {
                    zoomLayers.get(zoomLevel).get(chunkIndex).add(way);
                }
            }
        }
    }

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
        System.out.println("Finished while loop");
    }

    public void writeFiles() {
        IntStream.range(0, amountOfZoomLayers).forEach(i -> {
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

        try {
            writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeConfig() throws IOException {
        FileWriter writer = new FileWriter(dataPath + "/config");
        writer.write(
                "minLat: " + minLat + "\n" +
                "maxLat: " + maxLat + "\n" +
                "minLon: " + minLon + "\n" +
                "maxLon: " + maxLon + "\n" +
                "chunkColumnAmount: " + chunkColumnAmount + "\n" +
                "chunkRowAmount: " + chunkRowAmount + "\n" +
                "chunkAmount: " + chunkAmount + "\n" +
                "CHUNK_SIZE: " + CHUNK_SIZE + "\n");
        writer.close();
    }

    public void finishWork() {
        hasMoreWork = false;
        try {
            chunkingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
