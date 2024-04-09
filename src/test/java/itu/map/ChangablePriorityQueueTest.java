package itu.map;

import dk.itu.map.structures.ChangablePriorityQueue;

class ChangablePriorityQueueTest {

    ChangablePriorityQueue priorityQueue;
/*
    private Graph getGraph1(){
        Graph graph = new Graph();

        Way way = new Way(new float[]{0f,0f,1f,1f,2f,2f,3f,3f,4f,4f,5f,5f,6f,6f,7f,7f,8f,8f,9f,9f}, new float[]{}, new String[]{""}, new long[]{0,1,2,3,4,5,6,7,8,9});
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.addWay(way);
        graph.stop();
        graph.run();
        return graph;
    }

    private Graph getGraph2(){
        Graph graph = new Graph();

        Way way = new Way(new float[]{0f,0f,1f,1f,2f,2f,3f,3f,4f,4f,5f,5f,6f,6f,7f,7f,8f,8f,9f,9f}, new float[]{}, new String[]{""}, new long[]{0,1,2,3,4,5,6,7,8,9});
        graph.addWay(way);
        graph.stop();
        graph.run();
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
        assertEquals(0, priorityQueue.deleteMinValue());
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
        priorityQueue.decreaseValueTo(0,3);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(2, 1);
        priorityQueue.decreaseValueTo(3,3);
        assertEquals(1, priorityQueue.deleteMinValue());
    }

    @Test
    void testSwim() {
        Graph graph = getGraph1();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(0,3);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(2, 1);
        priorityQueue.decreaseValueTo(3,4);
        priorityQueue.decreaseValueTo(4, 7);
        priorityQueue.decreaseValueTo(5, 6);
        priorityQueue.decreaseValueTo(6, 5);
        priorityQueue.decreaseValueTo(7, 3);
        priorityQueue.decreaseValueTo(8, 2);
        priorityQueue.decreaseValueTo(9, 0);
        assertEquals(9, priorityQueue.deleteMinValue());
    }

    @Test
    void testIsEmpty() {
        Graph graph = getGraph2();

        priorityQueue = new ChangablePriorityQueue(graph);

        priorityQueue.decreaseValueTo(0,3);
        priorityQueue.decreaseValueTo(1, 1);
        priorityQueue.decreaseValueTo(2, 1);
        priorityQueue.decreaseValueTo(3,4);
        priorityQueue.decreaseValueTo(4, 7);
        priorityQueue.decreaseValueTo(5, 6);
        priorityQueue.decreaseValueTo(6, 5);
        priorityQueue.decreaseValueTo(7, 3);
        priorityQueue.decreaseValueTo(8, 2);
        priorityQueue.decreaseValueTo(9, 0);

        int i = 0;
        while(!priorityQueue.isEmpty()){
            priorityQueue.deleteMinValue();
            i++;
        }
        assertEquals(10, i);
    }

    @Test
    void testDecreaseValueToIfIndexHasBeenMovedBySomethingElse() {
        Graph graph = new Graph();
        Way way = new Way(new float[]{0f,0f,1f,1f,2f,2f}, new float[]{}, new String[]{""}, new long[]{0,1,2});
        graph.addWay(way);
        graph.stop();
        graph.run();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(0,2);
        priorityQueue.deleteMinValue();
        priorityQueue.decreaseValueTo(2,0);
        assertEquals(2, priorityQueue.deleteMinValue());
    }

    @Test
    void testChangeValueAfterMove(){
        Graph graph = new Graph();
        Way way = new Way(new float[]{0f,0f,1f,1f,2f,2f}, new float[]{}, new String[]{""}, new long[]{0,1,2});
        graph.addWay(way);
        graph.stop();
        graph.run();
        priorityQueue = new ChangablePriorityQueue(graph);
        priorityQueue.decreaseValueTo(0,2);
        priorityQueue.deleteMinValue();
        priorityQueue.decreaseValueTo(2,0);
        priorityQueue.decreaseValueTo(0, -1);
        assertEquals(0, priorityQueue.deleteMinValue());
    }

*/

}