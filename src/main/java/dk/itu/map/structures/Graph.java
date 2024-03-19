package dk.itu.map.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Graph implements Runnable{


    private List<Way> ways;
    private final HashMap<Long, IntArrayList> idToIndex;
    private final FloatArrayList edges;
    private final LongArrayList ids;

    public Graph() {
        edges = new FloatArrayList();
        ways = Collections.synchronizedList(new ArrayList<>());
        idToIndex = new HashMap<>();
        ids = new LongArrayList(); 
    }

    private float calcWeight(float ux, float uy, float vx, float vy) {
        float dist = (float) Math.sqrt(Math.pow(ux - vx, 2) + Math.pow(uy - vy, 2));
        return dist;
    }

    public void run() {
        while(true){
            if(ways.size() > 100_000){
                Way way = ways.remove(0);
                float[] coords = way.getCoords();
                float weight = calcWeight(coords[0], coords[1], coords[coords.length-2], coords[coords.length-1]);
                addEdge(coords[0], coords[1], coords[coords.length-2], coords[coords.length-1], weight, way.getId());
            }
        }
    }

    private void addVertix(){

    }

    private void addEdge(float ux, float uy, float vx, float vy, float weight, long id) {
        idToIndex.putIfAbsent(id, new IntArrayList());
        IntArrayList list = idToIndex.get(id);


    }

    public void addWay(Way way) {
        ways.add(way);
    }

}
