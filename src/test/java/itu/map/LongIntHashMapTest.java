package itu.map;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.HashMaps.LongIntHashMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LongIntHashMapTest {
    @Test public void addOneMillionSmallNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 1; i < 1_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(1_000_000-1, map.size());
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

    @Test
    void testContainsKey283509867() {
        LongIntHashMap map = new LongIntHashMap();
        assertFalse(map.containsKey(283509867));
    }

    @Test
    void testContainsKey0(){
        LongIntHashMap map = new LongIntHashMap();
        assertThrows(IllegalArgumentException.class,() -> {
            map.containsKey(0);
        });
    }

    @Test
    void testEquals() {
        LongIntHashMap map = new LongIntHashMap();
        LongIntHashMap map2 = new LongIntHashMap();
        for(int i = 1; i < 1_000_000; i++){
            map.put(i,i);
            map2.put(i,i);
        }
        assertEquals(map,map2);
    }

    @Test
    void testWriteAndRead() throws IOException {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 1; i < 10000; i++) {
            map.put(i, i);
        }
        String path = "src/test/java/itu/map/testFiles/testWriteAndReadOfHashMapInt.txt";
        map.write(path);
        LongIntHashMap map2 = new LongIntHashMap();
        map2.read(path);
        Files.deleteIfExists(Paths.get(path));
        assertEquals(map, map2);
    }
}
