package itu.map;

import dk.itu.map.parser.GraphBuilder;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.parser.Way;
import dk.itu.map.utility.Navigation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class    NavigationTest {

    private GraphBuilder getGraph1(){
        GraphBuilder graph = new GraphBuilder();
        ArrayList tags = new ArrayList();
        tags.add("navigationPath");

        CoordArrayList coords = TestUtilities.createCoordArrayList(new float[]{0f, 0f, 1f, 1f, 2f, 2f, 3f, 3f, 4f, 4f, 5f, 5f, 6f, 6f, 7f, 7f, 8f, 8f, 9f, 9f});

        LongArrayList nodeIDs = TestUtilities.createLongArrayList(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

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
        DrawableWay path = navigation.getPath(1, 10);
        assertNotNull(path);
    }
/*
    @Test
    void testGetPathTwoWays() {
        GraphBuilder graph = getGraph1();

        ArrayList<String> tags = new ArrayList<>();
        tags.add("navigationPath");
        CoordArrayList coords = TestUtilities.createCoordArrayList(1f, 2f, 3f, 4f);
        LongArrayList nodeIDs = TestUtilities.createLongArrayList(1, 2);

        Way secondWay = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(secondWay);
        graph.run();
        Navigation navigation = new Navigation(graph);
        DrawableWay path = navigation.getPath(1, 10);
        assertNotNull(path);

        ArrayList<String> tags1 = new ArrayList<>();

        CoordArrayList expectedCoords = TestUtilities.createCoordArrayList(1f,2f,1f,1f,0f,0f);

        LongArrayList pathIDs1 = TestUtilities.createLongArrayList(10, 2, 1);

        DrawableWay expectedPath = new DrawableWay();

        assertEquals(expectedPath, path);
    }
*/
    @Test
    void testGetPathNoPath() {
        /*
        Graph graph = getGraph1();
        Way secondWay = new Way(new float[]{1f,2f,3f,4f}, new float[]{}, new String[]{""}, new long[]{10,11});
        graph.addWay(secondWay);
        graph.run();
        Navigation navigation = new Navigation(graph);
        Way path = navigation.getPath(1, 11);
        assertNull(path);
        */
    }

}