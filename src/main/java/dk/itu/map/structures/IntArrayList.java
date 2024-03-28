package dk.itu.map.structures;

import java.util.Iterator;

public class IntArrayList {
    private int[] array;
    private int size;
    private final int ARRAY_INIT_SIZE = 100_000;

    public IntArrayList() {
        array = new int[ARRAY_INIT_SIZE];
        size = 0;
    }

    private void resize() {
        int[] newArray = new int[array.length*2];
        for(int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(int value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
    }

    public int get(int index) {
        return array[index];
    }
    public void set(int index, int element){
        array[index] = element;
    }

    public int size() {
        return size;
    }

}
