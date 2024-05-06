package itu.map.structures.ArrayList;

import dk.itu.map.structures.ArrayLists.LongArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveArrayListTest {

    @Test
    void testAdding1BillionElements(){
        LongArrayList longArrayList = new LongArrayList();
        for (long i = 0; i < 100_000_000; i++) {
            longArrayList.add(i);
        }

        assertEquals(100_000_000, longArrayList.size());
        for (long i = 0; i < 100_000_000; i++) {
            assertEquals(i, longArrayList.get((int) i));
        }
    }


}