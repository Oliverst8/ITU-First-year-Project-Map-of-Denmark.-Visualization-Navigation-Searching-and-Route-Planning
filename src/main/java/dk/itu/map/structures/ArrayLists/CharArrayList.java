package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class CharArrayList extends PrimitiveArrayList implements WriteAble {
    // The array that holds the values
    private char[] array;

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public CharArrayList() {
        super();
        array = new char[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given size
     * @param init_size to be initialized
     */
    public CharArrayList(int init_size) {
        super();
        array = new char[init_size];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given array
     * @param array to be initialized
     */
    public CharArrayList(char[] array) {
        super();
        this.array = array;
        size = array.length;
    }

    /**
     * Resizes the array to double the size
     */
    @Override
    protected void resize() {
        char[] newArray = new char[array.length*2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(char value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
        if(size > biggestIndex) biggestIndex = size-1;
    }

    public void set(int index, char value){
        while(index >= array.length) resize();
        array[index] = value;
        if(index > biggestIndex) biggestIndex = index;
    }

    /**
     * Returns the value at the given index.
     * @param index to be gotten
     * @return int the value at the given index
     */
    public char get(int index) {
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
        size = Math.max(size, biggestIndex+1);
        stream.writeInt(size);
        for (int i = 0; i < size; i++) {
            stream.writeChar(array[i]);
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
        biggestIndex = size-1;
        array = new char[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readChar();
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof CharArrayList)) return false;
        CharArrayList other = (CharArrayList) obj;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(array[i] != other.array[i]) return false;
        }

        return true;
    }

    @Override
    public void exchange(int index1, int index2) {
        char temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
        if(index1 > biggestIndex) biggestIndex = index1;
        else if(index2 > biggestIndex) biggestIndex = index2;
    }


    public void addAll(int[] values) {
        while (size + values.length > array.length) {
            resize();
        }

        System.arraycopy(values, 0, array, size, values.length);
        size += values.length;
    }

    public char[] toArray() {
        char[] result = new char[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }
}
