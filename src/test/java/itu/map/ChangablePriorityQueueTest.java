package itu.map;

import dk.itu.map.structures.ChangablePriorityQueue;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.Way;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChangablePriorityQueueTest {

    ChangablePriorityQueue priorityQueue;

    private Graph getGraph1(){
        Graph graph = new Graph();
        List<Float> nodes1 = Arrays.asList(0f,0f,1f,1f);
        List<String> stringList = new ArrayList<>();
        Way way = new Way(nodes1, stringList, new long[]{0, 1});
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        return graph;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void decreaseValueTo() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(0, 1);
        assertEquals(1, priorityQueue.getValue(0));
    }

    @Test
    void testDeleteMinValueOneValueExpects1() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(0, 1);
        assertEquals(1, priorityQueue.deleteMinValue());
    }

    @Test
    void testDeleteMinValueTwoValueExpects1() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(0, 2);
        assertEquals(1, priorityQueue.deleteMinValue());
    }

    @Test
    void testDeleteMinValueThreeValueExpects1() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(0, 2);
        priorityQueue.decreaseValueTo(2, 2);
        assertEquals(1, priorityQueue.deleteMinValue());
    }

    @Test
    void testDeleteMinValueTwoMathcingValueExpects1() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(0, 1);
        assertEquals(1, priorityQueue.deleteMinValue());
    }



}