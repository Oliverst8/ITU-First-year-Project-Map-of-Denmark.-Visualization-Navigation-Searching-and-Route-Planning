package itu.map;
import dk.itu.map.structures.ArrayLists.CoordArrayList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordArrayListTest {
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
}