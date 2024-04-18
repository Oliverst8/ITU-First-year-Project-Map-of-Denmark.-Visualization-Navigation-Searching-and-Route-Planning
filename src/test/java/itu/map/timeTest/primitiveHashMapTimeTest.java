package itu.map.timeTest;

import dk.itu.map.structures.HashMaps.LongIntHashMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class primitiveHashMapTimeTest {
/**
    int start = 1;

    @Test
    void testDefaultHashMapContains1millionNumbers(){
        HashMap<Long, Integer> map = new HashMap<>();
        for (int i = start; i < 1_000_000; i++) {
            map.put((long)i, i);
        }
        for (long i = start; i < 1_000_000L; i++) {
            assertTrue(map.containsKey((i)));
        }
        for(int i = start; i < 1_000_000; i++){
            assertEquals(i, map.get((long)i));
        }
        for(int i = start; i < 1_000_000; i++){
            map.remove((long)i);
        }
        for(int i = start; i < 1_000_000; i++){
            assertFalse(map.containsKey((long)i));
        }
    }

    @Test
    void testLongIntHashMapContains1MillionNumbers(){
        LongIntHashMap map = new LongIntHashMap();
        for (int i = start; i < 1_000_000; i++) {
            map.put(i, i);
        }
        for (long i = start; i < 1_000_000L; i++) {
            assertTrue(map.containsKey(i));
        }
        for(int i = start; i < 1_000_000; i++){
            assertEquals(i, map.get(i));
        }
        for(int i = start; i < 1_000_000; i++){
            map.remove(i);
        }
        for(int i = start; i < 1_000_000; i++){
            assertFalse(map.containsKey(i));
        }
    }
**/
}
