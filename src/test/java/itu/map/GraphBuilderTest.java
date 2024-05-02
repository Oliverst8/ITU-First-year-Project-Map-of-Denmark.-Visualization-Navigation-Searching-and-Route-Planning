package itu.map;

import dk.itu.map.parser.GraphBuilder;
import dk.itu.map.structures.ArrayLists.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dk.itu.map.parser.Way;
import org.apache.commons.io.FileUtils;
import dk.itu.map.structures.Graph;


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
    private GraphBuilder getGraph1() {
        GraphBuilder graph = new GraphBuilder();
        ArrayList<String> tags;
        CoordArrayList coords;
        LongArrayList nodeIDs;
        Way way;

        //redStriped
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("escape");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 1f, 1f, 2f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{1,2});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //redStriped
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("raceway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 2f, 1f, 3f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{2,3});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        //redStriped
        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("busway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 3f, 1f, 4f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{3,4});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("busguideway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 4f, 1f, 5f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{4,5});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("proposed");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 5f, 1f, 6f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{5,6});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("construction");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 6f, 1f, 7f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{6,7});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("service");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 7f, 1f, 8f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{7,8});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("pedestrian");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 8f, 1f, 9f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{8,9});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("footway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 9f, 1f, 10f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{9,10});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("steps");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 10f, 1f, 11f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{10,11});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("corridor");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 11f, 1f, 12f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{11,12});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("path");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 12f, 1f, 13f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{12,13});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("cycleway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 13f, 1f, 14f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{13,14});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("bridleway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 14f, 1f, 15f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{14,15});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("track");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 15f, 1f, 16f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{15,16});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("road");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 16f, 1f, 17f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{16,17});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("unclassified");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 17f, 1f, 18f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{17,18});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("residential");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 18f, 1f, 19f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{18,19});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("living_street");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 19f, 1f, 20f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{19,20});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("tertiary");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 20f, 1f, 21f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{20,21});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("secondary");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 21f, 1f, 22f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{21,22});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 22f, 1f, 23f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{22,23});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("trunk");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 23f, 1f, 24f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{23,24});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 24f, 1f, 25f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{24,25});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("motorway_link");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 25f, 1f, 26f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{25,26});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("trunk_link");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 26f, 1f, 27f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{26,27});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary_link");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 27f, 1f, 28f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{27,28});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("secondary_link");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 28f, 1f, 29f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{28,29});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        tags = new ArrayList<>();
        tags.add("highway");
        tags.add("tertiary_link");
        coords = TestUtilities.createCoordArrayList(new float[]{1f, 29f, 1f, 30f});
        nodeIDs = TestUtilities.createLongArrayList(new long[]{29,30});
        way = new Way(1l, tags, coords, nodeIDs);
        graph.addWay(way);

        graph.stop();
        graph.run();
        return graph;
    }


    @Test
    void testAddEdges() throws InterruptedException {
        GraphBuilder graph = new GraphBuilder();
        CoordArrayList nodes1 = TestUtilities.createCoordArrayList(0f,0f,1f,1f);
        List<String> tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary");
        tags.add("maxspeed");
        tags.add("50");

        CoordArrayList nodes2 = TestUtilities.createCoordArrayList(1f,1f,3f,1f);
        LongArrayList nodeIDs1 = new LongArrayList();
        nodeIDs1.add(1);
        nodeIDs1.add(2);
        LongArrayList nodeIDs2 = new LongArrayList();
        nodeIDs2.add(2);
        nodeIDs2.add(3);
        Way way1 = new Way(0, tags, nodes1, nodeIDs1);
        Way way2 = new Way(0, tags, nodes2, nodeIDs2);

        graph.addWay(way1);
        graph.addWay(way2);
        Thread thread = new Thread(graph);
        thread.start();
        Thread.sleep(10);
        graph.stop();
        thread.join();
        FloatArrayList expectedDistanceWeights = new FloatArrayList();
        expectedDistanceWeights.add((float) Math.sqrt((111.320*0.56)*(111.320*0.56)+110.574*110.574));
        expectedDistanceWeights.add((float) Math.sqrt((111.320*0.56)*(111.320*0.56)+110.574*110.574));
        expectedDistanceWeights.add(111.320f*0.56f*2);
        expectedDistanceWeights.add(111.320f*0.56f*2);

        FloatArrayList expectedTimeWeights = new FloatArrayList();
        expectedTimeWeights.add((float) Math.sqrt((111.320*0.56)*(111.320*0.56)+110.574*110.574)/50);
        expectedTimeWeights.add((float) Math.sqrt((111.320*0.56)*(111.320*0.56)+110.574*110.574)/50);
        expectedTimeWeights.add(111.320f*0.56f*2/50);
        expectedTimeWeights.add(111.320f*0.56f*2/50);

        IntArrayList expectedDestinations = new IntArrayList();

        expectedDestinations.add(1);
        expectedDestinations.add(0);
        expectedDestinations.add(2);
        expectedDestinations.add(1);


        IntArrayList actualDestinations = graph.getEdges();
        FloatArrayList actualDistanceWeights = graph.getDistanceWeights();
        FloatArrayList actualTimeWeights = graph.getTimeWeights();

        for(int i = 0; i < expectedDistanceWeights.size(); i++){
            assertEquals(expectedDistanceWeights.get(i), actualDistanceWeights.get(i));
        }
        for(int i = 0; i < expectedTimeWeights.size(); i++){
            assertEquals(expectedTimeWeights.get(i), actualTimeWeights.get(i));
        }
        for(int i = 0; i < expectedDestinations.size(); i++){
            assertEquals(expectedDestinations.get(i), actualDestinations.get(i));
        }
        assertEquals(expectedDistanceWeights.size(), actualDistanceWeights.size());
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
        //assertEquals(graph, graphBuilder);
        assertTrue(graph.equals(graphBuilder));
    }

    public GraphBuilder getGraph() throws InterruptedException {
        GraphBuilder graph = new GraphBuilder();
        CoordArrayList nodes1 = TestUtilities.createCoordArrayList(0f,0f,1f,1f);
        List<String> tags = new ArrayList<>();
        tags.add("highway");
        tags.add("primary");
        CoordArrayList nodes2 = TestUtilities.createCoordArrayList(1f,1f,3f,1f);
        LongArrayList nodeIDs1 = new LongArrayList();
        nodeIDs1.add(1);
        nodeIDs1.add(2);
        LongArrayList nodeIDs2 = new LongArrayList();
        nodeIDs2.add(2);
        nodeIDs2.add(3);
        Way way1 = new Way(0, tags, nodes1, nodeIDs1);
        Way way2 = new Way(0, tags, nodes2, nodeIDs2);

        graph.addWay(way1);
        graph.addWay(way2);
        Thread thread = new Thread(graph);
        thread.start();
        Thread.sleep(10);
        graph.stop();
        thread.join();
        return graph;
    }

    @Test
    void testSetVehicleRestrictions() {
        Graph graph = getGraph1();
        ByteArrayList expected = new ByteArrayList();
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 0);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 1);
        expected.add((byte) 3);
        expected.add((byte) 3);
        expected.add((byte) 3);
        expected.add((byte) 3);
        expected.add((byte) 3);
        expected.add((byte) 3);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 4);
        expected.add((byte) 4);
        expected.add((byte) 4);
        expected.add((byte) 4);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);
        expected.add((byte) 7);

        ByteArrayList actual = graph.getVehicleRestrictions();

        for(int i = 0; i < actual.size(); i++){
            System.out.print(actual.get(i) + " ");
        }
        System.out.println();
        for(int i = 0; i < expected.size(); i++){
            System.out.print(expected.get(i) + " ");
        }


        for(int i = 0; i < expected.size(); i++){
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
