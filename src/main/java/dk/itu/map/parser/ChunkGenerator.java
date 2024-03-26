package dk.itu.map.parser;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Way;

class Chunk extends HashSet<Way> {}
class ZoomLayer extends ArrayList<Chunk> {}

public class ChunkGenerator implements Runnable {
    // chunk size in coordinate size
    private final float CHUNK_SIZE = 0.25f;

    private final byte amountOfZoomLayers = 5;

    public float minlat, maxlat, minlon, maxlon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    private ArrayList<ZoomLayer> zoomLayers;
    private File[][] files;

    private int tempCounter = 0;
    private List<Way> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;

    private final Graph graph = new Graph();

    private Thread chunkingThread;

    public ChunkGenerator(float minlat, float maxlat, float minlon, float maxlon) {
        hasMoreWork = false;
        rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        chunkingThread = new Thread(this);
        chunkingThread.start();

        this.minlat = minlat;
        this.maxlat = maxlat;
        this.minlon = minlon;
        this.maxlon = maxlon;

        chunkColumnAmount = (int) Math.ceil(Math.abs(maxlon - minlon) / CHUNK_SIZE);
        chunkRowAmount = (int) Math.ceil(Math.abs(maxlat - minlat) / CHUNK_SIZE);

        chunkAmount = chunkColumnAmount * chunkRowAmount;

        resetChunks();

        files = new File[amountOfZoomLayers][chunkAmount];

        System.out.println(chunkRowAmount + " " + chunkColumnAmount);

        try {
            File folder = new File("zoomLayers");
            folder.mkdir();
            for (int i = 0; i < amountOfZoomLayers; i++) {
                File innerFolder = new File("zoomLayers/zoom" + i);
                innerFolder.mkdir();
                for (int j = 0; j < chunkAmount; j++) {
                    files[i][j] = new File("zoomLayers/zoom" + i + "/chunk" + j + ".txt");
                    new FileOutputStream(files[i][j]).close();
                }
            }

        } catch (Exception e) {
            System.out.println("failed " + e.getMessage());
        }
    }

    private void resetChunks() {
        zoomLayers = new ArrayList<ZoomLayer>(chunkAmount);
        for (int i = 0; i < amountOfZoomLayers; i++) {
            zoomLayers.add(new ZoomLayer());
            for (int j = 0; j < chunkAmount; j++) {
                zoomLayers.get(i).add(new Chunk());
            }
        }
    }

    private int coordsToChunkIndex(float lat, float lon) {
        return (int) Math.floor((lon - minlon) / CHUNK_SIZE) +
                (int) Math.floor((lat - minlat) / CHUNK_SIZE) * chunkColumnAmount;
    }

    public void addWay(Way way) { // main
        rawWays.add(way);
    }

    public void chunkWays() {
        List<Way> newWays = rawWays;
        System.out.println("chunking: " + newWays.size());
        rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        for (Way way : newWays) {
            byte zoomLevel = -1;
            String[] tags = way.getTags();
            for (String tag : tags) {
                switch (tag) {
                    case "motorway":
                    case "motorway_link":
                    case "trunk":
                    case "trunk_link":
                    case "primary":
                    case "primary_link":

                    case "water":
                    case "wetland":
                    case "bay":
                    case "beach":
                    case "coastline":
                    case "cape":
                    case "fell":
                    case "grassland":
                    case "heath":
                    case "scrub":
                    case "wood":
                    case "aerodrome":
                        zoomLevel = 4;
                        break;
                    case "secondary":
                    case "secondary_link":
                    case "rail":
                    case "light_rail":
                        if (zoomLevel < 3) zoomLevel = 3;
                        break;
                    case "runway":
                    case "tertiary":
                    case "tertiary_link":
                    case "unclassified":
                    case "residential":
                        if (zoomLevel < 2) zoomLevel = 2;
                        break;
                    case "terminal":
                    case "gate":
                        if (zoomLevel < 1) zoomLevel = 1;
                        break;
                    case "building":
                    case "highway":
                        if (zoomLevel < 0) zoomLevel = 0;
                        break;
                }
            }
            if (zoomLevel == -1) continue;
            // way.setZoomLevel( zoomLevel);

            float[] coords = way.getCoords();
            for (int i = 0; i < coords.length; i += 2) {
                float lat = coords[i];
                float lon = coords[i + 1];

                int chunkIndex = coordsToChunkIndex(lat, lon);

                if (chunkIndex < chunkAmount && chunkIndex >= 0) {
                    zoomLayers.get(zoomLevel).get(chunkIndex).add(way);
                }
            }
        }
    }

    public void run() {
        hasMoreWork = true;
        while (hasMoreWork || rawWays.size() > 0) {
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
                            new BufferedOutputStream(new FileOutputStream(files[i][j], true)));
                    for (Way way : zoomLayers.get(i).get(j)) {
                        way.stream(stream);
                    }
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
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
        FileWriter writer = new FileWriter("zoomLayers/config");
        StringBuilder builder = new StringBuilder();
        builder.append("minlat: ").append(minlat).append("\n")
                .append("maxlat: ").append(maxlat).append("\n")
                .append("minlon: ").append(minlon).append("\n")
                .append("maxlon: ").append(maxlon).append("\n")
                .append("chunkColumnAmount: ").append(chunkColumnAmount).append("\n")
                .append("chunkRowAmount: ").append(chunkRowAmount).append("\n")
                .append("chunkAmount: ").append(chunkAmount).append("\n")
                .append("CHUNK_SIZE: ").append(CHUNK_SIZE).append("\n");
        writer.write(builder.toString());
        writer.close();
    }

    public void printAll() {
        for (int i = 0; i < zoomLayers.size(); i++) {
            // System.out.println(chunks.get(i);
            System.out.println("nr " + i + ": " + zoomLayers.get(i).size());
        }
        System.out.println(tempCounter);
    }

    public void setWays(ArrayList<Way> rawWay) {
        this.rawWays = rawWay;
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
