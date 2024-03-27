package dk.itu.map.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Graph implements Runnable{


    private List<Way> ways;
    private final HashMap<Long, Integer> idToIndex;
    private final ArrayList<IntArrayList> vertexList;
    private final IntArrayList edgeDestinations;
    private final FloatArrayList edgeWeights;
    private final FloatArrayList coords;
    private final LongArrayList wayIDs;
    private boolean running = true;

    public Graph() {
        idToIndex = new HashMap<>();
        vertexList = new ArrayList<>();
        edgeDestinations = new IntArrayList();
        edgeWeights = new FloatArrayList();
        coords = new FloatArrayList();
        wayIDs = new LongArrayList();
        ways = Collections.synchronizedList(new ArrayList<>());
    }

    //Vi kender ikke enheden her, men det er måske givet i bredde- (eller længde-?) grader? 
    // Skal måske konverteres, men det er vel ligemeget egentlig, (indtil vi konvertere til tid?)
    private float calcWeight(Way way) {
        float[] coords = way.getCoords();
        float distSum = 0;
        for(int i = 0; i < way.getCoords().length-2; i +=2) {
            distSum += (float) Math.sqrt(Math.pow(coords[i] - coords[i+2], 2) + Math.pow(coords[i+1] - coords[i+3], 2));
        }
        return distSum;
    }

    public void run() {
        while(running){
            while(!ways.isEmpty()){
                Way way = ways.remove(0);
                float[] coords = way.getCoords();
                addVertices(way.getNodeIDs());
                addEdge(way);
            }
        }
    }

    private void addVertices(long[] vertexID){

        if(!idToIndex.containsKey(vertexID[0])){
            int index = vertexList.size();
            idToIndex.put(vertexID[0], index);
            vertexList.add(new IntArrayList());
            //coords.add();
            //Here we should add coords, but I dont know how to get them currently, as I should either give this method a way, 
            // or look at the LongFloatArrayHashMap in FileHandler, or just give them as arguments

            //Here we could add node ids to an nodeIDArray, if we want them later
        }
        if(!idToIndex.containsKey(vertexID[1])){
            idToIndex.put(vertexID[1], vertexList.size());
            vertexList.add(new IntArrayList());
            //coords.add();
            //Here we should add coords, but I dont know how to get them currently, as I should either give this method a way, 
            // or look at the LongFloatArrayHashMap in FileHandler, or just give them as arguments

            //Here we could add node ids to an nodeIDArray, if we want them later
        }
    }

    private void addEdge(Way way) {
        int node1 = idToIndex.get(way.getNodeIDs()[0]);
        int node2 = idToIndex.get(way.getNodeIDs()[1]);
        int edgeNumberFrom1 = edgeDestinations.size();
        int edgeNumberFrom2 = edgeNumberFrom1+1;
        //Weight should be differentiated later, but currently nothing can change it, so ill keep it in one variable
        float edgeWeight = calcWeight(way);

        vertexList.get(node1).add(edgeNumberFrom1);
        vertexList.get(node2).add(edgeNumberFrom2);

        edgeDestinations.add(node2);
        edgeDestinations.add(node1);

        edgeWeights.add(edgeWeight);
        edgeWeights.add(edgeWeight);

        wayIDs.add(way.getId());
        wayIDs.add(way.getId());

       //Maybe for all these we should add at the specific index to make sure no mistakes are made,
        // but as long as we just call these methods here, we should be okay I think
    }

    private float[] findShortestPath(){
        ChangablePriorityQueue CPQ = new ChangablePriorityQueue();

        float[] shortestPath = new float[1 ];
        return shortestPath;
    }


    public void addWay(Way way) {
        ways.add(way);
    }

    public void stop(){
        running = false;
    }
    //This test needs refactoring, since it is now split up in new Arrays
    public IntArrayList getEdges() {
        return edgeDestinations;
    }

    public LongArrayList getIds() {
        return wayIDs;
    }

    public FloatArrayList getWeights() {
        return edgeWeights;
    }
}
