package dk.itu.map.utility;

import dk.itu.map.structures.Graph;
import dk.itu.map.structures.IndexMinPQ;
import dk.itu.map.structures.DrawableWay;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.ArrayLists.LongArrayList;

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
        //queue = new ChangeablePriorityQueue(graph);
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
        System.out.println("Vehiclecode is: " + vehicleCode);
        IntArrayList edges = graph.getEdgeList(vertex);
        for(int i = 0; i < edges.size(); i++){
            int edge = edges.get(i);
            if((graph.getVehicleRestrictions().get(edge) & vehicleCode) == 0 ){
                System.out.println("VehicleRestriction: " + graph.getVehicleRestrictions().get(edge));
                continue;
            }

            int destination = graph.getDestination(edge);

            float newDistWeight = distTo[vertex] + graph.getDistanceWeight(edge);
            if((newDistWeight < distTo[destination]) && (vehicleCode == 2 || vehicleCode == 1)){
                System.out.println("We are in the distWeight");
                if(queue.contains(destination)) queue.decreaseKey(destination, newDistWeight);
                else queue.insert(destination, newDistWeight);
                setDistTo(destination, vertex, newDistWeight);
            }

            if(vehicleCode == 4){
                float newTimeWeight = timeTo[vertex] + graph.getTimeWeight(edge);
                if(graph.getTimeWeight(edge) <= 0){
                    System.out.println("TimeWeight is: "+ graph.getTimeWeight(edge));
                }

                if((newTimeWeight < timeTo[destination])){
                    System.out.println("We are in the timeWeight");
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

    private DrawableWay getPath(int startPointID, int endPointID){

        if(!buildPaths(startPointID, endPointID)) return null;

        CoordArrayList path = new CoordArrayList();
        LongArrayList pathIDs = new LongArrayList();
        int current = endPointID;
        while(current != startPointID){
            path.add(graph.getCoords(current));
            pathIDs.add(current);
            current = vertexTo[current];
            //System.out.println(path.size());
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
        //int startPoint = graph.getNearestNeighborID(startCoords);
        int startPoint = graph.getNearestNeigherborID(new float[]{startCoords[1],startCoords[0]}, vehicleCode, graph);
        //int endPoint = graph.getNearestNeighborID(endCoords);
        int endPoint = graph.getNearestNeigherborID(new float[]{endCoords[1],endCoords[0]}, vehicleCode, graph);
        return getPath(startPoint, endPoint);

    }
}
