package dk.itu.map.utility;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.IndexMinPQ;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;
import dk.itu.map.structures.Point;

import java.util.Arrays;

public class Navigation {
    private final Graph graph;
    //private ChangeablePriorityQueue queue;
    private IndexMinPQ<Float> queue;
    private final int[] vertexTo;
    private final float[] distTo;
    private final float[] timeTo;
    //This is 4 for car, 2 for bike and 1 for walk.
    private final int vehicleCode;

    public Navigation(Graph graph, int vehicleCode) {
        this.graph = graph;
        this.vehicleCode = vehicleCode;
        vertexTo = new int[graph.size()];
        distTo = new float[graph.size()];
        timeTo = new float[graph.size()];

        for(int i = 0; i < graph.size(); i++) {
            distTo[i] = Float.MAX_VALUE;
            timeTo[i] = Float.MAX_VALUE;
            vertexTo[i] = -1;
        }
    }

    private boolean buildPaths(int startPoint, int endPoint) {
        queue = new IndexMinPQ<>(graph.size());
        queue.insert(startPoint, 0f);
        setDistTo(startPoint, startPoint, 0f, 0f);
        while(!queue.isEmpty()){
            int min = queue.delMin();

            if(min == endPoint) return true;
            relax(min);
        }
        return false;
    }

    private void relax(int vertex) {
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int edge = edges.get(i);
            if((graph.getVehicleRestrictions().get(edge) & vehicleCode) == 0 ){
                continue;
            }

            int destination = graph.getDestination(edge);

            float newDistWeight = distTo[vertex] + graph.getDistanceWeight(edge);
            if((newDistWeight < distTo[destination]) && (vehicleCode == 2 || vehicleCode == 1)){
                if(queue.contains(destination)) queue.decreaseKey(destination, newDistWeight);
                else queue.insert(destination, newDistWeight);
                setDistTo(destination, vertex, newDistWeight);
            }

            if(vehicleCode == 4){
                float newTimeWeight = timeTo[vertex] + graph.getTimeWeight(edge);

                if((newTimeWeight < timeTo[destination])){
                    if(queue.contains(destination)) queue.decreaseKey(destination, newTimeWeight);
                    else queue.insert(destination, newTimeWeight);
                    setDistTo(destination, vertex, newDistWeight, newTimeWeight);
                }
            }
        }
    }

    private void setDistTo(int vertex, int vertexFrom, float dist, float time) {
        distTo[vertex] = dist;
        timeTo[vertex] = time;
        vertexTo[vertex] = vertexFrom;
    }

    private void setDistTo(int vertex, int vertexFrom, float dist) {
        distTo[vertex] = dist;
        vertexTo[vertex] = vertexFrom;
    }

    public DrawableWay[] getPath(float[] startCoords, float[] endCoords){

        int nearestStartPointID = graph.getNearestNeigherborID(new float[]{startCoords[0],startCoords[1]}, vehicleCode, graph);
        int nearestEndPointID = graph.getNearestNeigherborID(new float[]{endCoords[0],endCoords[1]}, vehicleCode, graph);

        if(!buildPaths(nearestStartPointID, nearestEndPointID)) return null;

        DrawableWay[] paths = new DrawableWay[3];

        CoordArrayList path = new CoordArrayList();
        int current = nearestEndPointID;
        while(current != nearestStartPointID){
            path.add(graph.getCoords(current));
            current = vertexTo[current];
        }
        path.add(graph.getCoords(current));
        paths[0] = new DrawableWay(path, new String[]{"navigationPath", "navigationPath"}, -1, "navigation", "path");

        CoordArrayList startPath = new CoordArrayList();
        startPath.add(startCoords);
        System.out.println("StartCoords: " + Arrays.toString(startCoords));
        startPath.add(graph.getCoords(nearestStartPointID));
        System.out.println("NearestStartPoint: " + Arrays.toString(graph.getCoords(nearestStartPointID)));
        paths[1] = new DrawableWay(startPath, new String[]{"pathToRoad", "pathToRoad"}, -2, "navigation", "pathToRoad");

        CoordArrayList endPath = new CoordArrayList();
        endPath.add(endCoords);
        System.out.println("EndCoords: "+ Arrays.toString(endCoords));
        endPath.add(graph.getCoords(nearestEndPointID));
        System.out.println("NearestEndPoint: " + Arrays.toString(graph.getCoords(nearestEndPointID)));
        paths[2] = new DrawableWay(endPath, new String[]{"pathToRoad", "pathToRoad"}, -3, "navigation", "pathToRoad");

        return paths;

    }
}
