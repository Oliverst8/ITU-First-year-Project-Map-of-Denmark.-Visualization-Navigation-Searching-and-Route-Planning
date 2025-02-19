package dk.itu.map.utility;

import dk.itu.map.parser.GraphBuilder;
import dk.itu.map.structures.Graph;
import dk.itu.map.structures.IndexMinPQ;
import dk.itu.map.structures.Point;
import dk.itu.map.structures.Drawable;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Navigation {
    private final Graph graph;
    //private ChangeablePriorityQueue queue;
    private IndexMinPQ<Float> queue;
    private final int[] vertexTo;
    private final float[] distTo;
    private final float[] timeTo;
    //This is 4 for car, 2 for bike and 1 for walk.
    private final int vehicleCode;

    public final Set<Drawable> navigatedPoints;

    /**
     * Create an instance of a Navigation
     * @param graph the graph to navigate
     * @param vehicleCode the vehicle code to be used in the navigation
     */
    public Navigation(Graph graph, int vehicleCode) {
        this.graph = graph;
        this.vehicleCode = vehicleCode;
        vertexTo = new int[graph.size()];
        distTo = new float[graph.size()];
        timeTo = new float[graph.size()];
        navigatedPoints = new HashSet<>();

        for(int i = 0; i < graph.size(); i++) {
            distTo[i] = Float.MAX_VALUE;
            timeTo[i] = Float.MAX_VALUE;
            vertexTo[i] = -1;
        }
    }

    /**
     * Build the paths
     * @param startPoint the start point
     * @param endPoint the end point
     * @return true if a path is found, false otherwise
     */
    private boolean buildPaths(int startPoint, int endPoint) {
        queue = new IndexMinPQ<>(graph.size());
        queue.insert(startPoint, 0f);
        setDistTo(startPoint, startPoint, 0f, 0f);

        while (!queue.isEmpty()) {
            int min = queue.delMin();
            navigatedPoints.add(new Point(graph.getCoords(min)[1], graph.getCoords(min)[0], "navigationpoint", Color.PURPLE));
            if (min == endPoint) {
                return true;
            }
            relax(min, endPoint);
        }

        return false;
    }

    /**
     * Relax the vertex
     * @param vertex the vertex to relax
     */
    private void relax(int vertex, int endPoint) {
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int edge = edges.get(i);
            if((graph.getVehicleRestrictions().get(edge) & vehicleCode) == 0 ){
                continue;
            }

            int destination = graph.getDestination(edge);

            float newDistWeight = distTo[vertex] + graph.getDistanceWeight(edge) + findHeuristicDistance(vertex, graph.getCoords(endPoint));
            // float newDistWeight = distTo[vertex] + graph.getDistanceWeight(edge);

            if((newDistWeight < distTo[destination]) && (vehicleCode == 2 || vehicleCode == 1)){
                if(queue.contains(destination)) queue.decreaseKey(destination, newDistWeight);
                else queue.insert(destination, newDistWeight);
                setDistTo(destination, vertex, newDistWeight);
            }

            if(vehicleCode == 4){
                float newTimeWeight = timeTo[vertex] + graph.getTimeWeight(edge) + findHeuristicTime(vertex, graph.getCoords(endPoint));
                // float newTimeWeight = timeTo[vertex] + graph.getTimeWeight(edge);

                if((newTimeWeight < timeTo[destination])){
                    if(queue.contains(destination)) queue.decreaseKey(destination, newTimeWeight);
                    else queue.insert(destination, newTimeWeight);
                    setDistTo(destination, vertex, newDistWeight, newTimeWeight);
                }
            }
        }
    }

    /**
     * Set the distance to the vertex
     * @param vertex the vertex to set the distance to
     * @param vertexFrom the vertex to set the distance from
     * @param dist the distance
     * @param time the time
     */
    private void setDistTo(int vertex, int vertexFrom, float dist, float time) {
        distTo[vertex] = dist;
        timeTo[vertex] = time;
        vertexTo[vertex] = vertexFrom;
    }

    /**
     * Set the distance to the vertex
     * @param vertex the vertex to set the distance to
     * @param vertexFrom the vertex to set the distance from
     * @param dist the distance
     */
    private void setDistTo(int vertex, int vertexFrom, float dist) {
        distTo[vertex] = dist;
        vertexTo[vertex] = vertexFrom;
    }

    /**
     * Get the path between two coordinates
     * @param startCoords the start coordinates
     * @param endCoords the end coordinates
     * @return the path
     */
    public DrawableWay[] getPath(float[] startCoords, float[] endCoords) {
        navigatedPoints.clear();

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
        paths[0] = new DrawableWay(path, -1, "navigation", "path");

        CoordArrayList startPath = new CoordArrayList();
        startPath.add(startCoords);
        System.out.println("StartCoords: "+Arrays.toString(startCoords));
        startPath.add(graph.getCoords(nearestStartPointID));
        System.out.println("NearestStartPoint: " + Arrays.toString(graph.getCoords(nearestStartPointID)));
        paths[1] = new DrawableWay(startPath, -2, "navigation", "pathToRoad");

        CoordArrayList endPath = new CoordArrayList();
        endPath.add(endCoords);
        System.out.println("EndCoords: "+ Arrays.toString(endCoords));
        endPath.add(graph.getCoords(nearestEndPointID));
        System.out.println("NearestEndPoint: " + Arrays.toString(graph.getCoords(nearestEndPointID)));
        paths[2] = new DrawableWay(endPath, -3, "navigation", "pathToRoad");

        return paths;
    }

    private float findHeuristicDistance(int vertex, float[] endCoords){
        float[] startCoords = graph.getCoords(vertex);
        float distance = GraphBuilder.distanceInKM(new float[]{startCoords[0], startCoords[1]}, new float[]{endCoords[0], endCoords[1]});
        return distance;
    }

    private float findHeuristicTime(int vertex, float[] endCoords){
        float[] startCoords = graph.getCoords(vertex);
        float distance = GraphBuilder.distanceInKM(new float[]{startCoords[0], startCoords[1]}, new float[]{endCoords[0], endCoords[1]});
        return distance/130;
    }

    public Set<Drawable> getNavigatedPoints() {
        return navigatedPoints;
    }
}
