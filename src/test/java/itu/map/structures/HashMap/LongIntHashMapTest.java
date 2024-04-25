package itu.map.structures.HashMap;

import org.junit.jupiter.api.Test;

import dk.itu.map.structures.HashMaps.LongIntHashMap;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LongIntHashMapTest {
    @Test void addOneMillionSmallNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 1; i < 1_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(1_000_000-1, map.size());
    }

    @Test void addOneMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(1_000_000, map.size());
    }

    @Test void addTwentyMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 20_000_000; i++) {
            map.put(i, i);
        }

        assertEquals(20_000_000, map.size());
    }

    @Test void addAndRemoveOneMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.put(i, i);
        }

        for (int i = 30_000_000; i < 30_000_000 + 1_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test void addAndRemoveTenMillionLargeNumbers() {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 30_000_000; i < 30_000_000 + 10_000_000; i++) {
            map.put(i, i);
        }

        for (int i = 30_000_000; i < 30_000_000 + 10_000_000; i++) {
            map.remove(i);
        }

        assertEquals(0, map.size());
    }

    @Test void testContainsKey283509867() {
        LongIntHashMap map = new LongIntHashMap();
        assertFalse(map.containsKey(283509867));
    }

    @Test void testContainsKey0(){
        LongIntHashMap map = new LongIntHashMap();
        assertThrows(IllegalArgumentException.class,() -> {
            map.containsKey(0);
        });
    }

    @Test void testEquals() {
        LongIntHashMap map = new LongIntHashMap();
        LongIntHashMap map2 = new LongIntHashMap();
        for(int i = 1; i < 1_000_000; i++){
            map.put(i,i);
            map2.put(i,i);
        }
        assertEquals(map,map2);
    }

    @Test void testWriteAndRead() throws IOException {
        LongIntHashMap map = new LongIntHashMap();
        for (int i = 1; i < 10000; i++) {
            map.put(i, i);
        }
        String path = "src/test/java/itu/map/testFiles/testWriteAndReadOfHashMapInt.txt";
        map.write(path);
        LongIntHashMap map2 = new LongIntHashMap();
        map2.read(path);
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (FileSystemException e) {}
        assertEquals(map, map2);
    }

    @Test void getElement() {
        LongIntHashMap map = new LongIntHashMap();

        map.put(1, 1);
        map.put(2, 2);

        assertEquals(1, map.get(1));
    }

    @Test void getKeys() {
        LongIntHashMap map = new LongIntHashMap();

        map.put(1, 1);
        map.put(2, 2);

        assertArrayEquals(new long[]{1, 2}, map.getKeys());
    }

    @Test void getValues() {
        LongIntHashMap map = new LongIntHashMap();

        map.put(1, 1);
        map.put(2, 2);

        assertArrayEquals(new int[]{1, 2}, map.getValues());
    }
}
