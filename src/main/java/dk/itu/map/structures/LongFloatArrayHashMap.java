package dk.itu.map.structures;

/**
 * <pre>LongFloatArrayHashMap is a hash map that uses longs as keys and float arrays as values.
 * Note: The key cannot be 0.</pre>
 */
public class LongFloatArrayHashMap {
    private final int INITIAL_CAPACITY = 1_000_000;

    private int capacity;
    private int size;
    private long[] keys;
    private float[] value0;
    private float[] value1;

    public LongFloatArrayHashMap() {
        capacity = INITIAL_CAPACITY;
        keys = new long[INITIAL_CAPACITY];
        value0 = new float[INITIAL_CAPACITY];
        value1 = new float[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * @return the number of elements in the map
     */
    public int size() {
        return size;
    }

    /**
     * <pre>Puts a value into the map
     * Note: If the key already exists, the value will be overwritten.
     * Note: The key cannot be 0.</pre>
     * @param key to put in the map.
     * @param value to put in the map.
     */
    public void put(long key, float[] value) {
        if (size >= capacity * 0.75) {
            resizeArrays();
        }

        int index = getIndex(key);
        int offset = 1;
        
        while (keys[index] != 0) {
            index = (index + offset * offset) % capacity;
            offset++;
        }

        keys[index] = key;
        value0[index] = value[0];
        value1[index] = value[1];
        size++;
    }

    /**
     * Gets a value from the map.
     * @param key to get the value from.
     * @return the value from the map.
     */
    public float[] get(long key) {
        int index = getIndex(key);

        int offset = 1;

        while (keys[index] != key && keys[index] != 0) {
            index = (index + offset * offset) % capacity;
            offset++;
        }

        return new float[] {value0[index], value1[index]};
    }

    /**
     * Checks if the map contains a key.
     * @param key to check for.
     * @return true if the key exists in the map, otherwise false.
     */
    public boolean containsKey(long key) {
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key && keys[index] != 0) {
            index = (index + offset * offset) % capacity;
            offset++;
        }

        return keys[index] == key;
    }

    /**
     * Removes a key from the map.
     * @param key to remove from the map.
     */
    public void remove(long key) {
        int index = getIndex(key);
        int offset = 1;

        while (keys[index] != key && keys[index] != 0) {
            index = (index + offset * offset) % capacity;
            offset++;
        }

        if (keys[index] == key) {
            keys[index] = 0;
            value0[index] = 0;
            value1[index] = 0;
            size--;
        }
    }

    /**
     * <pre>Resizes the arrays in the map.
     * Note: The capacity will be doubled.</pre>
     */
    private void resizeArrays() {
        int newCapacity = capacity * 2;

        long[] newKeys = new long[newCapacity];
        float[] newValues0 = new float[newCapacity];
        float[] newValues1 = new float[newCapacity];

        for (int i = 0; i < capacity; i++) {
            if (keys[i] != 0) {
                int index = getIndex(keys[i], newCapacity);
                int offset = 1;

                while (newKeys[index] != 0) {
                    index = (index + offset * offset) % newCapacity;
                    offset++;
                }

                newKeys[index] = keys[i];
                newValues0[index] = value0[i];
                newValues1[index] = value1[i];
            }
        }

        keys = newKeys;
        value0 = newValues0;
        value1 = newValues1;
        capacity = newCapacity;
    }

    /**
     * Gets the index of a key in the map.
     * @param key to get the index for.
     * @return the index of the key.
     */
    private int getIndex(long key) {
        return (int) (key % (long) capacity);
    }

    /**
     * Gets the index of a key in the map.
     * @param key to get the index for.
     * @param capacity of the map.
     * @return the index of the key.
     */
    private int getIndex(long key, int capacity) {
        return (int) (key % (long) capacity);
    }
}