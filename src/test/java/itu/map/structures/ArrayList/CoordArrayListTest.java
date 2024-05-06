package itu.map.structures.ArrayList;

import dk.itu.map.structures.ArrayLists.CoordArrayList;

import itu.map.TestUtilities;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CoordArrayListTest {
    @Test public void createWidthSize() {
        CoordArrayList list = new CoordArrayList(100);
        assertEquals(100, list.capacity());
    }

    @Test public void createListAndAdd() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListWithArray() {
        CoordArrayList list = new CoordArrayList(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f});
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndGet() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        assertEquals(1.0f, list.get(0)[0]);
        assertEquals(5.0f, list.get(0)[1]);
        assertEquals(2.0f, list.get(1)[0]);
        assertEquals(6.0f, list.get(1)[1]);
        assertEquals(3.0f, list.get(2)[0]);
        assertEquals(7.0f, list.get(2)[1]);
        assertEquals(4.0f, list.get(3)[0]);
        assertEquals(8.0f, list.get(3)[1]);
    }

    @Test public void createListAndExchange() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.exchange(0, 1);
        assertArrayEquals(new float[]{2.0f, 6.0f, 1.0f, 5.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndAddAll() {
        CoordArrayList list = new CoordArrayList();
        list.addAll(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f});
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndReverseOddList() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.add(5.0f, 10.0f);
        list.reverse();
        assertArrayEquals(new float[]{5.0f, 10.0f, 4.0f, 8.0f, 3.0f, 7.0f, 2.0f, 6.0f, 1.0f, 5.0f}, list.toArray());
    }

    @Test public void createListAndReverseEvenList() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.reverse();
        assertArrayEquals(new float[]{4.0f, 8.0f, 3.0f, 7.0f, 2.0f, 6.0f, 1.0f, 5.0f}, list.toArray());
    }

    @Test public void createListAndReverseTuple() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.reverse();
        assertArrayEquals(new float[]{1.0f, 5.0f}, list.toArray());
    }

    @Test public void createListAndReverseFirst() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.reverse();
        assertArrayEquals(new float[]{4.0f, 8.0f}, list.get(0));
        assertArrayEquals(new float[]{1.0f, 5.0f}, list.get(-1));
    }

    @Test public void addElementsWithAddAllWithResize() {
        CoordArrayList list = new CoordArrayList(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f});
        list.addAll(new float[]{11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f, 17.0f, 18.0f, 19.0f, 20.0f});
        assertArrayEquals(new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f, 17.0f, 18.0f, 19.0f, 20.0f}, list.toArray());
    }

    @Test public void getElementFromBehind() {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        assertArrayEquals(new float[]{3.0f, 7.0f}, list.get(-2));
    }

    @Test
    void testAddNeedToResize(){
        CoordArrayList list = new CoordArrayList(1);
        list.add(1f,1f);
        list.add(2f,2f);
        assertEquals(2, list.size());
        assertEquals(2, list.capacity());
    }

    @Test
    void testWriteAndRead() throws IOException {
        CoordArrayList list = new CoordArrayList();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        CoordArrayList list2 = new CoordArrayList();
        String path = TestUtilities.getTestFilesPath() + "CoordArrayListTest.testWriteAndRead";
        list.write(path);
        list2.read(path);
        TestUtilities.deleteFile(path);
        assertEquals(list, list2);
    }

    @Test
    void testConstructorWithList(){
        float[] array = new float[]{1f,1f,2f,2f};
        CoordArrayList list = new CoordArrayList(array);
        assertArrayEquals(array, list.toArray());
    }

    @Test
    void testConstructorWithInitSize() {
        CoordArrayList list = new CoordArrayList(5);
        assertEquals(5, list.capacity());
    }

    @Test
    void testEqualsSame(){
        CoordArrayList list = new CoordArrayList();
        assertEquals(list, list);
    }

    @Test
    void testEqualsOtherIsNull(){
        CoordArrayList list = new CoordArrayList();
        assertNotEquals(list, null);
    }

    @Test
    void testEqualsDifferentObject() {
        CoordArrayList list = new CoordArrayList();
        assertNotEquals(list, new Object());
    }

    @Test
    void testEqualsShouldNotEqualInLon() {
        CoordArrayList list = new CoordArrayList();
        CoordArrayList list2 = new CoordArrayList();
        list.add(1f,1f);
        list2.add(1f,2f);
        assertNotEquals(list, list2);
    }

    @Test
    void testEqualsShouldNotEqualInLat() {
        CoordArrayList list = new CoordArrayList();
        CoordArrayList list2 = new CoordArrayList();
        list.add(1f,1f);
        list2.add(2f,1f);
        assertNotEquals(list, list2);
    }

    @Test
    void testEqualsDifferentSize() {
        CoordArrayList list = new CoordArrayList();
        CoordArrayList list2 = new CoordArrayList();
        list.add(1f,1f);
        assertNotEquals(list, list2);
    }


}