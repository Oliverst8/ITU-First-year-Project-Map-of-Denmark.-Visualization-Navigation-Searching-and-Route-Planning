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
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NavigationTest {

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

    private GraphBuilder getGraph2(){
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary");

        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //redStriped
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 100f, 8f, 7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,11, 12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStriped
        coords = TestUtilities.createCoordArrayList(new float[]{2f,4f, 4f,5f, 5f,6f, 8f,7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{13,14,15,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStiped
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f, 2f, 4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1, 13});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        graph.stop();
        graph.run();
        return graph;
    }

    private GraphBuilder getGraph3() {
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("highway");
        tags.add("footway");

        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //redStriped
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 100f, 8f, 7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,11, 12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStriped
        coords = TestUtilities.createCoordArrayList(new float[]{2f,4f, 4f,5f, 5f,6f, 8f,7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{13,14,15,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStiped
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f, 2f, 4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1, 13});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        graph.stop();
        graph.run();
        return graph;
    }

    private GraphBuilder getGraphSomeFootPathSomeMotorway() {
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags = new ArrayList<>();
        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //redStriped
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 100f, 8f, 7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,11, 12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blueStriped
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway");
        coords = TestUtilities.createCoordArrayList(new float[]{2f,4f, 4f,5f, 5f,6f, 8f,7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{13,14,15,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //green
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("footway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f, 2f, 4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1, 13});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        graph.stop();
        graph.run();
        return graph;
    }
    private GraphBuilder getGraphSameDistanceDifferentSpeed() {
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags = new ArrayList<>();
        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //red
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("living_street");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 100f,});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,3});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //blue
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f,1f, 1f,50f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,2});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //green
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f,50f, 1f, 100f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{2,3});
        way = new Way(1l, tags, coords, nodeIDs);
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
    void testTheresNoPath() {
        Graph graph = getGraph1();
        Navigation navigation = new Navigation(graph, 1);
        DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{1f,3f});
        assertNull(paths);
    }


        @Test
        void testGetPathOnOneWay() {
            Graph graph = getGraph1();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,2f},new float[]{1f,4f});
            float[] expected = new float[]{1f, 4f, 1f, 3f, 1f,2f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }

        @Test
        void testGetPathWithTwoWays() {
            Graph graph = getGraph1();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{4f,4f});
            float[] expected = new float[]{4f, 4f, 3f, 3f, 4f, 2f, 1f, 1f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }

        @Test
        void testGetFasterPath(){
            Graph graph = getGraph1();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{4f,2f});
            float[] expected = new float[]{4f,2f,1f,1f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }

        @Test
        void testGetFasterPathWithMoreNodes(){
            Graph graph = getGraph1();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{8f,7f});
            float[] expected = new float[]{8f,7f,5f,6f,4f,5f,2f,4f,1f,1f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }

        @Test
        void testGetFasterPathWithMoreNodes2(){
            Graph graph = getGraph2();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{8f,7f});
            float[] expected = new float[]{8f,7f,5f,6f,4f,5f,2f,4f,1f,1f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }

        @Test
        void testShortestWayIsVehicleRestrictedByFoot(){
            Graph graph = getGraphSomeFootPathSomeMotorway();
            Navigation navigation = new Navigation(graph, 1);
            DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{2f,4f});

            float[] expected = new float[]{2f,4f,1f,1f};
            assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
        }
    @Test
    void testShortestWayIsVehicleRestrictedByCar(){
        Graph graph = getGraphSomeFootPathSomeMotorway();
        Navigation navigation = new Navigation(graph, 4);
        DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{2f,4f});

        float[] expected = new float[]{2f,4f,4f,5f,5f,6f,8f,7f,1f,100f,1f,1f};
        assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
    }

    @Test
    void testGraphSameDistanceDifferentSpeed(){
        Graph graph = getGraphSameDistanceDifferentSpeed();
        Navigation navigation = new Navigation(graph, 4);
        DrawableWay[] paths = navigation.getPath(new float[]{1f,1f}, new float[]{1f,100f});

        float[] expected = new float[]{1f,100f,1f,50f,1f,1f};
        assertArrayEquals(expected, paths[0].getOuterCoords().toArray());
    }

}