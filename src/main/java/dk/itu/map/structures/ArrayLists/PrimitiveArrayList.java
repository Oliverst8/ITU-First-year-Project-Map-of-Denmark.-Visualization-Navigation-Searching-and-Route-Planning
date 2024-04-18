package dk.itu.map.structures.ArrayLists;

public abstract class PrimitiveArrayList {

    //The current size of the list
    protected int size;
    protected int capacity;

    //The default size of the array
    protected final int ARRAY_INIT_SIZE = 10;

    /**
     * Constructor for the PrimitiveArrayList class.
     * Sets size to 0
     */
    public PrimitiveArrayList() {
        size = 0;
        capacity = ARRAY_INIT_SIZE;
    }

    public PrimitiveArrayList(int init_size) {
        size = 0;
        capacity = init_size;
    }

    /**
     * Resizes the array to double the size
     */
    abstract void resize();

    /**
     * Swaps the values at the given indexes
     * @param index1 the first index
     * @param index2 the second index
     */
    abstract void exchange(int index1, int index2);

    /**
     * Returns the size of the list
     * @return size
     */
    public int size() {
        return size;
    }

    /**
     * Returns the capacity of the list
     * @return capacity
     */
    public int capacity() {
        return capacity;
    }
}
