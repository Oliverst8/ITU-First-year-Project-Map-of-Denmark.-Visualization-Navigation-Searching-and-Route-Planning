package dk.itu.map;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.parser.FileHandler;
import dk.itu.map.structures.Way;

public class Model implements Serializable {

    List<List<Way>> ways = new ArrayList<>();
    ChunkHandler chunkHandler = new ChunkHandler("C:/Users/augus/OneDrive/Documents/ITU/2. semester/1. year project/BFST2024Group8/chunkData");

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
        ways.clear();
        try{
            ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount-1));
            ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount));
            ways.add(chunkHandler.loadBytes(n-chunkHandler.chunkColumnAmount+1));
            ways.add(chunkHandler.loadBytes(n-1));
            ways.add(chunkHandler.loadBytes(n));
            ways.add(chunkHandler.loadBytes(n+1));
            ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount-1));
            ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount));
            ways.add(chunkHandler.loadBytes(n+chunkHandler.chunkColumnAmount+1));

        } catch(IOException e){
            System.out.println(e.getStackTrace());
        }

    }
}
