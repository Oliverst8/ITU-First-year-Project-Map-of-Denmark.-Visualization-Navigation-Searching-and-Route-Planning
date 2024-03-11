package dk.itu.map.parser;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;


import javax.xml.stream.XMLStreamException;

import dk.itu.map.structures.Way;

public class ChunkHandler {

    private final String dataPath;
    
    public float minlat, maxlat, minlon, maxlon;
    
    public int chunkColumnAmount, chunkRowAmount, chunkAmount;

    //Temp variable to save loaded ways
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
            this.minlat = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.maxlat = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.minlon = Integer.parseInt(reader.readLine().split(" ")[1]);
            this.maxlon = Integer.parseInt(reader.readLine().split(" ")[1]);
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
        chunks = new ArrayList<ArrayList<Way>>(chunkAmount);
        for (int i = 0; i < chunkAmount; i++) {
            chunks.add(new ArrayList<Way>());
        }
    }

    public void load(int chunk) throws IOException, XMLStreamException {
        File file = new File(this.dataPath + "/chunk" + chunk + ".txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        float[] coords;
        String[] tags;
        while ((line = reader.readLine()) != null) {
            if (line.equals("Nodes:")) {
                coords = new float[Integer.parseInt(reader.readLine())];
                for (int i = 0; i < coords.length/2; i++) {
                    String[] numbers = reader.readLine().split(" ");
                    coords[i*2] = Float.parseFloat(numbers[0]);
                    coords[i*2+1] = Float.parseFloat(numbers[1]);
                }
                line = reader.readLine();
                if(!line.equals("Tags:")) throw new RuntimeException(line);
                
                tags = new String[Integer.parseInt(reader.readLine())];
                for (int i = 0; i < tags.length/2; i++) {
                    String[] numbers = reader.readLine().split(" ", 2);
                    if (i == 9) {
                        System.out.println(tags.length);
                    }
                    tags[i*2] = numbers[0];
                    tags[i*2+1] = numbers[1];
                }
                chunks.get(chunk).add(new Way(coords, tags));
            }
        }
        reader.close();
    }

    public ArrayList<Way> getChunk(int chunk) {
        return chunks.get(chunk);
    }
}

/**
Nodes:
53.60783 -3.0618331
53.634167 -3.101333
53.65 -3.1172
53.65075 -3.133333
53.750782 -3.883333
53.75102 -3.917617
53.751316 -3.934283
53.76695 -4.016667
53.76673 -4.05105
53.81682 -4.634433
53.81695 -4.65105
53.384766 -6.101533
53.3847 -6.117417
53.384632 -6.117517
Tags:
communication line
description Southport to Dublin
name Hibernia 'C'
operator Hibernia Atlantic
phone +44 7771 730654
seamark:cable_submarine:category fibre_optic
seamark:type cable_submarine
source Kingfisher Information Service, February 2010, http://www.kisca.org.uk/charts.htm
submarine yes
Nodes:
53.60783 -3.0618331
53.634167 -3.101333
53.65 -3.1172
53.65075 -3.133333
53.6515 -3.1345
53.6669 -3.150233
 */