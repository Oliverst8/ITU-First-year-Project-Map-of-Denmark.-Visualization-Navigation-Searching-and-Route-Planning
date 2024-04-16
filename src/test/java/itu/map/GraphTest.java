package itu.map;

import dk.itu.map.structures.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GraphTest {

    @Test
    void testEqualsNotEqualsDifferentObject() {
        Graph graph = new Graph();
        Object obj = new Object();
        assertNotEquals(graph, obj);
    }

}
