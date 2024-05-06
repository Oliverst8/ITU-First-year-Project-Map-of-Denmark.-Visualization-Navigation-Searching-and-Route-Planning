package itu.map;


import dk.itu.map.parser.Way;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import dk.itu.map.structures.Graph;
import dk.itu.map.parser.GraphBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class GraphTest {

    private GraphBuilder getGraph1(){
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary");

        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //green
        coords = TestUtilities.createCoordArrayList(new float[]{1f,2f,1f,3f,1f,4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{2,3,4});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blue
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f,2f,2f,4f,2f,5f,4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,5,7,10});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //red
        coords = TestUtilities.createCoordArrayList(new float[]{4f,4f,3f,3f,4f,2f,5f,2f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{9,6,7,8});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //greenStriped
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f,4f,2f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,7});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //redStriped1note
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 100f, 8f, 7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,11,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStriped
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f, 2f,4f, 4f,5f, 5f,6f, 8f,7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,13,14,15,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        graph.stop();
        graph.run();
        return graph;
    }
    @Test
    void testEqualsNotEqualsDifferentObject() {
        Graph graph = new Graph();
        Object obj = new Object();
        assertNotEquals(graph, obj);
    }
    @Test
    void testNearestNeighborSamePoint(){
        Graph graph = getGraph1();
        float[] point = new float[]{1f, 100f};
        int vehicleCode = 1;
        int result = graph.getNearestNeigherborID(point, vehicleCode, graph);

        assertEquals(point[0], graph.getCoords(result)[0]);
        assertEquals(point[1], graph.getCoords(result)[1]);
    }
    @Test
    void testNearestNeighborPoint(){
        Graph graph = getGraph1();
        float[] point = new float[]{2f, 3f};
        int vehicleCode = 1;
        int result = graph.getNearestNeigherborID(point, vehicleCode, graph);

        float[] expected = new float[]{3f, 3f};

        assertEquals(expected[0], graph.getCoords(result)[0]);
        //assertEquals(expected[1], graph.getCoords(result)[1]);
    }

}
