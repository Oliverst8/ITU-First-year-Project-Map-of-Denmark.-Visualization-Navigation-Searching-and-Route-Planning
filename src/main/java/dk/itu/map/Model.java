package dk.itu.map;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.Serializable;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.structures.Way;

public class Model implements Serializable {

    List<List<Way>> ways = new ArrayList<>();
    
    ChunkHandler chunkHandler;

    double minlat, maxlat, minlon, maxlon;

    public Model(ChunkHandler chunkHandler) {
        this.chunkHandler = chunkHandler;

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
