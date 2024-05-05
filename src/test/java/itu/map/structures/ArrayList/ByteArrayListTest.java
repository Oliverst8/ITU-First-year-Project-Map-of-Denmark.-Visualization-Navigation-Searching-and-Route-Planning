package itu.map.structures.ArrayList;

import dk.itu.map.structures.ArrayLists.ByteArrayList;
import itu.map.TestUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ByteArrayListTest {
    ByteArrayList byteArrayList;

    @BeforeEach
    void setup() {
        byteArrayList = new ByteArrayList();
    }
    @Test
    void testByteArrayListConstructorWithArgs() {
        byteArrayList = new ByteArrayList(5);
        assertEquals(5, byteArrayList.capacity());
    }

    @Test
    void testByteArrayListConstructorWithArgs0() {
        byteArrayList = new ByteArrayList(0);
        assertEquals(1, byteArrayList.capacity());
    }

    @Test
    void testAdd() {
        byteArrayList.add((byte) 1);
        assertEquals(1, byteArrayList.size());
        assertEquals(1, byteArrayList.get(0));
    }

    @Test
    void testAddNeedsToResize() {
        byteArrayList = new ByteArrayList(1);
        assertEquals(1, byteArrayList.capacity());
        byteArrayList.add((byte) 1);
        byteArrayList.add((byte) 1);
        assertEquals(2, byteArrayList.capacity());
    }

    @Test
    void testGet() {
        byteArrayList.add((byte) 1);
        assertEquals(1, byteArrayList.get(0));
    }

    @Test
    void testExchange() {
        byteArrayList.add((byte) 1);
        byteArrayList.add((byte) 2);
        byteArrayList.exchange(0, 1);
        assertEquals(2, byteArrayList.get(0));
        assertEquals(1, byteArrayList.get(1));
    }

    @Test
    void testEqualsSame() {
        byteArrayList.add((byte) 1);
        byteArrayList.equals(byteArrayList);
    }

    @Test
    void testEqualsShouldBeEqual() {
        ByteArrayList byteArrayList2 = new ByteArrayList();
        byteArrayList.add((byte) 1);
        byteArrayList2.add((byte) 1);
        byteArrayList.equals(byteArrayList2);
    }

    @Test
    void testEqualsShouldNotBeEqual() {
        ByteArrayList byteArrayList2 = new ByteArrayList();
        byteArrayList.add((byte) 1);
        byteArrayList2.add((byte) 2);
        byteArrayList.equals(byteArrayList2);
    }

    @Test
    void testEqualsDifferentSize(){
        ByteArrayList byteArrayList2 = new ByteArrayList();
        byteArrayList.add((byte) 1);
        byteArrayList2.add((byte) 1);
        byteArrayList2.add((byte) 2);
        byteArrayList.equals(byteArrayList2);
    }

    @Test
    void testEqualsDifferentObjectType(){
        byteArrayList.equals(new Object());
    }

    @Test
    void testWritingAndReading() throws IOException {
        byteArrayList.add((byte) 1);
        byteArrayList.add((byte) 2);
        ByteArrayList byteArrayList2 = new ByteArrayList();
        String path = TestUtilities.getTestFilesPath() + "byteArrayListTest.txt";
        byteArrayList.write(path);
        byteArrayList2.read(path);
        TestUtilities.deleteFile(path);
        byteArrayList.equals(byteArrayList2);
    }
}
