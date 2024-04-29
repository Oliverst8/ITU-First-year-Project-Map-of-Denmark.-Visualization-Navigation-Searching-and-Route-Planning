package itu.map.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;
import java.util.Map;


import dk.itu.map.structures.Drawable;
import org.junit.jupiter.api.Test;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.OSMParser;
import itu.map.TestUtilities;

public class OSMParserTest {
    
    @Test
    public void OSMParserWriteAndReadTest() {
        String path = TestUtilities.getTestFilesPath();
        OSMParser parser = new OSMParser(new File(path + "testmap.osm"), path + "maps/testmap");
        parser.start();
        try {
            parser.join();
        } catch (InterruptedException e) {

        }
        ChunkLoader chunkLoader = new ChunkLoader(path + "maps/testmap");
        Map<Integer, List<Drawable>> chunkMap = chunkLoader.readFiles(new int[]{0}, 0);
        System.out.println(chunkMap.size());
        assertEquals(chunkMap.get(0).size(), 16);
    }
}
