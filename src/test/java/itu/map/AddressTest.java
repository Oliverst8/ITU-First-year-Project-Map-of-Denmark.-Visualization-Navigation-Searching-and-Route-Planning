package itu.map;

import dk.itu.map.structures.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class AddressTest {
    Address address;
    @BeforeEach
    public void setUp(){
        address = new Address();
    }

    public void addAdresses(){
        address.addStreetName("ab");
        address.addStreetName("ad");
        address.addStreetName("ja");
        address.addStreetName("jd");
        address.addStreetName("hv");
        address.addStreetName("hvi");
        address.addStreetName("he");
        address.run();
    }

    @Test
    public void testRun(){
        addAdresses();
        Address.InformationNode node = address.search("hv");
        System.out.println("");
    }

    @Test
    void testAutoCompleteMatchingStringFound(){
        addAdresses();
        Set<String> result = address.autoComplete("hv",10);
        assertEquals(2,result.size());
        assertTrue(result.contains("hv"));
        assertTrue(result.contains("hvi"));
    }

    @Test
    void testAutoCompleteMatchingStringNotFound(){
        addAdresses();
        Set<String> result = address.autoComplete("h",10);
        assertEquals(3,result.size());
        assertTrue(result.contains("hv"));
        assertTrue(result.contains("hvi"));
    }

    @Test
    void testEqualsSame(){
        addAdresses();
        assertEquals(address, address);
    }

    @Test
    void testEqualsDifferent(){
        addAdresses();
        Address newAddress = new Address();
        assertNotEquals(address, newAddress);
    }

    @Test
    void testEqualsShouldEqual(){
        addAdresses();
        Address newAddress = new Address();
        newAddress.addStreetName("ab");
        newAddress.addStreetName("ad");
        newAddress.addStreetName("ja");
        newAddress.addStreetName("jd");
        newAddress.addStreetName("hv");
        newAddress.addStreetName("hvi");
        newAddress.addStreetName("he");
        newAddress.run();
        assertEquals(address, newAddress);
    }

    @Test
    void testWriteAndRead() throws IOException {
        addAdresses();
        String testPath = TestUtilities.getTestFilesPath();
        address.write(testPath + "addressTest");
        Address newAddress = new Address();
        newAddress.read(testPath + "addressTest");
        assertEquals(address, newAddress);
    }
}
