package itu.map;
import dk.itu.map.structures.ArrayLists.CoordArrayListV2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CoordArrayListV2Test {
    @Test public void createListAndAdd() {
        CoordArrayListV2 list = new CoordArrayListV2();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListWithArray() {
        CoordArrayListV2 list = new CoordArrayListV2(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f});
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndGet() {
        CoordArrayListV2 list = new CoordArrayListV2();
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
        CoordArrayListV2 list = new CoordArrayListV2();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.exchange(0, 1);
        assertArrayEquals(new float[]{2.0f, 6.0f, 1.0f, 5.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndAddAll() {
        CoordArrayListV2 list = new CoordArrayListV2();
        list.addAll(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f});
        assertArrayEquals(new float[]{1.0f, 5.0f, 2.0f, 6.0f, 3.0f, 7.0f, 4.0f, 8.0f}, list.toArray());
    }

    @Test public void createListAndReverseOddList() {
        CoordArrayListV2 list = new CoordArrayListV2();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.add(5.0f, 10.0f);
        list.reverse();
        assertArrayEquals(new float[]{5.0f, 10.0f, 4.0f, 8.0f, 3.0f, 7.0f, 2.0f, 6.0f, 1.0f, 5.0f}, list.toArray());
    }

    @Test public void createListAndReverseEvenList() {
        CoordArrayListV2 list = new CoordArrayListV2();
        list.add(1.0f, 5.0f);
        list.add(2.0f, 6.0f);
        list.add(3.0f, 7.0f);
        list.add(4.0f, 8.0f);
        list.reverse();
        assertArrayEquals(new float[]{4.0f, 8.0f, 3.0f, 7.0f, 2.0f, 6.0f, 1.0f, 5.0f}, list.toArray());
    }

    @Test public void createListAndReverseTuple() {
        CoordArrayListV2 list = new CoordArrayListV2();
        list.add(1.0f, 5.0f);
        list.reverse();
        assertArrayEquals(new float[]{1.0f, 5.0f}, list.toArray());
    }
}