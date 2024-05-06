package itu.map.structures.ArrayList;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import java.io.File;

import itu.map.TestUtilities;

import dk.itu.map.structures.ArrayLists.FloatArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FloatArrayListTest {
    @Test void addElementsWithAddAll() {
        FloatArrayList list = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});

        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f}, list.toArray());
    }

    @Test void addElemetsWithAddAllToExisting() {
        FloatArrayList list = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});
        list.addAll(new float[]{5.0f, 6.0f, 7.0f, 8.0f});

        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f}, list.toArray());
    }

    @Test void addElementsWithAddAllWithResize() {
        FloatArrayList list = new FloatArrayList(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f});
        list.addAll(new float[]{11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f, 17.0f, 18.0f, 19.0f, 20.0f});

        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f, 17.0f, 18.0f, 19.0f, 20.0f}, list.toArray());
    }

    @Test void toDoubleArray() {
        FloatArrayList list = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});

        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0}, list.toDoubleArray());
    }

    @Test void exchange() {
        FloatArrayList list = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});

        list.exchange(0, 1);

        assertArrayEquals(new float[]{2.0f, 1.0f, 3.0f, 4.0f}, list.toArray());
    }

    @Test void notEqualsSameType() {
        FloatArrayList list = new FloatArrayList();
        FloatArrayList list2 = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});
        list2.addAll(new float[]{1.0f, 2.0f, 3.0f, 5.0f});

        assertFalse(list.equals(list2));
    }

    @Test void notEqualsDifferntLength() {
        FloatArrayList list = new FloatArrayList();
        FloatArrayList list2 = new FloatArrayList();
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});
        list2.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f});

        assertFalse(list.equals(list2));
    }

    @Test void notEqualsDifferentType() {
        FloatArrayList list = new FloatArrayList();
        ArrayList<Float> list2 = new ArrayList<>(Arrays.asList(new Float[]{1.0f, 2.0f, 3.0f, 4.0f}));
        list.addAll(new float[]{1.0f, 2.0f, 3.0f, 4.0f});

        assertFalse(list.equals(list2));
    }

    @Test void readAndWriteToFile() {
        FloatArrayList list1 = new FloatArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        
        String path = TestUtilities.getTestFilesPath() + "floatArrayListTest.txt";

        try {
            list1.write(path);
            FloatArrayList list2 = new FloatArrayList();
            list2.read(path);
            assertArrayEquals(list1.toArray(), list2.toArray());
            TestUtilities.deleteFile(path);
        } catch (Exception e) {
            fail("Exception thrown: " + e);
        }
    }

    @Test
    void testEqualsShouldEqual() {
        FloatArrayList list1 = new FloatArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        FloatArrayList list2 = new FloatArrayList();
        list2.add(1);
        list2.add(2);
        list2.add(3);
        list2.add(4);

        assertEquals(list1, list2);
    }

    @Test
    void testAddShouldResize() {
        FloatArrayList list = new FloatArrayList(1);
        list.add(1f);
        list.add(2f);

        assertEquals(2, list.capacity());
    }

    @Test
    void testGet(){
        FloatArrayList list = new FloatArrayList();
        list.add(1);
        assertEquals(1, list.get(0));
    }

    @Test
    void testSize(){
        FloatArrayList list = new FloatArrayList();
        list.add(1);
        assertEquals(1, list.size());
    }
}
