package itu.map;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Way;
import dk.itu.map.utility.Navigation;
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
    void getPath() {
        Graph graph = getGraph1();
        Navigation navigation = new Navigation(graph);
        Way path = navigation.getPath(0, 9);
        assertNotNull(path);
    }
}