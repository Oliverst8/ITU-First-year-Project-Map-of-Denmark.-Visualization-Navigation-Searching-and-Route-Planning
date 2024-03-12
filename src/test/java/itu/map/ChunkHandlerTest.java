package itu.map;

import org.junit.Test;

import dk.itu.map.parser.ChunkHandler;

import static org.junit.Assert.*;

import java.io.IOException;

public class ChunkHandlerTest {
    @Test public void readChunkTest() {
        ChunkHandler chunkHandler = new ChunkHandler("chunkData");
        long startTime = System.nanoTime();

        try {
            chunkHandler.loadBytes(75);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(1047190, chunkHandler.getChunk(75).size());
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1_000_000_000.0);

    }
}
