package itu.map.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.OSMParser;
import dk.itu.map.structures.DrawableWay;

public class OSMParserTest {
    
    @Test
    public void OSMParserWriteAndReadTest() {
        String path = "C:/Users/nickl/Downloads/testmap4.osm"; //AAA FIX
        OSMParser parser = new OSMParser(new File(path), "maps/aaasdsaasads");
        parser.start();
        parser.setCallback((Runnable)() -> {
            ChunkLoader chunkLoader = new ChunkLoader("maps/aaasdsaasads");
            Map<Integer, List<DrawableWay>> chunkMap = chunkLoader.readFiles(new int[]{0}, 0);
            System.out.println(chunkMap.size());
            assertEquals(chunkMap.get(0).size(), 21);
        });
    }
}
