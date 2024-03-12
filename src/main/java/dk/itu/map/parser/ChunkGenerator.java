package dk.itu.map.parser;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
        for (int i = 0; i < chunkAmount; i++) {
            try {
                // FileWriter writer = new FileWriter(files[i]);
                DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(files[i])));
                for (Way way : chunks.get(i)) {
                    // writer.write(way.toString());
                    way.stream(stream);
                }

                // writer.close();
                stream.close();
                
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }

        try {

            FileWriter writer = new FileWriter("chunkData/config");
            StringBuilder builder = new StringBuilder();
            builder.append("minlat: " + minlat + "\n");
            builder.append("maxlat: " + maxlat + "\n");
            builder.append("minlon: " + minlon + "\n");
            builder.append("maxlon: " + maxlon + "\n");
            builder.append("chunkColumnAmount: " + chunkColumnAmount + "\n");
            builder.append("chunkRowAmount: " + chunkRowAmount + "\n");
            builder.append("chunkAmount: " + chunkAmount + "\n");
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printAll() {
        for (int i = 0; i < chunks.size(); i++) {
            // System.out.println(chunks.get(i);
            System.out.println("nr " + i + ": " + chunks.get(i).size());
        }
        System.out.println(tempCounter);
    }
}
