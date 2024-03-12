package dk.itu.map.parser;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import java.util.stream.IntStream;

import dk.itu.map.structures.Way;

public class ChunkGenerator {
    // chunk size in coordinate size
    private final float chunkSize = 0.25f;
    
    public float minlat, maxlat, minlon, maxlon;
    
    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    private ArrayList<ArrayList<Way>> chunks;
    private File[] files;

    private int tempCounter = 0;
    

    public ChunkGenerator(float minlat, float maxlat, float minlon, float maxlon) {

        this.minlat = minlat;
        this.maxlat = maxlat;
        this.minlon = minlon;
        this.maxlon = maxlon;

        chunkColumnAmount = (int)Math.ceil(Math.abs(maxlon-minlon)/chunkSize);
        chunkRowAmount = (int)Math.ceil(Math.abs(maxlat-minlat)/chunkSize);
        
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
            }
            
            for (int i = 0; i < chunkAmount; i++) {
                files[i] = new File("chunkData/chunk" + i + ".txt");
            }
        } catch(Exception e) {
            System.out.println("failed " + e.getMessage());
        }
    }

    private int coordsToChunkIndex(float lat, float lon) {
        return (int)Math.floor((lon-minlon)/chunkSize) + 
        (int)Math.floor((lat-minlat)/chunkSize) * chunkColumnAmount;
    }

    public void addWay(Way way){
        tempCounter++;
        float[] coords = way.getCoords();
        for(int i=0; i<coords.length; i+=2){
            float lat = coords[i];
            float lon = coords[i+1];

            int chunkIndex = coordsToChunkIndex(lat, lon);
            
            if (chunkIndex < chunkAmount && chunkIndex >= 0) {
                chunks.get(chunkIndex).add(way);
            }
        }
    }

    public void writeFiles() {
        IntStream.range(0, chunks.size()).parallel().forEach(i -> {
            try {
                DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files[i])));
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
}
