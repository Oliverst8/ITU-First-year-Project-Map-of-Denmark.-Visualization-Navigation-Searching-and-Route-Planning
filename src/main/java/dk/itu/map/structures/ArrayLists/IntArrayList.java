package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class IntArrayList extends PrimitiveArrayList implements WriteAble {
    // The array that holds the values
    private int[] array;

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public IntArrayList() {
        super();
        array = new int[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given size
     * @param init_size to be initialized
     */
    public IntArrayList(int init_size) {
        super();
        array = new int[init_size];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given array
     * @param array to be initialized
     */
    public IntArrayList(int[] array) {
        super();
        this.array = array;
        size = array.length;
    }

    /**
     * Resizes the array to double the size
     */
    @Override
    protected void resize() {
        int[] newArray = new int[array.length*2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return int the value at the given index
     */
    public int get(int index) {
        return array[index];
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
            stream.writeInt(array[i]);
        }
    }

    @Override
    public void read(String path) throws IOException {
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
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readInt();
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof IntArrayList)) return false;
        IntArrayList other = (IntArrayList) obj;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(array[i] != other.array[i]) return false;
        }

        return true;
    }

    @Override
    public void exchange(int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(int value) {
        if(size + 1 > array.length) {
            resize();
        }

        array[size] = value;
        size++;
    }

    public void addAll(int[] values) {
        while (size + values.length > array.length) {
            resize();
        }

        System.arraycopy(values, 0, array, size, values.length);
        size += values.length;
    }

    public int[] toArray() {
        int[] result = new int[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }
}
