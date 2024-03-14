package dk.itu.map.parser;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import dk.itu.map.structures.Way;

public class ChunkGenerator implements Runnable {
    // chunk size in coordinate size
    private final float chunkSize = 0.25f;

    public float minlat, maxlat, minlon, maxlon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    private ArrayList<ArrayList<Way>> chunks;
    private File[] files;

    private int tempCounter = 0;
    private List<Way> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;

    private final HashSet<String> zoomWords0 = new HashSet<>(Arrays.asList("building", "highway"));
    private final HashSet<String> zoomWords1 = new HashSet<>(Arrays.asList("terminal", "gate"));
    private final HashSet<String> zoomWords2 = new HashSet<>(Arrays.asList("runway", "tertiary", "tertiary_link", "unclassified", "residential"));
    private final HashSet<String> zoomWords3 = new HashSet<>(Arrays.asList("secondary", "secondary_link", "rail", "light_rail"));
    private final HashSet<String> zoomWords4 = new HashSet<>(Arrays.asList("aerodrome", "motorway", "motorway_link", "trunk", "trunk_link", "primary", "primary_link", "fell", "grassland", "heath", "scrub", "wood", "bay", "beach", "coastline", "cape", "water", "wetland"));
    

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

        chunkColumnAmount = (int) Math.ceil(Math.abs(maxlon - minlon) / chunkSize);
        chunkRowAmount = (int) Math.ceil(Math.abs(maxlat - minlat) / chunkSize);

        chunkAmount = chunkColumnAmount * chunkRowAmount;

        chunks = new ArrayList<ArrayList<Way>>(chunkAmount);
        for (int i = 0; i < chunkAmount; i++) {
            chunks.add(new ArrayList<Way>());
        }
        files = new File[chunkAmount];

        System.out.println(chunkRowAmount + " " + chunkColumnAmount);

        try {
            File folder = new File("chunkData");
            if (!folder.exists()) {
                folder.mkdir();
            } else {
                File[] files = folder.listFiles();
                int count = 0;
                if (files != null) {
                    for (File file : files) {
                        count++;
                        file.delete();
                        if (count > 500) {
                            System.err.println("STOPPPPPP");
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < chunkAmount; i++) {
                files[i] = new File("chunkData/chunk" + i + ".txt");
            }
        } catch (Exception e) {
            System.out.println("failed " + e.getMessage());
        }
    }

    private int coordsToChunkIndex(float lat, float lon) {
        return (int) Math.floor((lon - minlon) / chunkSize) +
                (int) Math.floor((lat - minlat) / chunkSize) * chunkColumnAmount;
    }

    public void addWay(Way way) { // main
        rawWays.add(way);
    }

    public void chunkWays() {
        List<Way> newWays = rawWays;
        System.out.println("chunking: " + newWays.size());
        rawWays = Collections.synchronizedList(new ArrayList<>(MIN_ARRAY_LENGTH));
        for(Way way : newWays) {
            byte zoomLevel = -1;
            String[] tags = way.getTags();
            for(String tag : tags){
                switch (tag) {
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
                    case "motorway":
                    case "motorway_link":
                    case "trunk":
                    case "trunk_link":
                    case "primary":
                    case "primary_link":
                    case "aerodrome":
                        zoomLevel = 4;
                        break;
                    case "secondary":
                    case "secondary_link":
                    case "rail":
                    case "light_rail":
                        if(zoomLevel < 3) zoomLevel = 3;
                        break;
                    case "runway":
                    case "tertiary":
                    case "tertiary_link":
                    case "unclassified":
                    case "residential":
                        if(zoomLevel < 2) zoomLevel = 2;
                        break;
                    case "terminal":
                    case "gate":
                        if(zoomLevel < 1) zoomLevel = 1;
                        break;
                    case "building":
                    case "highway":
                        if(zoomLevel < 0) zoomLevel = 0;
                        break;
                }
            }
            if(zoomLevel == -1) continue;
            way.setZoomLevel( zoomLevel);

            float[] coords = way.getCoords();
            for (int i = 0; i < coords.length; i += 2) {
                float lat = coords[i];
                float lon = coords[i + 1];

                int chunkIndex = coordsToChunkIndex(lat, lon);

                if (chunkIndex < chunkAmount && chunkIndex >= 0) {
                    chunks.get(chunkIndex).add(way);
                }
            }
        }
    }

    public void run() {
        hasMoreWork = true;
        while (hasMoreWork || rawWays.size() > 0) {
            if(rawWays.size() < 100_000 && hasMoreWork) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            chunks = new ArrayList<ArrayList<Way>>(chunkAmount);
            for (int i = 0; i < chunkAmount; i++) {
                chunks.add(new ArrayList<Way>());
            }
            
            chunkWays();

            writeFiles();
        }
        System.out.println("Finished while loop");
    }

    public void writeFiles() {
        IntStream.range(0, chunks.size()).parallel().forEach(i -> {
            try {

                DataOutputStream stream = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(files[i], true)));
                for (Way way : chunks.get(i)) {
                    way.stream(stream);
                }
                stream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeConfig() throws IOException {
        FileWriter writer = new FileWriter("chunkData/config");
        StringBuilder builder = new StringBuilder();
        builder.append("minlat: ").append(minlat).append("\n")
        .append("maxlat: ").append(maxlat).append("\n")
        .append("minlon: ").append(minlon).append("\n")
        .append("maxlon: ").append(maxlon).append("\n")
        .append("chunkColumnAmount: ").append(chunkColumnAmount).append("\n")
        .append("chunkRowAmount: ").append(chunkRowAmount).append("\n")
        .append("chunkAmount: ").append(chunkAmount).append("\n");
        writer.write(builder.toString());
        writer.close();
    }

    public void printAll() {
        for (int i = 0; i < chunks.size(); i++) {
            // System.out.println(chunks.get(i);
            System.out.println("nr " + i + ": " + chunks.get(i).size());
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
