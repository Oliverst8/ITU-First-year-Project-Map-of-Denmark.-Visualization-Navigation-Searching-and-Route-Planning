package dk.itu.map.structures.ArrayLists;

public abstract class PrimitiveArrayList {

    //The current size of the list
    protected int size;

    //The default size of the array
    protected final int ARRAY_INIT_SIZE = 10;

    /**
     * Constructor for the PrimitiveArrayList class.
     * Sets size to 0
     */
    public PrimitiveArrayList() {
        size = 0;
    }

    /**
     * Resizes the array to double the size
     */
    abstract void resize();

    /**
     * Returns the size of the list
     * @return size
     */
    public int size() {
        return size;
    }

}
