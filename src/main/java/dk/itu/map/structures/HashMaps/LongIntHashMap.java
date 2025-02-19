package dk.itu.map.structures.HashMaps;

import dk.itu.map.structures.WriteAble;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class LongIntHashMap extends PrimitiveHashMap implements WriteAble {
    long[] keys;
    int[] value;
    private final long DEFAULT_KEY_VALUE = 0;
    private final int REMOVED_VALUE = Integer.MIN_VALUE;

    private int total;

    /**
     * Instantiates a new LongIntHashMap with the default capacity.
     */
    public LongIntHashMap() {
        super();
        total = 0;
        keys = new long[INITIAL_CAPACITY];
        value = new int[INITIAL_CAPACITY];
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
        stream.writeInt(capacity);
        stream.writeInt(total);
        for (int i = 0; i < capacity; i++) {
                stream.writeLong(keys[i]);
                stream.writeInt(value[i]);
        }
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        size = stream.readInt();
        capacity = stream.readInt();
        total = stream.readInt();
        keys = new long[capacity];
        value = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            keys[i] = stream.readLong();
            value[i] = stream.readInt();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof LongIntHashMap)) return false;
        LongIntHashMap other = (LongIntHashMap) obj;
        if (other.size() != size) return false;
        return Arrays.equals(keys, other.keys) && Arrays.equals(value, other.value);
    }

    /**
     * Checks if the key is the default value. It is, then it throws an exception.
     * @param key the key to check
     */
    private void validateKey(long key) {
        if(key == DEFAULT_KEY_VALUE) throw new IllegalArgumentException("Key cannot be " + key);
    }

    /**
     * Checks if the value is the removed value. It is, then it throws an exception.
     * @param value the value to check
     */
    private void validateValue(int value) {
        if(value == REMOVED_VALUE) throw new IllegalArgumentException("Value cannot be " + value);
    }

    /**
     * Puts a value into the map with the given key.
     * @param key the key
     * @param value the value to put
     */
    public void put(long key, int value) {
        validateKey(key);
        validateValue(value);
        if (total >= Math.floor(capacity * 0.75)) resize();

        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != 0 && keys[index] != key) {
            index = probe(index, offset);
            offset++;
        }

        if(keys[index] != key) {
            size++;
            total++;
        }

        this.keys[index] = key;
        this.value[index] = value;
    }

    /**
     * Gets the value from the map with the given key.
     * @param key the key to get the value from
     * @return the value from the map
     */
    public int get(long key) {
        validateKey(key);
        int index = getIndex(key);
        int offset = 1;

        long keyFound = keys[index];

        while (keyFound != key) {
            index = probe(index, offset);
            keyFound = keys[index];
            if(keys[index] == DEFAULT_KEY_VALUE) throw new IllegalArgumentException("Key not found");
            offset++;
        }

        return value[index];
    }

    /**
     * Checks if the map contains the given key.
     * @param key the key to check
     * @return true if the key is in the map, false otherwise
     */
    public boolean containsKey(long key) {
        validateKey(key);
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key) {
            index = probe(index, offset);
            if (keys[index] == DEFAULT_KEY_VALUE) return false;
            offset++;
        }

        if (value[index] == REMOVED_VALUE) return false;

        return keys[index] == key;
    }

    /**
     * Removes the key from the map.
     * @param key the key to remove
     */
    public void remove(long key) {
        validateKey(key);
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key && keys[index] != 0) {
            index = probe(index, offset);
            offset++;
        }

        if (keys[index] == key) {
            value[index] = REMOVED_VALUE;
            size--;
        }
    }

    /**
     * Resizes the map.
     * It doubles the capacity and rehashes the keys and values.
     */
    private void resize() {
        capacity *= 2;

        long[] newKeys = new long[capacity];
        int[] newValues = new int[capacity];

        for (int i = 0; i < capacity / 2; i++) {
            if (keys[i] != 0) {

                if(keys[i] == REMOVED_VALUE) continue;

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

    /**
     * Gets the index of the key in the map.
     * @param key the key to get the index of
     * @return the index of the key
     */
    protected int getIndex(long key) {
        return (int) (key % (long) capacity);
    }

    /**
     * Gets the keys in the map.
     * @return the keys in the map as an array
     */
    public long[] getKeys(){
        long[] keys = new long[size];

        int j = 0;
        for(int i = 0; i < capacity; i++) {
            long key = this.keys[i];
            if(key == DEFAULT_KEY_VALUE || key == REMOVED_VALUE) continue;

            keys[j] = key;
            j++;
        }

        return keys;
    }

    /**
     * Gets the values in the map.
     * @return the values in the map as an array
     */
    public int[] getValues(){
        int[] values = new int[size];

        int j = 0;
        for(int i = 0; i < capacity; i++) {
            int value = this.value[i];
            if(value == DEFAULT_KEY_VALUE || value == REMOVED_VALUE) continue;

            values[j] = value;
            j++;
        }

        return values;
    }

    /**
     * Gets the keys in the map with the given value.
     * @param value the value to get the keys for
     * @return the keys in the map with the given value
     */
    public HashSet<Long> getKey(int value){
        HashSet<Long> set = new HashSet<>();
        for (int i = 0; i < capacity; i++) {
            if (this.value[i] == value) {
                set.add(keys[i]);
            }
        }
        return set;
    }
}
