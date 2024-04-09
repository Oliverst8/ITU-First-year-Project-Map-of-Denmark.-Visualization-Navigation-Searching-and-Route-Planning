package dk.itu.map.structures.HashMaps;

/**
 * <pre>LongCoordHashMap is a hash map that uses longs as keys and float arrays as values.
 * Note: The key cannot be 0.</pre>
 */
public class LongCoordHashMap extends PrimitiveHashMap {

    private long[] keys;
    private float[] valueX;
    private float[] valueY;

    public LongCoordHashMap() {
        keys = new long[INITIAL_CAPACITY];
        valueX = new float[INITIAL_CAPACITY];
        valueY = new float[INITIAL_CAPACITY];
    }

    /**
     * <pre>Puts a value into the map
     * Note: If the key already exists, the value will be overwritten.
     * Note: The key cannot be 0.</pre>
     * @param key to put in the map.
     * @param value to put in the map.
     */
    public void put(long key, float[] value) {
        if (size >= capacity * 0.75) resize();

        int index = getIndex(key);
        int offset = 1;
        
        while (keys[index] != 0) {
            index = probe(index, offset);
            offset++;
        }

        keys[index] = key;
        valueX[index] = value[0];
        valueY[index] = value[1];
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
            index = probe(index, offset);
            offset++;
        }

        return new float[] {valueX[index], valueY[index]};
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
            index = probe(index, offset);
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
            index = probe(index, offset);
            offset++;
        }

        if (keys[index] == key) {
            keys[index] = 0;
            valueX[index] = 0;
            valueY[index] = 0;
            size--;
        }
    }

    /**
     * <pre>Resizes the arrays in the map.
     * Note: The capacity will be doubled.</pre>
     */
    private void resize() {
        capacity *= 2;

        long[] newKeys = new long[capacity];
        float[] newValues0 = new float[capacity];
        float[] newValues1 = new float[capacity];

        for (int i = 0; i < capacity / 2; i++) {
            if (keys[i] != 0) {
                int index = getIndex(keys[i]);
                int offset = 1;

                while (newKeys[index] != 0) {
                    index = probe(index, offset);
                    offset++;
                }

                newKeys[index] = keys[i];
                newValues0[index] = valueX[i];
                newValues1[index] = valueY[i];
            }
        }

        keys = newKeys;
        valueX = newValues0;
        valueY = newValues1;
    }

    /**
     * Gets the index of a key in the map.
     * @param key to get the index for.
     * @return the index of the key.
     */
    private int getIndex(long key) {
        return (int) (key % (long) capacity);
    }
}