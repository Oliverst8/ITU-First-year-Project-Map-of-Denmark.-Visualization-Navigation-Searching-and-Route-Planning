package itu.map.structures.ArrayList;

import java.io.File;

import dk.itu.map.structures.ArrayLists.IntArrayList;

import itu.map.TestUtilities;

import java.util.Arrays;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntArrayListTest {
    @Test void addElementsWithAddAll() {
        IntArrayList list = new IntArrayList();
        list.addAll(new int[]{1, 2, 3, 4});

        assertArrayEquals(new int[]{1, 2, 3, 4}, list.toArray());
    }

    @Test void addElemetsWithAddAllToExisting() {
        IntArrayList list = new IntArrayList();
        list.addAll(new int[]{1, 2, 3, 4});
        list.addAll(new int[]{5, 6, 7, 8});

        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, list.toArray());
    }

    @Test void addElementsWithAddAllWithResize() {
        IntArrayList list = new IntArrayList(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        list.addAll(new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20});

        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, list.toArray());
    }

    @Test void exchangeElements() {
        IntArrayList list = new IntArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.exchange(0, 1);
        assertArrayEquals(new int[]{2, 1, 3, 4}, list.toArray());
    }

    @Test void notEqualsSameType() {
        IntArrayList list = new IntArrayList();
        IntArrayList list2 = new IntArrayList();

        list.addAll(new int[]{1, 2, 3, 4});
        list2.addAll(new int[]{1, 2, 3, 5});

        assertNotEquals(list, list2);
    }

    @Test void notEqualsDifferentType() {
        IntArrayList list = new IntArrayList();
        ArrayList<Integer> list2 = new ArrayList<>();

        list.addAll(new int[]{1, 2, 3, 4});
        list2.addAll(Arrays.asList(1, 2, 3, 4));

        assertNotEquals(list, list2);
    }

    @Test void notEqualsDifferntLength() {
        IntArrayList list = new IntArrayList();
        IntArrayList list2 = new IntArrayList();

        list.addAll(new int[]{1, 2, 3, 4});
        list2.addAll(new int[]{1, 2, 3});

        assertNotEquals(list, list2);
    }

    @Test void readAndWriteToFile() {
        IntArrayList list1 = new IntArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        
        String path = TestUtilities.getTestFilesPath() + "intArrayListTest.txt";

        try {
            list1.write(path);

            IntArrayList list2 = new IntArrayList();
            list2.read(path);

            assertArrayEquals(list1.toArray(), list2.toArray());

            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }

}
