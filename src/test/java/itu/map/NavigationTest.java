package itu.map;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.LongArrayList;
import dk.itu.map.structures.Way;
import dk.itu.map.utility.Navigation;
import dk.itu.map.structures.FloatArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

    private Graph getGraph1(){
        Graph graph = new Graph();

        Way way = new Way(new float[]{0f,0f,1f,1f,2f,2f,3f,3f,4f,4f,5f,5f,6f,6f,7f,7f,8f,8f,9f,9f}, new float[]{}, new String[]{""}, new long[]{0,1,2,3,4,5,6,7,8,9});
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
        Way path = navigation.getPath(0, 9);
        assertNotNull(path);
    }

    @Test
    void testGetPathTwoWays() {
        Graph graph = getGraph1();
        Way secondWay = new Way(new float[]{1f,2f,3f,4f}, new float[]{}, new String[]{""}, new long[]{10,1});
        graph.addWay(secondWay);
        graph.run();
        Navigation navigation = new Navigation(graph);
        Way path = navigation.getPath(0, 10);
        assertNotNull(path);
        FloatArrayList expectedCoords = new FloatArrayList();
        expectedCoords.add(0f);
        expectedCoords.add(0f);
        expectedCoords.add(1f);
        expectedCoords.add(1f);
        expectedCoords.add(1f);
        expectedCoords.add(2f);

        LongArrayList pathIDs = new LongArrayList();
        pathIDs.add(10);
        pathIDs.add(1);
        pathIDs.add(0);

        Way expectedPath = new Way(expectedCoords.toArray(), new float[]{}, new String[]{"navigationPath", "navigationPath"}, pathIDs.toArray());

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

}