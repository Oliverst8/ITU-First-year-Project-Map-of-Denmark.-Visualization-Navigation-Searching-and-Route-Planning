package dk.itu.map.structures;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class Graph implements Runnable {
    private List<Way> ways;
    private final HashMap<Long, IntArrayList> idToIndex;
    private final FloatArrayList edges;
    private final LongArrayList ids;
    private boolean running = true;

    public Graph() {
        edges = new FloatArrayList();
        ways = Collections.synchronizedList(new ArrayList<>());
        idToIndex = new HashMap<>();
        ids = new LongArrayList(); 
    }

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
                addVertix(way.getNodeIDs());
                addEdge(way);
            }
        }
    }

    private void addVertix(long[] vertexID){
        idToIndex.putIfAbsent(vertexID[0], new IntArrayList());
        idToIndex.putIfAbsent(vertexID[1], new IntArrayList());
    }

    private void addEdge(Way way) {
        idToIndex.get(way.getNodeIDs()[0]).add(ids.size());
        ids.add(way.getId());
        idToIndex.get(way.getNodeIDs()[1]).add(ids.size());
        ids.add(way.getId());

        float[] nodes = way.getCoords();

        edges.add(nodes[nodes.length-2]);
        edges.add(nodes[nodes.length-1]);
        edges.add(calcWeight(way));

        edges.add(nodes[0]);
        edges.add(nodes[1]);
        edges.add(calcWeight(way));
    }

    public void addWay(Way way) {
        ways.add(way);
    }

    public void stop() {
        running = false;
    }

    public FloatArrayList getEdges() {
        return edges;
    }

    public LongArrayList getIds() {
        return ids;
    }
}
