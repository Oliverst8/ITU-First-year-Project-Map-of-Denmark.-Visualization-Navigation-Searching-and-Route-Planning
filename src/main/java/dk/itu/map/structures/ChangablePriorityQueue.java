package dk.itu.map.structures;

import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

public class ChangablePriorityQueue {
//Soooo, what we need is a normal priorityQueue with a dictionary(map I guess) that is a direct access array

    //Jeg ved ikke om vi skal have denne her, eller bare give ting med som argumenter
    private Graph graph;
    private FloatArrayList shortestPathList;
    private IntArrayList lastDestinationList;
    private IntArrayList heap;
    //private Node[] heapReference;

    public ChangablePriorityQueue(Graph graph){
        this.graph = graph;
        shortestPathList = new FloatArrayList();
        lastDestinationList = new IntArrayList();
        heap = new IntArrayList();
        for(int i = 0; i < graph.getIds().size(); i++){
            shortestPathList.add(Float.MAX_VALUE);
            heap.add(i);
            swim(i);
        }
    }
    private int deleteMinValue(){
        return (int) heap.remove();
    }
    private void decreaseValue(int id, float newDistance){
        throw new UnsupportedOperationException();
    }
    private void swim(int index){
        throw new UnsupportedOperationException();
        //if(shortestPathList.get(index) < shortestPathList.get(+))
    }
}
