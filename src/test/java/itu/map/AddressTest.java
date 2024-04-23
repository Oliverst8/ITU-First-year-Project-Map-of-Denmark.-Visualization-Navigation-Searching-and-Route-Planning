package itu.map;

import dk.itu.map.structures.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
