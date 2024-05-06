package dk.itu.map.structures.ArrayLists;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

public class BooleanArrayList extends PrimitiveArrayList {
    // The array that holds the values
    private boolean[] array;

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public BooleanArrayList() {
        super();
        array = new boolean[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given size
     * @param init_size to be initialized
     */
    public BooleanArrayList(int init_size) {
        super();
        array = new boolean[init_size];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given array
     * @param array to be initialized
     */
    public BooleanArrayList(boolean[] array) {
        super();
        this.array = array;
        size = array.length;
    }

    @Override
    protected void resize() {
        boolean[] newArray = new boolean[array.length*2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
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
        size = Math.max(size, biggestIndex+1);
        stream.writeInt(size);
        for (int i = 0; i < size; i++) {
            stream.writeBoolean(array[i]);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        size = stream.readInt();
        biggestIndex = size-1;
        array = new boolean[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readBoolean();
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof BooleanArrayList)) return false;
        BooleanArrayList other = (BooleanArrayList) obj;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(array[i] != other.array[i]) return false;
        }

        return true;
    }

    @Override
    public void exchange(int index1, int index2) {
        boolean temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
        if(index1 > biggestIndex) biggestIndex = index1;
        else if(index2 > biggestIndex) biggestIndex = index2;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(boolean value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
        if(size > biggestIndex) biggestIndex = size-1;
    }

    /**
     * Sets the value at the given index.
     * @param index to be set
     * @param value new value
     */
    public void set(int index, boolean value){
        array[index] = value;
        if(index > biggestIndex) biggestIndex = index;
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return int the value at the given index
     */
    public boolean get(int index) {
        return array[index];
    }

    /**
     * Adds all the values in the given array to the end of the list
     * @param values to be added
     */
    public void addAll(int[] values) {
        while (size + values.length > array.length) {
            resize();
        }

        System.arraycopy(values, 0, array, size, values.length);
        size += values.length;
    }

    /**
     * Returns as a boolean array
     * @return int[] the array
     */
    public boolean[] toArray() {
        boolean[] result = new boolean[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }
}
