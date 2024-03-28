package dk.itu.map;

import java.util.*;

import java.io.Serializable;

import dk.itu.map.parser.ChunkHandler;
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

    private void updateChunkLevel(int n, int zoomLevel) {

        Map<Integer, List<Way>> chunks = chunkLayers.get(zoomLevel);

        int[] chunkNumbers = new int[9];

        chunkNumbers[0] = n - chunkHandler.chunkColumnAmount - 1;
        chunkNumbers[1] = n - chunkHandler.chunkColumnAmount;
        chunkNumbers[2] = n - chunkHandler.chunkColumnAmount + 1;
        chunkNumbers[3] = n - 1;
        chunkNumbers[4] = n;
        chunkNumbers[5] = n + 1;
        chunkNumbers[6] = n + chunkHandler.chunkColumnAmount - 1;
        chunkNumbers[7] = n + chunkHandler.chunkColumnAmount;
        chunkNumbers[8] = n + chunkHandler.chunkColumnAmount + 1;

        int[] newChunks = new int[9];

        int c = 0;
        for (int chunk : chunkNumbers) {
            if (chunk < 0 || chunk >= chunkHandler.chunkAmount) continue;
            if (chunks.containsKey(chunk)) continue;
            newChunks[c++] = chunk;
        }

        while (c < newChunks.length) {
            newChunks[c++] = -1;
        }


        chunks.putAll(chunkHandler.loadBytes(newChunks, zoomLevel));
    }

    private void updateChunkLevel(Set<Integer> chunkSet, int zoomLevel) {

        Map<Integer, List<Way>> chunks = chunkLayers.get(zoomLevel);

        int[] newChunks = new int[chunkSet.size()];

        int c = 0;
        for (int chunk : chunkSet) {
            if (chunk < 0 || chunk >= chunkHandler.chunkAmount) continue;
            if (chunks.containsKey(chunk)) continue;
            newChunks[c++] = chunk;
        }

        while (c < newChunks.length) {
            newChunks[c++] = -1;
        }


        chunks.putAll(chunkHandler.loadBytes(newChunks, zoomLevel));
    }

    public void updateChunks(Set<Integer> chunks, int zoomLevel) {
        System.out.println("Updating " + chunks.size() + " chunks");
        System.out.println("Zoom level: " + zoomLevel);
        for(Map<Integer, List<Way>> chunkLayers : chunkLayers)
            chunkLayers.keySet().retainAll(chunks);

        for(int n : chunks)
            for(int i = zoomLevel; i <= 4; i++)
                updateChunkLevel(n, i);
    }

    public void updateChunk(int n, int zoomLevel) {
        for(int i = zoomLevel; i <= 4; i++)
            updateChunkLevel(n, i);
    }

    public void updateChunks(){

    }
}
