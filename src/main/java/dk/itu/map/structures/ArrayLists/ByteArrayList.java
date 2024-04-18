package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

import java.io.*;

public class ByteArrayList extends PrimitiveArrayList implements WriteAble{
    // The array that holds the values
    private byte[] array;

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the default size
     * Default size is 10
     */
    public ByteArrayList() {
        super();
        array = new byte[ARRAY_INIT_SIZE];
    }

    /**
     * Constructor for the IntArrayList class
     * Initializes the array with the given size
     * @param init_size to be initialized
     */
    public ByteArrayList(int init_size) {
        super();
        array = new byte[init_size];
    }

    /**
     * Resizes the array to double the size
     */
    @Override
    protected void resize() {
        byte[] newArray = new byte[array.length*2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    /**
     * Adds a value to the empty spot in the array.
     * If the array is full, it will resize the array.
     * @param value to be inserted
     */
    public void add(byte value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
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
        array = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readByte();
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ByteArrayList)) return false;
        ByteArrayList other = (ByteArrayList) obj;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(array[i] != other.array[i]) return false;
        }
        return true;
    }

    @Override
    public void exchange(int index1, int index2) {
        byte temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

}
