package dk.itu.map;

import java.util.*;

import java.io.Serializable;

import dk.itu.map.parser.ChunkHandler;
import dk.itu.map.structures.Way;

public class Model implements Serializable {

    Map<Integer, List<Way>> chunks = new HashMap<>();
    final ChunkHandler chunkHandler;

    public Model(ChunkHandler chunkHandler) {
        this.chunkHandler = chunkHandler;

    }

    public void updateChunk(int n) {

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
        // System.out.println(c);
        while (c < newChunks.length) {
            newChunks[c++] = -1;
        }


        chunks.putAll(chunkHandler.loadBytes(newChunks, 0));
    }
}
