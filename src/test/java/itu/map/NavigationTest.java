package itu.map;

import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.parser.Way;
import dk.itu.map.utility.Navigation;
import dk.itu.map.structures.FloatArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class    NavigationTest {
/*
    private Graph getGraph1(){
        Graph graph = new Graph();
        ArrayList tags = new ArrayList();
        tags.add("navigationPath");

        CoordArrayList coords = new CoordArrayList();
        coords.add(0f);
        coords.add(0f);
        coords.add(1f);
        coords.add(1f);
        coords.add(2f);
        coords.add(2f);
        coords.add(3f);
        coords.add(3f);
        coords.add(4f);
        coords.add(4f);
        coords.add(5f);
        coords.add(5f);
        coords.add(6f);
        coords.add(6f);
        coords.add(7f);
        coords.add(7f);
        coords.add(8f);
        coords.add(8f);
        coords.add(9f);
        coords.add(9f);

        LongArrayList nodeIDs = new LongArrayList();
        nodeIDs.add(0);
        nodeIDs.add(1);
        nodeIDs.add(2);
        nodeIDs.add(3);
        nodeIDs.add(4);
        nodeIDs.add(5);
        nodeIDs.add(6);
        nodeIDs.add(7);
        nodeIDs.add(8);
        nodeIDs.add(9);

        Way way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);
        graph.stop();
        graph.run();
        return graph;
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetPathOneWay() {
        Graph graph = getGraph1();
        Navigation navigation = new Navigation(graph);
        DrawableWay path = navigation.getPath(0, 9);
        assertNotNull(path);
    }

    @Test
    void testGetPathTwoWays() {
        Graph graph = getGraph1();

        ArrayList tags = new ArrayList();
        tags.add("navigationPath");

        CoordArrayList coords = new CoordArrayList();
        coords.add(1f);
        coords.add(2f);
        coords.add(3f);
        coords.add(4f);

        LongArrayList nodeIDs = new LongArrayList();
        nodeIDs.add(10);
        nodeIDs.add(1);

        Way secondWay = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(secondWay);
        graph.run();
        Navigation navigation = new Navigation(graph);
        DrawableWay path = navigation.getPath(0, 10);
        assertNotNull(path);

        ArrayList<String>
        FloatArrayList expectedCoords = new FloatArrayList();
        expectedCoords.add(1f);
        expectedCoords.add(2f);
        expectedCoords.add(1f);
        expectedCoords.add(1f);
        expectedCoords.add(0f);
        expectedCoords.add(0f);


        LongArrayList pathIDs = new LongArrayList();
        pathIDs.add(10);
        pathIDs.add(1);
        pathIDs.add(0);

        Way expectedPath = new Way(1l, expectedCoords, new float[]{}, new String[]{"navigationPath", "navigationPath"}, pathIDs1);

        assertEquals(expectedPath, path);
    }

    @Test
    void testGetPathNoPath() {
        Graph graph = getGraph1();
        Way secondWay = new Way(new float[]{1f,2f,3f,4f}, new float[]{}, new String[]{""}, new long[]{10,11});
        graph.addWay(secondWay);
        graph.run();
        Navigation navigation = new Navigation(graph);
        Way path = navigation.getPath(1, 11);
        assertNull(path);
    }
*/
}