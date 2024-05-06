package dk.itu.map.structures.ArrayLists;

import java.util.Arrays;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class LongArrayList extends PrimitiveArrayList {
    // The array that holds the values
    private long[] array;

    /**
     * Constructor for the LongArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public LongArrayList() {
        super();
        array = new long[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the LongArrayList class
     * Initializes the array with the given size
     * @param init_size size of the array
     */
    public LongArrayList(int init_size) {
        super(init_size);
        array = new long[init_size];
    }

    /**
     * Resizes the array to double the size
     */
    @Override
    protected void resize() {
        long[] newList = new long[array.length * 2];
        System.arraycopy(array, 0, newList, 0, array.length);
        array = newList;
        capacity = array.length;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeInt(size);
        for (int i = 0; i < size; i++) {
            stream.writeLong(array[i]);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        size = stream.readInt();
        array = new long[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readLong();
        }
    }

    @Override
    public void exchange(int index1, int index2) {
        long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    /**
     * @return list as an long array
     */
    public long[] toArray() {
        return Arrays.copyOfRange(array, 0, size);
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(long value) {
        if (size >= array.length) {
            resize();
        }

        array[size] = value;
        size++;
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return long the value at the index
     */
    public long get(int index) {
        return array[index];
    }
}
