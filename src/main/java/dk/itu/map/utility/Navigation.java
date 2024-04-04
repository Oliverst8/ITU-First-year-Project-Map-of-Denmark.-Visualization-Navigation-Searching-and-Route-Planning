package dk.itu.map.utility;

import dk.itu.map.structures.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Navigation {
    private final Graph graph;
    //private ChangablePriorityQueue queue;
    private IndexMinPQ<Float> queue;
    private final int[] vertexTo;

    private final float[] distTo;

    public Navigation(Graph graph){
        this.graph = graph;
        vertexTo = new int[graph.size()];
        distTo = new float[graph.size()];

        for(int i = 0; i < graph.size(); i++) {
            distTo[i] = Float.MAX_VALUE;
            vertexTo[i] = -1;
        }

    }

    private boolean buildPaths(int startPoint, int endPoint){
        //queue = new ChangablePriorityQueue(graph);
        queue = new IndexMinPQ<>(graph.size());
        queue.insert(startPoint, 0f);
        setDistTo(startPoint, startPoint, 0f);
        while(!queue.isEmpty()){
            int min = queue.delMin();
            if(min == endPoint) return true;
            relax(min);
        }
        return false;
    }

    private void relax(int vertex){
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int destination = graph.getDestination(edges.get(i));
            float newWeight = distTo[vertex] + graph.getWeight(vertex);
            if(newWeight < distTo[destination]){
                if(queue.contains(destination)) queue.decreaseKey(destination, newWeight);
                else queue.insert(destination, newWeight);
                //vertexTo[destination] = vertex;
                setDistTo(destination, vertex, newWeight);
            }
        }
    }

    private void setDistTo(int vertex, int vertexFrom, float dist){
        distTo[vertex] = dist;
        vertexTo[vertex] = vertexFrom;
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

        float[] coords = graph.getCoords(current);
        path.add(coords[0]);
        path.add(coords[1]);
        pathIDs.add(current);

        return new Way(path.toArray(), new float[]{}, new String[]{"navigationPath", "navigationPath"}, pathIDs.toArray());


        //return path;

    }



}
