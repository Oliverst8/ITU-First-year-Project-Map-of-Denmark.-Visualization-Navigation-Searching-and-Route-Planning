package dk.itu.map.structures;

import java.util.Arrays;

public class CoordArrayList {
    private final int ARRAY_INIT_SIZE = 100_000;
    private float[] array;
    private int size;

    public CoordArrayList() {
        array = new float[ARRAY_INIT_SIZE];
        size = 0;
    }

    public CoordArrayList(int init_size) {
        array = new float[init_size];
        size = 0;
    }

    public CoordArrayList(float[] array) {
        this.array = array;
        size = array.length;
    }

    private void resize() {
        float[] newArray = new float[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(float value) {
        if (size + 1 > array.length) {
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

    public void reverse() {
        for (int i = 0; i < size / 2; i += 2) {
            float tempX = array[i];
            float tempY = array[i + 1];

            array[i] = array[size - i - 2];
            array[i + 1] = array[size - i - 1];

            array[size - i - 2] = tempX;
            array[size - i - 1] = tempY;
        }
    }

    public float get(int index) {
        if (index < 0)
            return array[size + index];
        else
            return array[index];
    }

    public int size() {
        return size;
    }
}
