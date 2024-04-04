package dk.itu.map;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Way;
import dk.itu.map.parser.FileHandler;
import dk.itu.map.parser.ChunkHandler;

import java.io.*;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

public class Model implements Serializable {
    private final String dataPath = "maps";
    private final Graph graph;

    public List<Map<Integer,List<Way>>> chunkLayers;
    public ChunkHandler chunkHandler;

    public Model() {
        chunkHandler = null;
        graph = new Graph();
        chunkLayers = new ArrayList<>();
        for(int i = 0; i <= 4; i++) {
            chunkLayers.add(new HashMap<>());
        }
    }

    public void importMap(String filePath, String name) {
        try {            
            if (!new File(dataPath + "/" + name + "/config").exists()) {
                FileHandler fileHandler = new FileHandler(new File(filePath), dataPath + "/" + name);
                fileHandler.load();
            };
            String path = dataPath + "/" + name;
            chunkHandler = new ChunkHandler(path);
            graph.loadFromDataPath(path + "/utilities");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
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

    public Graph getGraph() {
        return graph;
    }
}
