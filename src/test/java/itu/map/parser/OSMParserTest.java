package itu.map.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dk.itu.map.App;
import dk.itu.map.parser.FileProgress;
import dk.itu.map.structures.Drawable;
import javafx.scene.control.ProgressBar;
import org.junit.jupiter.api.Test;

import dk.itu.map.parser.ChunkLoader;
import dk.itu.map.parser.OSMParser;
import itu.map.TestUtilities;

public class OSMParserTest {
    
//     @Test
//     public void OSMParserWriteAndReadTest() throws InterruptedException {
//         String path = TestUtilities.getTestFilesPath();
//         App.DATA_PATH = path;
//         App.mapPath = path + "/testmap/";
//         FileProgress fileProgress = new FileProgress(null);
//         OSMParser parser = new OSMParser(new File(path + "testmap.osm"), fileProgress);
//         parser.start();
//         parser.join();
//
//         ChunkLoader chunkLoader = new ChunkLoader();
//         chunkLoader.readFiles(new int[]{0}, 0);
//         Map<Integer, Map<Integer, List<Drawable>>> chunkMap = new HashMap<>();
//         chunkLoader.setCallback(() -> {
//             Map<Integer, Map<Integer, List<Drawable>>> newChunks = chunkLoader.getFinishedChunks();
//             for (int i = 0; i < 6; i++) {
//                 Map<Integer, List<Drawable>> chunks = chunkMap.getOrDefault(i, new HashMap<>());
//                 Map<Integer, List<Drawable>> newLayer = newChunks.get(i);
//                 if (newLayer == null) {
//                     continue;
//                 }
//                 chunks.putAll(newLayer);
//                 chunkMap.put(i, chunks);
//             }
//         });
//         chunkLoader.start();
//         chunkLoader.join();
//         //Map<Integer, List<Drawable>> chunkMap = chunkLoader.getChunkMap();
//                 System.out.println(chunkMap.size());
//         assertEquals(chunkMap.get(0).size(), 16);
//     }
}
