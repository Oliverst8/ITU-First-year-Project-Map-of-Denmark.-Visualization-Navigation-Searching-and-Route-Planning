package dk.itu.map.structures.ArrayLists;

import java.util.Arrays;

public class CoordArrayList extends PrimitiveArrayList {

    // The array that holds the values
    private float[] array;

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a default size.
     * default size is 10
      */
    public CoordArrayList() {
        super();
        array = new float[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given size.
     * @param init_size
     */
    public CoordArrayList(int init_size) {
        super();
        array = new float[init_size];
    }

    /**
     * Constructor for the CoordArrayList class. Initializes the array with a given array.
     * @param array
     */
    public CoordArrayList(float[] array) {
        this.array = array;
        size = array.length;
    }

    /**
     * Resizes the array to double the size.
     */
    protected void resize() {
        float[] newArray = new float[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(float value) {
        if (size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
    }

    /**
     * Adds an array of values to the empty spots in the array.
     * If the array is full, it will resize the array.
     * @param values to be inserted
     */
    public void addAll(float[] values) {
        while (size + values.length > array.length) {
            resize();
        }
        System.arraycopy(values, 0, array, size, values.length);
        size += values.length;
    }

    /**
     * Converts the list to an float array.
     * @return float[]
     */
    public float[] toArray() {
        return Arrays.copyOf(array, size);
    }

    /**
     * Reverses the array.
     * Will swap in coordinate pairs.
     * It will swap the first and last values, then the second and second to last values, and so on.
     * If the size is odd, the middle value will not be swapped.
     */
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

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return float value at the index
     */
    public float get(int index) {
        if (index < 0)
            return array[size + index];
        else
            return array[index];
    }

}
