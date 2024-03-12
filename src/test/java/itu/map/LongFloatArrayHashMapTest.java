package itu.map;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.LongFloatArrayHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LongFloatArrayHashMapTest {
    @Test public void addOneMillionSmallNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 0; i < 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addOneMillionLargeNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addTwentyMillionLargeNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        assertEquals(20_000_000, map.size());
    }

    @Test public void addAndRemoveOneMillionLargeNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 1_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test public void addAndRemoveTenMillionLargeNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 10_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test public void addAndRemoveTwentyMillionLargeNumbers() {
        LongFloatArrayHashMap map = new LongFloatArrayHashMap();
        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.put(i, new float[] {i, i});
        }

        for (long i = 30_000_000_000l; i < 30_000_000_000l + 20_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }
}
