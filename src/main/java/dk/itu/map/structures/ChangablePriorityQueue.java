package dk.itu.map.structures;

public class ChangablePriorityQueue {
//Soooo, what we need is a normal priorityQueue with a dictionary(map I guess) that is a direct access array
    private final float[] value;
    private final int[] heap;
    private int size;
    private int currentSize;


    public ChangablePriorityQueue(Graph graph){
        currentSize = 0;
        size = graph.getIds().size();
        value = new float[size];
        heap = new int[size];
        for(int i = 0; i < size; i++){
            value[i] = Float.MAX_VALUE;
            heap[i] = i;
        }
    }

    private void swim(int index){
        if(index == 0) return;
        int parentIndex = (index-1)/4;
        while(value[heap[parentIndex]] > value[heap[index]]) {
            exch(parentIndex, index);
            index = parentIndex;
            parentIndex = (index-1)/4;
            if(index == 0) return;
        }
    }

    private void sink(int index){
        while(true){
            int smallestChild = index * 4 + 1;
            for (int i = 2; i <= 4; i++) {
                int childValue = index * 4 + i;
                if (value[smallestChild] > value[childValue]) smallestChild = childValue;
            }
            if (value[index] > value[smallestChild]) {
                exch(index, smallestChild);
                index = smallestChild;
            } else break;

        }
    }

    private void exch(int index1, int index2){
        int oldIndex = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = oldIndex;
    }
    public void decreaseValueTo(int id, float newDistance){

        int index = id;

        if(value[id] == Float.MAX_VALUE) {
            exch(currentSize, id);
            index = currentSize;
            currentSize++;
        }

        if(newDistance > value[id]) throw new IllegalArgumentException("new value cant be bigger then old: \nnew value: " + newDistance + " \nold value: " + value[heap[id]]);

        value[id] = newDistance;

        swim(index);
    }
    public int deleteMinValue(){
        int min = heap[0];

        value[min] = Float.POSITIVE_INFINITY;

        exch(--size, 0);

        currentSize--;

        sink(0);

        return min;
    }
    public int size(){
        return size;
    }
    public float getValue(int index){
        return value[index];
    }
}
