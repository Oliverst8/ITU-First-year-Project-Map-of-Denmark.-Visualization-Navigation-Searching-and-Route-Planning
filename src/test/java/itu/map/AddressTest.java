package itu.map;

import dk.itu.map.structures.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class AddressTest {
    Address address;
    @BeforeEach
    public void setUp(){
        address = new Address();
    }
    @Test
    public void testRun(){
        address.addStreetName("ab");
        address.addStreetName("ad");
        address.addStreetName("ja");
        address.addStreetName("jd");
        address.addStreetName("hv");
        address.run();
        System.out.println("");
    }
}
