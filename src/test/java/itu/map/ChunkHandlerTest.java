package itu.map;

import org.junit.Test;

import dk.itu.map.parser.ChunkHandler;

import static org.junit.Assert.*;

import java.io.IOException;

public class ChunkHandlerTest {
    @Test public void readChunkTest() {
        ChunkHandler chunkHandler = new ChunkHandler("chunkData2");
        long startTime = System.nanoTime();
        int chunk = 75;
        try {
            chunkHandler.loadBytes(chunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(1047190, chunkHandler.getChunk(chunk).size());
        // assertEquals(5, chunkHandler.getChunk(chunk).size());
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1_000_000_000.0);

    }
}
