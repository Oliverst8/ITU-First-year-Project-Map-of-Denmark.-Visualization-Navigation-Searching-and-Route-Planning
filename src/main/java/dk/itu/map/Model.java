package dk.itu.map;

import java.io.File;
import java.util.*;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.parser.FileHandler;
import dk.itu.map.structures.Way;

public class Model implements Serializable {

    Map<Integer, List<Way>> chunks = new HashMap<>();
    ChunkHandler chunkHandler = new ChunkHandler("chunkData");

    double minlat, maxlat, minlon, maxlon;

    public Model(FileHandler fileHandler) throws XMLStreamException, IOException {
        fileHandler.load();
        //ways = fileHandler.ways;
        minlat = fileHandler.minlat;
        maxlat = fileHandler.maxlat;
        minlon = fileHandler.minlon;
        maxlon = fileHandler.maxlon;
    }

    public void updateChunk(int n){

            Set<Integer> chunkNumbers = new HashSet<>();
                chunkNumbers.add(n-chunkHandler.chunkColumnAmount-1);
                chunkNumbers.add(n-chunkHandler.chunkColumnAmount);
                chunkNumbers.add(n-chunkHandler.chunkColumnAmount+1);
                chunkNumbers.add(n-1);
                chunkNumbers.add(n);
                chunkNumbers.add(n+1);
                chunkNumbers.add(n+chunkHandler.chunkColumnAmount-1);
                chunkNumbers.add(n+chunkHandler.chunkColumnAmount);
                chunkNumbers.add(n+chunkHandler.chunkColumnAmount+1);


            chunkNumbers.removeAll(chunks.keySet());

            int[] chunkNumbersArray = new int[chunkNumbers.size()];

            Object[] tempArray = chunkNumbers.toArray();

            System.out.println("chunkNumbers.size() = " + chunkNumbers.size());

            for(int i = 0; i < chunkNumbers.size(); i++){
                chunkNumbersArray[i] = (int) tempArray[i];
            }

            chunks = chunkHandler.loadBytes(chunkNumbersArray);



            //ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount-1));
            //ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount));
            //ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount+1));
            //ways.add(chunkHandler.loadBytes(n-1));
            //ways.add(chunkHandler.loadBytes(n));
            //ways.add(chunkHandler.loadBytes(n+1));
            //ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount-1));
            //ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount));
            //ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount+1));



    }
}
