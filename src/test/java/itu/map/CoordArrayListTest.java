package itu.map;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.CoordArrayList;
import dk.itu.map.structures.LongFloatArrayHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CoordArrayListTest {

    @Test public void createListAndReverseEvenList() {
        CoordArrayList list = new CoordArrayList(new float[]{1, 5, 2, 6, 3, 7, 4, 8});
        list.reverse();
        assertArrayEquals(new float[]{4, 8, 3, 7, 2, 6, 1, 5}, list.toArray());
    }
    
    
    @Test public void createListAndReverseOddList() {
        CoordArrayList list = new CoordArrayList(new float[]{1, 5, 2, 6, 3, 7, 4, 8, 5, 10});
        list.reverse();
        assertArrayEquals(new float[]{5, 10, 4, 8, 3, 7, 2, 6, 1, 5}, list.toArray());
    }
    
    @Test public void createListAndReverseTuple() {
        CoordArrayList list = new CoordArrayList(new float[]{1, 5});
        list.reverse();
        assertArrayEquals(new float[]{1, 5}, list.toArray());
    }
}
