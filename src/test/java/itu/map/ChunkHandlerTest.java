package itu.map;

import org.junit.Test;

import dk.itu.map.App;
import dk.itu.map.parser.ChunkHandler;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

public class ChunkHandlerTest {
    @Test public void readChunkTest() {
        ChunkHandler chunkHandler = new ChunkHandler("chunkData");
        long startTime = System.nanoTime();

        try {
            chunkHandler.load(10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        assertEquals(317843, chunkHandler.getChunk(10).size());
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1_000_000_000.0);

    }
}
