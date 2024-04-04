package dk.itu.map.structures;

import java.io.*;

public class LongIntHashMap extends PrimitiveHashMap implements WriteAble{
    long[] keys;
    int[] value;

    public LongIntHashMap() {
        super();
        keys = new long[INITIAL_CAPACITY];
        value = new int[INITIAL_CAPACITY];
    }

    public void put(long key, int value) {
        if (size >= capacity * 0.75) resize();

        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != 0) {
            index = probe(index, offset);
            offset++;
        }

        this.keys[index] = key;
        this.value[index] = value;
        size++;
    }

    public int get(long key) {
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key) {
            index = probe(index, offset);
            offset++;
        }

        return value[index];
    }

    public boolean containsKey(long key) {
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key) {
            index = probe(index, offset);
            if(index > capacity || index < 0) return false;
            offset++;
        }

        return keys[index] == key;
    }

    public void remove(long key) {
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key && keys[index] != 0) {
            index = probe(index, offset);
            offset++;
        }

        if (keys[index] == key) {
            keys[index] = 0;
            value[index] = 0;
            size--;
        }
    }

    private void resize() {
        capacity *= 2;

        long[] newKeys = new long[capacity];
        int[] newValues = new int[capacity];

        for (int i = 0; i < capacity / 2; i++) {
            if (keys[i] != 0) {
                int index = getIndex(keys[i]);
                int offset = 1;

                while (newKeys[index] != 0) {
                    index = probe(index, offset);
                    offset++;
                }

                newKeys[index] = keys[i];
                newValues[index] = value[i];
            }
        }

        keys = newKeys;
        value = newValues;
    }

    // private void resize() {
    //     long[] oldKeys = keys;
    //     int[] oldValue = value;

    //     capacity *= 2;
    //     size = 0;

    //     keys = new long[capacity];
    //     value = new int[capacity];

    //     for (int i = 0; i < oldKeys.length; i++) {
    //         if (oldKeys[i] != 0) {
    //             put(oldKeys[i], oldValue[i]);
    //         }
    //     }
    // }

    protected int getIndex(long key) {
        return (int) (key % (long) capacity);
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
                stream.writeLong(keys[i]);
                stream.writeInt(value[i]);
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
        keys = new long[size];
        value = new int[size];
        for (int i = 0; i < size; i++) {
            keys[i] = stream.readLong();
            value[i] = stream.readInt();
        }
    }
}
