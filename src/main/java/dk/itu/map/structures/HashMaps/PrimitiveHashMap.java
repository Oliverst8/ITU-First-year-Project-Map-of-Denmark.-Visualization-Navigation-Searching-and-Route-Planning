package dk.itu.map.structures.HashMaps;

/**
 * Abstract class for the primitive hash maps
 * Every hash map made from this class will have its capacity and a size bound to the inte
 
 */
abstract class PrimitiveHashMap {
    final int INITIAL_CAPACITY = 10;

    int capacity;
    int size;

    /**
     * Instantiates a new PrimitiveHashMap with the default capacity.
     */
    PrimitiveHashMap() {
        capacity = INITIAL_CAPACITY;
        size = 0;
    }

    /**
     * @return the size of the map
     */
    public int size() {
        return size;
    }

    /**
     * Probes the index with the offset
     * @param index the index to probe
     * @param offset the offset to probe with
     * @return the probed index
     */
    int probe(int index, int offset) {
        return (index + offset * offset) % capacity;
    }
}
