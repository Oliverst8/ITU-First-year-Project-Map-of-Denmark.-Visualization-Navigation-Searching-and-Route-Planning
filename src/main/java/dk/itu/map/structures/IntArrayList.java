package dk.itu.map.structures;

import java.io.*;
import java.util.Iterator;

public class IntArrayList implements Serializable, WriteAble {
    private int[] array;
    private int size;
    private final int ARRAY_INIT_SIZE = 100_000;

    public IntArrayList() {
        array = new int[ARRAY_INIT_SIZE];
        size = 0;
    }

    public IntArrayList(int size) {
        array = new int[size];
        this.size = 0;
    }

    private void resize() {
        int[] newArray = new int[array.length*2];
        for(int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        array = newArray;
    }

    public void add(int value) {
        if(size + 1 > array.length) {
            resize();
        }
        array[size] = value;
        size++;
    }

    public int get(int index) {
        return array[index];
    }
    public void set(int index, int element){
        array[index] = element;
    }

    public int size() {
        return size;
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
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readInt();
        }
    }
}
