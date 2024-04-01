package dk.itu.map.parser;

import dk.itu.map.structures.Way;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;

class Chunk extends HashSet<Way> {}
class ZoomLayer extends ArrayList<Chunk> {}

public class ChunkGenerator implements Runnable {
    // chunk size in coordinate size
    private final String dataPath;
    private final float CHUNK_SIZE = 0.05f;
    private final byte amountOfZoomLayers = 5;

    public float minlat, maxlat, minlon, maxlon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    private ArrayList<ZoomLayer> zoomLayers;
    private File[][] files;

    private int tempCounter = 0;
    private List<Way> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;

    private Thread chunkingThread;

    public ChunkGenerator(String dataPath, float minlat, float maxlat, float minlon, float maxlon) {
        this.dataPath = dataPath;
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
        forWay:
        for (Way way : newWays) {
            byte zoomLevel = -1;
            String[] tags = way.getTags();
            for (int i = 0; i < tags.length; i += 2) {
                switch (tags[i]) {
                    case "route":
                        switch (tags[i + 1]) {
                            case "ferry":
                            case "ferry_link":
                                continue forWay;
                        }

                    case "aeroway":
                        switch (tags[i + 1]) {
                            case "aerodrome":
                                if (zoomLevel < 3) zoomLevel = 3;

                            case "apron":
                            case "runway":
                            case "taxiway":
                                if (zoomLevel < 2) zoomLevel = 2;
                                break;
                        }
                        break;
                    
                    case "highway":
                        switch (tags[i + 1]) {
                            case "trunk":
                            case "trunk_link":
                            case "primary":
                            case "primary_link":
                            case "motorway":
                            case "motorway_link":
                                if (zoomLevel < 4) zoomLevel = 4;
                                break;

                            case "tertiary":
                            case "tertiary_link":
                                if (zoomLevel < 3) zoomLevel = 3;
                                break;
                            
                            case "service":
                            case "residential":
                            case "unclassified":
                                if (zoomLevel < 0) zoomLevel = 0;
                                break;
                        }
                        break;

                    case "natural":
                        switch (tags[i + 1]) {
                            case "wood":
                            case "water":
                            case "scrub":
                            case "beach":
                            case "meadow":
                            case "coastline":
                            case "grassland":
                                if (zoomLevel < 3) zoomLevel = 3;
                                break;
                        }
                        break;
                    
                    case "place":
                        switch (tags[i + 1]) {
                            case "island":
                                if (zoomLevel < 4) zoomLevel = 4;
                                break;
                        }
                        break;
                    
                    case "landuse":
                        switch (tags[i + 1]) {
                            case "grass":
                            case "forest":
                            case "meadow":
                            case "farmland":
                            case "military":
                            case "allotments":
                            case "industrial":
                            case "residential":
                            case "construction":
                            case "recreation_ground":
                                if (zoomLevel < 3) zoomLevel = 3;
                                break;
                        }
                        break;

                    case "leisure":
                        switch (tags[i + 1]) {
                            case "park":
                            case "golf_course":
                            case "sports_centre":
                                if (zoomLevel < 2) zoomLevel = 2;
                                break;
                        }
                        break;

                    case "amenity":
                        switch (tags[i + 1]) {
                            case "parking":
                                if (zoomLevel < 2) zoomLevel = 2;
                                break;
                        }
                        break;
                    
                    case "building":
                        switch (tags[i + 1]) {
                            case "yes":
                            if (zoomLevel < 0) zoomLevel = 0;
                        }
                        break;
                }

                // switch (tags[i]) {
                //     case "ferry":
                //         continue forWay;

                //     case "motorway":
                //     case "motorway_link":
                //     case "trunk":
                //     case "trunk_link":
                //     case "primary":
                //     case "primary_link":
                //     case "coastline":
                //     // case "land_area":
                //     // case "peninsula":
                //     // case "island":
                //         zoomLevel = 4;
                //         break;

                //     case "aerodrome":
                //     case "secondary":
                //     case "secondary_link":
                //     case "rail":
                //     case "light_rail":
                //         if (zoomLevel < 3) zoomLevel = 3;
                //         break;

                //     case "forest":
                //     case "wetland":
                //     case "runway":
                //     case "tertiary":
                //     case "tertiary_link":
                //     case "heath":
                //     case "grassland":
                //     case "farmland":
                //     case "wood":
                //     case "meadow":
                //     case "scrub":
                //     case "fell":
                //     case "recreation_ground":
                //     case "beach":
                //     case "water":
                //     case "residential":
                //     case "industrial":
                //     case "park":
                //         if (zoomLevel < 2) zoomLevel = 2;
                //         break;
                        
                //     case "unclassified":
                //         if (zoomLevel < 1) zoomLevel = 1;
                //         break;

                //     case "building":
                //     case "highway":
                //         if (zoomLevel < 0) zoomLevel = 0;
                //         break;
                // }
            }
            if (way.isRelation() && zoomLevel == -1) {
                zoomLevel = 3;
            }
            if (zoomLevel == -1) continue;
            // if (zoomLevel == -1) zoomLevel = 0;
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
                            new BufferedOutputStream(
                                new FileOutputStream(files[i][j], true)));
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
        FileWriter writer = new FileWriter(dataPath + "/config");
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
