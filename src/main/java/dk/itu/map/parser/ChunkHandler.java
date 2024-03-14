package dk.itu.map.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileReader;

import java.util.*;
import java.util.stream.IntStream;

import dk.itu.map.structures.Way;

public class ChunkHandler {

    private final String dataPath;

    public float minlat, maxlat, minlon, maxlon;

    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    // Temp variable to save loaded ways
    public ArrayList<Way> ways;

    private ArrayList<ArrayList<Way>> chunks;

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

    public Map<Integer, List<Way>> loadBytes(int chunk) {
        return loadBytes(new int[]{chunk});
    }

    public Map<Integer, List<Way>> loadBytes(int[] chunks) {

        //List<Way> ways = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, List<Way>> ways = Collections.synchronizedMap(new HashMap<>());

        for (int chunk : chunks) {
            ways.put(chunk, new ArrayList<>());
        }

        long StartTime = System.nanoTime();
        IntStream.range(0, chunks.length).parallel().forEach(i -> {

            if(chunks[i] < 0 || chunks[i] >= chunkAmount) return;

            File file = new File(this.dataPath + "/chunk" + chunks[i] + ".txt");

            float[] coords;
            String[] tags;
            try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))){
                while (true) {
                    coords = new float[stream.readInt()];
                    for (int j = 0; j < coords.length; j++) {
                        coords[j] = stream.readFloat();
                    }
                    tags = new String[stream.readInt()];
                    for (int j = 0; j < tags.length; j++) {
                        tags[j] = stream.readUTF();
                    }
                    ways.get(chunks[i]).add(new Way(coords, tags));
                }
                /*The steam will throw an end of file exception when its done,
                this way we can skip checking if we are done reading every loop run, and save time*/
            } catch(EOFException e){
                //End of file reached
            } catch (IOException e) {
                //Since we run it in parallel we need to return a runtime exception if since we can throw the IOException out of the scope
                throw new RuntimeException(e);
            }
        });

        long EndTime = System.nanoTime();

        System.out.println("Reading " + chunks.length + " chunks took: " + ((EndTime-StartTime)/1_000_000_000.0) + "s");

        return ways;

    }


}

