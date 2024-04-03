package dk.itu.map.utility;

import dk.itu.map.structures.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Navigation {
    private final Graph graph;
    private ChangablePriorityQueue queue;
    private final int[] vertexTo;

    public Navigation(Graph graph){
        this.graph = graph;
        vertexTo = new int[graph.size()];
        Arrays.fill(vertexTo, -1);
    }

    private boolean buildPaths(int startPoint, int endPoint){
        queue = new ChangablePriorityQueue(graph);
        queue.decreaseValueTo(startPoint, 0);
        while(!queue.isEmpty()){
            int min = queue.deleteMinValue();
            if(min == endPoint) return true;
            relax(min);
        }
        return false;
    }

    private void relax(int vertex){
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int destination = graph.getDestination(edges.get(i));
            float newWeight = queue.getValue(vertex) + graph.getWeight(vertex);
            if(newWeight < queue.getValue(destination)){
                queue.decreaseValueTo(destination, newWeight);
                vertexTo[destination] = vertex;
            }
        }
    }

    public Way getPath(long startPoint, long endPoint){

        int startPointID = graph.idToVertexId(startPoint);
        int endPointID = graph.idToVertexId(endPoint);

        if(!buildPaths(startPointID, endPointID)) return null;

        FloatArrayList path = new FloatArrayList();
        LongArrayList pathIDs = new LongArrayList();
        int current = endPointID;
        while(current != startPointID){
            float[] coords = graph.getCoords(current);
            path.add(coords[0]);
            path.add(coords[1]);
            pathIDs.add(current);
            current = vertexTo[current];
        }

        return new Way(path.toArray(), new float[]{}, new String[]{"navigationPath", "navigationPath"}, pathIDs.toArray());


        //return path;

    }



}
