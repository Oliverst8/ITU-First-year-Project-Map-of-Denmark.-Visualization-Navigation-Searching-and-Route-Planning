package dk.itu.map;

import java.io.File;
import java.util.*;

import java.io.IOException;
import java.io.Serializable;

import javax.xml.stream.XMLStreamException;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.parser.FileHandler;
import dk.itu.map.structures.Way;
import javafx.geometry.Point2D;

public class Model implements Serializable {
    
    List<Map<Integer,List<Way>>> chunkLayers;
    final ChunkHandler chunkHandler;

    public Model(ChunkHandler chunkHandler) {
        this.chunkHandler = chunkHandler;
        chunkLayers = new ArrayList<>();
        for(int i = 0; i <= 4; i++)
            chunkLayers.add(new HashMap<>());
    }

    private void updateChunkLevel(Set<Integer> chunkSet, int zoomLevel) {

        Map<Integer, List<Way>> chunks = chunkLayers.get(zoomLevel);

        chunkSet.removeAll(chunks.keySet());

        int[] newChunks = new int[chunkSet.size()];

        int c = 0;
        for (int chunk : chunkSet) {
            newChunks[c++] = chunk;
        }
        if(chunkSet.isEmpty()) {
            return;
        }
        chunks.putAll(chunkHandler.loadBytes(newChunks, zoomLevel));
    }

    public void updateChunks(Set<Integer> chunks, int zoomLevel) {

        for(Map<Integer, List<Way>> chunkLayers : chunkLayers) {
            chunkLayers.keySet().retainAll(chunks);
        }

            for(int i = zoomLevel; i <= 4; i++) {
                updateChunkLevel(chunks, i);
            }


    }




}
