package itu.map;

import dk.itu.map.parser.GraphBuilder;
import dk.itu.map.structures.ArrayLists.CoordArrayListV2;
import dk.itu.map.structures.ArrayLists.FloatArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dk.itu.map.parser.Way;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphBuilderTest {
    GraphBuilder graphBuilder;
    @BeforeEach
    void setUp(){
        graphBuilder = new GraphBuilder();
    }

    @Test
    void testAddEdges() throws InterruptedException {
        GraphBuilder graph = new GraphBuilder();
        CoordArrayListV2 nodes1 = TestUtilities.createCoordArrayList(0f,0f,1f,1f);
        List<String> stringList = new ArrayList<>();
        CoordArrayListV2 nodes2 = TestUtilities.createCoordArrayList(1f,1f,3f,1f);
        LongArrayList nodeIDs1 = new LongArrayList();
        nodeIDs1.add(1);
        nodeIDs1.add(2);
        LongArrayList nodeIDs2 = new LongArrayList();
        nodeIDs2.add(2);
        nodeIDs2.add(3);
        Way way1 = new Way(0, stringList, nodes1, nodeIDs1);
        Way way2 = new Way(0, stringList, nodes2, nodeIDs2);

        graph.addWay(way1);
        graph.addWay(way2);
        Thread thread = new Thread(graph);
        thread.start();
        Thread.sleep(10);
        graph.stop();
        thread.join();
        FloatArrayList expectedWeights = new FloatArrayList();
        expectedWeights.add((float) Math.sqrt(2));
        expectedWeights.add((float) Math.sqrt(2));
        expectedWeights.add(2f);
        expectedWeights.add(2f);

        IntArrayList expectedDestinations = new IntArrayList();

        expectedDestinations.add(1);
        expectedDestinations.add(0);
        expectedDestinations.add(2);
        expectedDestinations.add(1);


        IntArrayList actualDestinations = graph.getEdges();
        FloatArrayList actualWeights = graph.getWeights();

        for(int i = 0; i < expectedWeights.size(); i++){
            assertEquals(expectedWeights.get(i), actualWeights.get(i));
        }
        for(int i = 0; i < expectedDestinations.size(); i++){
            assertEquals(expectedDestinations.get(i), actualDestinations.get(i));
        }
        assertEquals(expectedWeights.size(), actualWeights.size());
        assertEquals(expectedDestinations.size(), actualDestinations.size());
    }

    @Test
    void testWriteAndRead() throws InterruptedException, IOException {
        String dataPath = TestUtilities.getTestFilesPath();
        graphBuilder = getGraph();
        graphBuilder.writeToFile(dataPath);
        GraphBuilder graph = new GraphBuilder();
        graph.loadFromDataPath(dataPath);
        FileUtils.deleteDirectory(new File(dataPath + "/graph"));
        assertEquals(graph, graphBuilder);
    }


    public GraphBuilder getGraph() throws InterruptedException {
        GraphBuilder graph = new GraphBuilder();
        CoordArrayListV2 nodes1 = TestUtilities.createCoordArrayList(0f,0f,1f,1f);
        List<String> stringList = new ArrayList<>();
        CoordArrayListV2 nodes2 = TestUtilities.createCoordArrayList(1f,1f,3f,1f);
        LongArrayList nodeIDs1 = new LongArrayList();
        nodeIDs1.add(1);
        nodeIDs1.add(2);
        LongArrayList nodeIDs2 = new LongArrayList();
        nodeIDs2.add(2);
        nodeIDs2.add(3);
        Way way1 = new Way(0, stringList, nodes1, nodeIDs1);
        Way way2 = new Way(0, stringList, nodes2, nodeIDs2);

        graph.addWay(way1);
        graph.addWay(way2);
        Thread thread = new Thread(graph);
        thread.start();
        Thread.sleep(10);
        graph.stop();
        thread.join();
        return graph;
    }

}
