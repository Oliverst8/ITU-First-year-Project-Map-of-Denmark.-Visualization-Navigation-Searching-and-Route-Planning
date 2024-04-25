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
        address.addStreetName(new String[]{"ab","1","1234","cph"});
        address.addStreetName(new String[]{"ad","1","1234","cph"});
        address.addStreetName(new String[]{"ja","1","1234","cph"});
        address.addStreetName(new String[]{"jd","1","1234","cph"});
        address.addStreetName(new String[]{"hv","1","1234","cph"});
        address.addStreetName(new String[]{"hvi","1","1234","cph"});
        address.addStreetName(new String[]{"he","1","1234","cph"});
        address.run();
    }

    @Test
    public void testRun(){
        addAdresses();
        System.out.println("");
    }

    @Test
    void testAutoCompleteMatchingStringFound(){
        addAdresses();
        Set<String> result = address.autoComplete("hv",10);
        Set<String> expected = new HashSet<>(List.of("hv","hvi"));
        assertEquals(expected,result);
    }

    @Test
    void testAutoComplete1() {
        addAdresses();
        Set<String> result = address.autoComplete("a", 10);
        Set<String> expected = new HashSet<>(List.of("ab", "ad"));
        assertEquals(expected, result);
    }

    @Test
    void testAutoComplete2() {
        addAdresses();
        Set<String> result = address.autoComplete("ol", 10);
        Set<String> expected = new HashSet<>();
        assertEquals(expected, result);
    }

    @Test
    void testAutoCompleteMatchingStringNotFound(){
        addAdresses();
        Set<String> result = address.autoComplete("h",10);
        Set<String> expected = new HashSet<>(List.of("hv","hvi","he"));
        assertEquals(expected,result);
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
        newAddress.addStreetName(new String[]{"ab","1","1234","cph"});
        newAddress.addStreetName(new String[]{"ad","1","1234","cph"});
        newAddress.addStreetName(new String[]{"ja","1","1234","cph"});
        newAddress.addStreetName(new String[]{"jd","1","1234","cph"});
        newAddress.addStreetName(new String[]{"hv","1","1234","cph"});
        newAddress.addStreetName(new String[]{"hvi","1","1234","cph"});
        newAddress.addStreetName(new String[]{"he","1","1234","cph"});
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

    @Test
    void testAutocomplete3() {
        address.addStreetName(new String[]{"Ballafletcher Road 17 Cronkbourne","1","2","3"});
        address.addStreetName(new String[]{"Ballafletcher Road 12 Cronkbourne","1","2","3"});
        address.addStreetName(new String[]{"Ballafletcher Road 13 Cronkbourne","1","2","3"});
        address.run();
        Set<String> result = address.autoComplete("Ballafletcher", 20);
        Set<String> expected = new HashSet<>(List.of("Ballafletcher Road 17 Cronkbourne", "Ballafletcher Road 12 Cronkbourne", "Ballafletcher Road 13 Cronkbourne"));
        assertEquals(expected, result);
    }

    @Test
    void testAutocomplete(){
        Random random = new Random();
        for(int i = 0; i < 1000; i++){
            StringBuilder streetName = new StringBuilder();
            for(int j = 0; j < 10; j++){
                streetName.append((char) ('a' + random.nextInt(26)));
            }
            address.addStreetName(new String[]{streetName.toString(),"1","2","3"});
        }

        address.addStreetName(new String[]{"Ballafletcher Road 17 Cronkbourne","1","2","3"});
        address.addStreetName(new String[]{"Ballafletcher Road 12 Cronkbourne","1","2","3"});
        address.addStreetName(new String[]{"Ballafletcher Road 13 Cronkbourne","1","2","3"});
        address.run();
        Set<String> result = address.autoComplete("Ballafletcher", 20);
        Set<String> expected = new HashSet<>(List.of("Ballafletcher Road 17 Cronkbourne", "Ballafletcher Road 12 Cronkbourne", "Ballafletcher Road 13 Cronkbourne"));
        assertEquals(expected, result);
    }
}
