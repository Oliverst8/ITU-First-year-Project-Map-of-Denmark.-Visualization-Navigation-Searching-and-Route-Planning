package dk.itu.map.structures;

import java.util.Arrays;

public class FloatArrayList {
    private final int ARRAY_INIT_SIZE = 100_000;
    private float[] array;
    private int size;

    public FloatArrayList() {
        array = new float[ARRAY_INIT_SIZE];
        size = 0;
    }

    public FloatArrayList(int init_size) {
        array = new float[init_size];
        size = 0;
    }

    public FloatArrayList(float[] array) {
        this.array = array;
        size = array.length;
    }

    private void resize() {
        float[] newArray = new float[array.length*2];
        for(int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(float value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
    }

    public void addAll(float[] values) {
        while (size + values.length > array.length) {
            resize();
        }
        System.arraycopy(values, 0, array, size, values.length);
        size += values.length;
    }

    public float[] toArray() {
        return Arrays.copyOf(array, size);
    }

    public double[] toDoubleArray() {
        double[] output = new double[size];
        for (int i = 0; i < size; i++) {
            output[i] = array[i];
        }
        return output;
    }

    public float get(int index) {
        return array[index];
    }

    public int size() {
        return size;
    }
}
