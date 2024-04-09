package dk.itu.map.structures.HashMaps;

abstract class PrimitiveHashMap {
    final int INITIAL_CAPACITY = 10;

    int capacity;
    int size;

    PrimitiveHashMap() {
        capacity = INITIAL_CAPACITY;
        size = 0;
    }

    public int size() {
        return size;
    }

    int probe(int index, int offset) {
        return (index + offset * offset) % capacity;
    }
}
