package dk.itu.map.utility;

import dk.itu.map.structures.ChangablePriorityQueue;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.IntArrayList;

import java.util.Stack;

public class Navigation {
    private final Graph graph;
    private ChangablePriorityQueue queue;
    private int[] vertexTo;

    public Navigation(Graph graph){
        this.graph = graph;
        vertexTo = new int[graph.getIds().size()];
    }

    public Stack<Long> navigate(int startPoint, int endPoint){
        queue = new ChangablePriorityQueue(graph);
        queue.decreaseValueTo(startPoint, 0);
        for(int min = queue.deleteMinValue(); queue.getValue(min) < Float.MAX_VALUE; min = queue.deleteMinValue()){
            relax(min);
        }
        throw new UnsupportedOperationException();
    }

    private void relax(int vertex){
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int destination = edges.get(i);
            float newWeight = queue.getValue(vertex) + graph.getWeight(vertex);
            if(newWeight < queue.getValue(destination)){
                queue.decreaseValueTo(destination, newWeight);
                vertexTo[destination] = vertex;
            }
        }
    }



}
