package itu.map.structures.ArrayList;

import java.io.File;

import itu.map.TestUtilities;

import dk.itu.map.structures.ArrayLists.LongArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LongArrayListTest {
    @Test public void createWidthSize() {
        LongArrayList list = new LongArrayList(100);
        assertEquals(100, list.capacity());
    }

    @Test public void exchangeElements() {
        LongArrayList list = new LongArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.exchange(0, 1);
        assertArrayEquals(new long[]{2, 1, 3, 4}, list.toArray());
    }

    @Test public void readAndWriteToFile() {
        LongArrayList list1 = new LongArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        
        String path = TestUtilities.getTestFilesPath() + "longArrayListTest.txt";

        try {
            list1.write(path);

            LongArrayList list2 = new LongArrayList();
            list2.read(path);

            assertArrayEquals(list1.toArray(), list2.toArray());

            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }
}