package dk.itu.map.utility;

import dk.itu.map.structures.*;

import java.util.Stack;

public class Navigation {
    private final Graph graph;
    private ChangablePriorityQueue queue;
    private int[] vertexTo;

    public Navigation(Graph graph){
        this.graph = graph;
        vertexTo = new int[graph.getIds().size()];
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

    private boolean buildPaths(int startPoint, int endPoint){
        queue = new ChangablePriorityQueue(graph);
        queue.decreaseValueTo(startPoint, 0);
        for(int min = queue.deleteMinValue(); queue.getValue(min) < Float.MAX_VALUE; min = queue.deleteMinValue()){
            if(min == endPoint) return true;
            relax(min);
        }
        return false;
    }

    public Way getPath(int startPoint, int endPoint){
        if(!buildPaths(startPoint, endPoint)) return null;

        FloatArrayList path = new FloatArrayList();
        int current = endPoint;
        while(current != startPoint){
            float[] coords = graph.getCoords(current);
            path.add(coords[0]);
            path.add(coords[1]);
            current = vertexTo[current];
        }

        return new Way(path.toArray(), new float[]{}, new String[]{"navigationPath", "navigationPath"});


        //return path;

    }

}
