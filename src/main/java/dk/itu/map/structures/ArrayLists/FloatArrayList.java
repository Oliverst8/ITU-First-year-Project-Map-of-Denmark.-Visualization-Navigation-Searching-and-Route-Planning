package dk.itu.map.structures.ArrayLists;

import java.util.Arrays;

import java.io.IOException;
import java.io.Serializable;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;

public class FloatArrayList extends PrimitiveArrayList implements Serializable {
    private float[] array;

    public FloatArrayList() {
        array = new float[ARRAY_INIT_SIZE];
        size = 0;
    }

    public FloatArrayList(int init_size) {
        array = new float[init_size];
        size = 0;
    }

    public FloatArrayList(float[] array) {
        this.array = array;
        size = array.length;
    }

    @Override
    protected void resize() {
        float[] newArray = new float[array.length*2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    @Override
    public void write(String path) throws FileNotFoundException, IOException {
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
            stream.writeFloat(array[i]);
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
        array = new float[size];
        for (int i = 0; i < size; i++) {
            array[i] = stream.readFloat();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FloatArrayList)) return false;
        FloatArrayList other = (FloatArrayList) obj;
        if(size != other.size) return false;
        for(int i = 0; i < size; i++){
            if(array[i] != other.array[i]) return false;
        }

        return true;
    }

    @Override
    public void exchange(int index1, int index2) {
        float temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public void add(float value) {
        if(size + 1 > array.length) {
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

    public float get(int index) {
        return array[index];
    }

    public int size() {
        return size;
    }
}
