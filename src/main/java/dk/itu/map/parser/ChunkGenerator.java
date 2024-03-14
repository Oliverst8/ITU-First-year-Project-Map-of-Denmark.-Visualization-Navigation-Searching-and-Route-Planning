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

    private ArrayList<ArrayList<ArrayList<Way>>> chunks;
    private File[][] files;

    private int tempCounter = 0;
    private List<Way> rawWays;
    private boolean hasMoreWork;
    private final int MIN_ARRAY_LENGTH = 150_000;
    

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

        chunks = new ArrayList<ArrayList<ArrayList<Way>>>(chunkAmount);
        for(int i = 0; i < 5; i++){
            chunks.add(new ArrayList<ArrayList<Way>>());
            for (int j = 0; j < chunkAmount; j++) {
                chunks.get(i).add(new ArrayList<Way>());
            }
        }

        files = new File[5][chunkAmount];

        System.out.println(chunkRowAmount + " " + chunkColumnAmount);

        try {
            File folder = new File("zoomLayers");
            if (!folder.exists()) {
                folder.mkdir();
                for(int i = 0; i < 5; i++ ){
                    File innerFolder = new File("zoomLayers/zoom" + i);
                    innerFolder.mkdir();
                    for (int j = 0; j < chunkAmount; j++) {
                        files[i][j] = new File("zoomLayers/zoom" + i + "/chunk" + j +".txt");

                    }
                }
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
            //way.setZoomLevel( zoomLevel);

            float[] coords = way.getCoords();
            for (int i = 0; i < coords.length; i += 2) {
                float lat = coords[i];
                float lon = coords[i + 1];

                int chunkIndex = coordsToChunkIndex(lat, lon);

                if (chunkIndex < chunkAmount && chunkIndex >= 0) {
                    chunks.get(zoomLevel).get(chunkIndex).add(way);
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
            chunks = new ArrayList<ArrayList<ArrayList<Way>>>(chunkAmount);
            for(int i = 0; i < 5; i++){
                chunks.add(new ArrayList<ArrayList<Way>>());
                for (int j = 0; j < chunkAmount; j++) {
                    chunks.get(i).add(new ArrayList<Way>());
                }
            }

            chunkWays();

            writeFiles();
        }
        System.out.println("Finished while loop");
    }

    public void writeFiles() {
        IntStream.range(0, 5).forEach(i -> {
            IntStream.range(0, chunks.size()).parallel().forEach(j -> {
                try {

                    DataOutputStream stream = new DataOutputStream(
                            new BufferedOutputStream(new FileOutputStream(files[i][j], true)));
                    for (Way way : chunks.get(i).get(j)) {
                        way.stream(stream);
                    }
                    stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
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
