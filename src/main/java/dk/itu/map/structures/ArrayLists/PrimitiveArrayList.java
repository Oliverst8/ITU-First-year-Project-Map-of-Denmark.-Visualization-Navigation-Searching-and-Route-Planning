package dk.itu.map.structures.ArrayLists;

import dk.itu.map.structures.WriteAble;

public abstract class PrimitiveArrayList implements WriteAble{

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

    abstract void exchange(int index1, int index2);

    /**
     * Returns the size of the list
     * @return size
     */
    public int size() {
        return size;
    }

}
