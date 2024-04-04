package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.io.*;
import java.util.Arrays;

public class LongArrayList extends PrimitiveArrayList implements WriteAble {
    // The array that holds the values
    private long[] list;

    /**
     * Constructor for the LongArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public LongArrayList() {
        super();
        list = new long[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the LongArrayList class
     * Initializes the array with the given size
     * @param init_size size of the array
     */
    public LongArrayList(int init_size) {
        super();
        list = new long[init_size];
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(long value) {
        if (size >= list.length) {
            resize();
        }
        list[size] = value;
        size++;
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return long the value at the index
     */
    public long get(int index) {
        return list[index];
    }

    /**
     * Resizes the array to double the size
     */
    protected void resize() {
        long[] newList = new long[list.length * 2];
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }

    @Override
    public void write(String path) throws IOException {
        DataOutputStream stream = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(path)
                )
        );
        write(stream);
        stream.close();
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeInt(size);
        for (int i = 0; i < size; i++) {
            stream.writeLong(list[i]);
        }
    }

    @Override
    public void read(String path) throws IOException{
        DataInputStream stream = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(path)
                )
        );
        read(stream);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        size = stream.readInt();
        list = new long[size];
        for (int i = 0; i < size; i++) {
            list[i] = stream.readLong();
        }
    }

    public long[] toArray() {
        return Arrays.copyOfRange(list, 0, size);
    }
}
