package dk.itu.map.utility;

import dk.itu.map.structures.*;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.FloatArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

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
            int edge = edges.get(i);
            int destination = graph.getDestination(edge);
            float newWeight = distTo[vertex] + graph.getWeight(edge);
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

    private DrawableWay getPath(int startPointID, int endPointID){



        if(!buildPaths(startPointID, endPointID)) return null;

        CoordArrayList path = new CoordArrayList();
        LongArrayList pathIDs = new LongArrayList();
        int current = endPointID;
        while(current != startPointID){
            path.add(graph.getCoords(current));
            pathIDs.add(current);
            current = vertexTo[current];
        }

        path.add(graph.getCoords(current));
        pathIDs.add(current);

        return new DrawableWay(path, new String[]{"navigationPath", "navigationPath"}, pathIDs.toArray());

        //return path;

    }

    public DrawableWay getPath(long startID, long endID){
        int startPointID = graph.idToVertexId(startID);
        int endPointID = graph.idToVertexId(endID);
        return getPath(startPointID, endPointID);
    }

    public DrawableWay getPath(float[] startCoords, float[] endCoords){
        //int startPoint = graph.getNearestNeigherborID(startCoords);
        int startPoint = graph.getNearestNeigherborID(new float[]{startCoords[1],startCoords[0]});
        //int endPoint = graph.getNearestNeigherborID(endCoords);
        int endPoint = graph.getNearestNeigherborID(new float[]{endCoords[1],endCoords[0]});
        return getPath(startPoint, endPoint);

    }



}
