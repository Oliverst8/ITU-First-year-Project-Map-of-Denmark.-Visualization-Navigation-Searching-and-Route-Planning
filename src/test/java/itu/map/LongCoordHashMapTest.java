package itu.map;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.LongCoordHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LongCoordHashMapTest {
    @Test public void addOneMillionSmallNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 0; i < 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addOneMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addTwentyMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(20_000_000, map.size());
    }

    @Test public void addAndRemoveOneMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test public void addAndRemoveTenMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test public void addAndRemoveTwentyMillionLargeNumbers() {
        LongCoordHashMap map = new LongCoordHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }
}
