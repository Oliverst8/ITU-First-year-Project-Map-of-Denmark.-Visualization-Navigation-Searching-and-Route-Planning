package dk.itu.map.structures;

public class FloatArrayList {
    private final int ARRAY_INIT_SIZE = 100_000;
    private float[] array;
    private int size;

    public FloatArrayList() {
        array = new float[ARRAY_INIT_SIZE];
        size = 0;
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

    public float get(int index) {
        return array[index];
    }

    public int size() {
        return size;
    }
}
