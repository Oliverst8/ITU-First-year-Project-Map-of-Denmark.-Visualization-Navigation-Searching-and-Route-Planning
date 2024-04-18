package itu.map.structures.HashMap;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.HashMaps.LongCoordHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LongCoordHashMapTest {
    @Test void addOneMillionSmallNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 0; i < 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test void addOneMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test void addTwentyMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(20_000_000, map.size());
    }

    @Test void addAndRemoveOneMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test void addAndRemoveTenMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test void addAndRemoveTwentyMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test void getElement() {
        LongCoordHashMap map = new LongCoordHashMap();

        map.put(1, new float[] {1, 1});
        map.put(2, new float[] {2, 2});

        assertArrayEquals(new float[] {1, 1}, map.get(1));
    }

    @Test void getElementWithHashColision() {
        LongCoordHashMap map = new LongCoordHashMap();

        map.put(1, new float[] {1, 1});
        map.put(11, new float[] {1, 1});

        assertArrayEquals(new float[] {1, 1}, map.get(11));
    }

    @Test void containsKey() {
        LongCoordHashMap map = new LongCoordHashMap();

        map.put(1, new float[] {1, 1});
        map.put(2, new float[] {2, 2});

        assertTrue(map.containsKey(1));
    }

    @Test void removeElement() {
        LongCoordHashMap map = new LongCoordHashMap();

        map.put(1, new float[] {1, 1});
        map.put(2, new float[] {2, 2});

        map.remove(1);

        assertFalse(map.containsKey(1));
    }

    @Test void removeElementWithHashColision() {
        LongCoordHashMap map = new LongCoordHashMap();

        map.put(1, new float[] {1, 1});
        map.put(11, new float[] {1, 1});
        
        map.remove(11);
        assertFalse(map.containsKey(11));
    }
}
