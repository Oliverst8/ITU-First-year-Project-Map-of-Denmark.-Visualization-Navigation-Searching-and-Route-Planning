package itu.map;

import dk.itu.map.structures.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testGraph() {
        Graph graph = new Graph();
        assertNotNull(graph);
    }

    @Test
    void testAddEdges() throws InterruptedException {
        Graph graph = new Graph();
        List<Float> nodes1 = Arrays.asList(0f,0f,1f,1f);
        List<String> stringList = new ArrayList<>();
        List<Float> nodes2 = Arrays.asList(1f,1f,3f,1f);
        Way way1 = new Way(nodes1, stringList, new long[]{0, 1});
        Way way2 = new Way(nodes2, stringList, new long[]{1, 2});
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
    void testAddIDs() throws InterruptedException {
        Graph graph = new Graph();
        List<Float> nodes1 = Arrays.asList(0f,0f,1f,1f);
        List<String> stringList1 = Arrays.asList("id", "1");
        List<String> stringList2 = Arrays.asList("id", "2");
        List<Float> nodes2 = Arrays.asList(1f,1f,3f,1f);
        Way way1 = new Way(nodes1, stringList1, new long[]{0, 1});
        Way way2 = new Way(nodes2, stringList2, new long[]{1, 2});
        graph.addWay(way1);
        graph.addWay(way2);
        Thread thread = new Thread(graph);
        thread.start();
        Thread.sleep(100);
        graph.stop();
        thread.join();
        LongArrayList expected = new LongArrayList();
        expected.add(1);
        expected.add(1);
        expected.add(2);
        expected.add(2);

        LongArrayList actual = graph.getIds();

        for(int i = 0; i < expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
        assertEquals(expected.size(), actual.size());
    }

}
