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

import java.util.ArrayList;

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

    public ArrayList<Way> loadBytes(int chunk) throws IOException {

        if(chunk < 0 || chunk >= chunkAmount){
            return new ArrayList<Way>();
        }
        File file = new File(this.dataPath + "/chunk" + chunk + ".txt");

        float[] coords;
        String[] tags;
        ArrayList<Way> ways = new ArrayList<>();

        try (DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))){
            while (true) {
                coords = new float[stream.readInt()];
                for (int i = 0; i < coords.length; i++) {
                    coords[i] = stream.readFloat();
                }
                tags = new String[stream.readInt()];
                for (int i = 0; i < tags.length; i++) {
                    tags[i] = stream.readUTF();
                }
                ways.add(new Way(coords, tags));
            }
            /*The steam will throw an end of file exception when its done,
            this way we can skip checking if we are done reading every loop run, and save time*/
        } catch(EOFException e){
            //End of file reached
        }

        return ways;
    }


}

