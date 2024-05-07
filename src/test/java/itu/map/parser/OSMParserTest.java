package itu.map.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dk.itu.map.App;
import dk.itu.map.parser.FileProgress;
import dk.itu.map.structures.Drawable;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.OSMParser;
import itu.map.TestUtilities;

public class OSMParserTest {
    
    @Test
    public void OSMParserWriteAndReadTest() throws InterruptedException, IOException {
        String path = TestUtilities.getTestFilesPath();
        App.DATA_PATH = path;
        App.mapName = "testmap/";
        FileProgress fileProgress = new FileProgress(null);
        OSMParser parser = new OSMParser(new File(path + "testmap.osm"), fileProgress);
        parser.start();
        parser.join();

        ChunkLoader chunkLoader = new ChunkLoader("external");
        Map<Integer, Map<Integer, List<Drawable>>> chunkMap = new HashMap<>();
        chunkLoader.readFiles(new int[]{0}, 0);
        while (chunkMap.size() <= 0) {
            Thread.sleep(100);
            Map<Integer, Map<Integer, List<Drawable>>> newChunks = chunkLoader.getFinishedChunks();
            for (int i = 0; i < 6; i++) {
                Map<Integer, List<Drawable>> chunks = chunkMap.getOrDefault(i, new HashMap<>());
                Map<Integer, List<Drawable>> newLayer = newChunks.get(i);
                if (newLayer == null) {
                    continue;
                }
                chunks.putAll(newLayer);
                chunkMap.put(i, chunks);
            }
        }
        System.out.println(chunkMap.size());
        FileUtils.deleteDirectory(new File(App.DATA_PATH + App.mapName));
        assertEquals(10, chunkMap.get(0).get(0).size());
    }
}
