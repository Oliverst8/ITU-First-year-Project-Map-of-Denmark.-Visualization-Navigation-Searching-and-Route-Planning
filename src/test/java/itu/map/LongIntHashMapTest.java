package itu.map;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.LongIntHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class LongIntHashMapTest {
    @Test public void addOneMillionSmallNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 0; i < 1_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addOneMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(1_000_000, map.size());
    }

    @Test public void addTwentyMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 20_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(20_000_000, map.size());
    }

    @Test public void addAndRemoveOneMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.put(i, i);
        }

        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test public void addAndRemoveTenMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 10_000_000; i++) {
            map.put(i, i);
        }

        for (int i = 30_000_000; i < 30_000_000 + 10_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }
}
