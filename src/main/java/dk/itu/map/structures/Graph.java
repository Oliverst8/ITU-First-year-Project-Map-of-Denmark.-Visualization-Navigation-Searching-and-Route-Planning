package dk.itu.map.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Graph implements Runnable, Serializable {


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
    private float calcWeight(Way way, int firstNode) {
        float[] coords = way.getCoords();

        return (float) Math.sqrt(Math.pow(coords[firstNode] - coords[firstNode+2], 2) + Math.pow(coords[firstNode+1] - coords[firstNode+3], 2));
    }

    public void run() {
        while(running || !ways.isEmpty()){
            if(running && ways.size() < 100_000){
                try {
                    Thread.sleep(30);
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(!ways.isEmpty()){
                Way way = ways.remove(0);
                float[] coords = way.getCoords();
                addVertices(way.getNodeIDs(), way.getCoords());
                addEdge(way);
            }
        }
    }

    private void addVertices(long[] vertexID, float[] coords) {
        for (int i = 0; i < vertexID.length; i++) {
            if(!idToIndex.containsKey(vertexID[i])){
                int index = vertexList.size();
                idToIndex.put(vertexID[i], index);
                vertexList.add(new IntArrayList(2));
                this.coords.add(coords[i*2]);
                this.coords.add(coords[i*2+1]);
                //coords.add();
                //Here we should add coords, but I dont know how to get them currently, as I should either give this method a way,
                // or look at the LongFloatArrayHashMap in FileHandler, or just give them as arguments

                //Here we could add node ids to an nodeIDArray, if we want them later
            }
        }
    }

    private void addEdge(Way way) {
        /*
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
        */

        //This is the new version of the above code, which should be more efficient
        long[] nodeIDs = way.getNodeIDs();

        for(int i = 0; i < nodeIDs.length-1; i++){
            int node1 = idToIndex.get(nodeIDs[i]);
            int node2 = idToIndex.get(nodeIDs[(i+1)]);

            int edgeNumberFrom1 = edgeDestinations.size();
            int edgeNumberFrom2 = edgeNumberFrom1+1;

            vertexList.get(node1).add(edgeNumberFrom1);
            vertexList.get(node2).add(edgeNumberFrom2);

            edgeDestinations.add(node2);
            edgeDestinations.add(node1);


            float weight = calcWeight(way, i);

            edgeWeights.add(weight);
            edgeWeights.add(weight);

        }

       //Maybe for all these we should add at the specific index to make sure no mistakes are made,
        // but as long as we just call these methods here, we should be okay I think
    }

    public int size(){
        return idToIndex.size();
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
    public IntArrayList getEdgeList(int vertex){return vertexList.get(vertex);}

    public int getDestination(int edge){
        return edgeDestinations.get(edge);
    }
    public float getWeight(int edge){
        return edgeWeights.get(edge);
    }

    public FloatArrayList getWeights() {
        return edgeWeights;
    }

    public float[] getCoords(int index){
        return new float[]{coords.get(index*2), coords.get(index*2+1)};
    }

    public int idToVertexId(long id){
        return idToIndex.get(id);
    }
}
