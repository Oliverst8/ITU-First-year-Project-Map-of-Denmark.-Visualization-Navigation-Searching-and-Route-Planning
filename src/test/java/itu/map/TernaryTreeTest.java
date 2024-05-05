package itu.map;

import dk.itu.map.structures.TernaryTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class TernaryTreeTest {
    TernaryTree address;
    @BeforeEach
    public void setUp(){
        address = new TernaryTree();
    }

    public void addAdresses(){
        address.addStreetName(new String[]{"ab","1","1234","cph"},1f,1f);
        address.addStreetName(new String[]{"ad","1","1234","cph"},3f,1f);
        address.addStreetName(new String[]{"ja","1","1234","cph"},1f,7f);
        address.addStreetName(new String[]{"jd","1","1234","cph"},4f,1f);
        address.addStreetName(new String[]{"hv","1","1234","cph"},2f,3f);
        address.addStreetName(new String[]{"hvi","1","1234","cph"},12f,14f);
        address.addStreetName(new String[]{"he","1","1234","cph"},1f,1f);

        Thread thread = new Thread(address);
        thread.start();
        try {
            Thread.sleep(50);
            address.finish();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRun(){
        addAdresses();
        System.out.println("");
        assertTrue(true);
    }

    @Test
    void testAutoCompleteMatchingStringFound(){
        addAdresses();
        List<TernaryTree.searchAddress> result_array = address.autoComplete("hv",10);
        List<String> expected_array = new ArrayList<>(List.of("hv: 1234", "hvi: 1234"));

        assertEquals(result_array.toString(), expected_array.toString());
    }

    @Test
    void testAutoComplete1() {
        addAdresses();
        List<TernaryTree.searchAddress> result_array = address.autoComplete("a", 10);
        List<String> expected_array = new ArrayList<>(List.of("ab: 1234", "ad: 1234"));
        assertEquals(result_array.toString(), expected_array.toString());
    }

    @Test
    void testAutoComplete2() {
        addAdresses();
        List<TernaryTree.searchAddress> result_array = address.autoComplete("ol", 10);
        List<String[]> expected_array = new ArrayList<>();
        assertEquals(result_array.toString(), expected_array.toString());
    }

    @Test
    void testAutoCompleteMatchingStringNotFound(){
        addAdresses();
        List<TernaryTree.searchAddress> result_array = address.autoComplete("h",10);
        List<String> expected_array = new ArrayList<>(List.of("hv: 1234","he: 1234","hvi: 1234"));
        assertEquals(result_array.toString(), expected_array.toString());
    }

    @Test
    void testEqualsSame(){
        addAdresses();
        assertEquals(address, address);
    }

    @Test
    void testEqualsDifferent(){
        addAdresses();
        TernaryTree newAddress = new TernaryTree();
        assertNotEquals(address, newAddress);
    }

    @Test
    void testEqualsShouldEqual(){
        addAdresses();
        TernaryTree newAddress = new TernaryTree();
        newAddress.addStreetName(new String[]{"ab","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"ad","1","1234","cph"},3f,1f);
        newAddress.addStreetName(new String[]{"ja","1","1234","cph"},1f,7f);
        newAddress.addStreetName(new String[]{"jd","1","1234","cph"},4f,1f);
        newAddress.addStreetName(new String[]{"hv","1","1234","cph"},2f,3f);
        newAddress.addStreetName(new String[]{"hvi","1","1234","cph"},12f,14f);
        newAddress.addStreetName(new String[]{"he","1","1234","cph"},1f,1f);

        Thread thread = new Thread(newAddress);
        thread.start();
        try {
            Thread.sleep(50);
            newAddress.finish();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(address, newAddress);
    }

    @Test
    void testWriteAndRead() throws IOException {
        addAdresses();
        String testPath = TestUtilities.getTestFilesPath();
        address.write(testPath + "addressTest");
        TernaryTree newAddress = new TernaryTree();
        newAddress.read(testPath + "addressTest");
        //assertEquals(address, newAddress);
        assertTrue(address.equals(newAddress));
    }

    @Test
    void testAutocomplete3() {
        address.addStreetName(new String[]{"Ballafletcher Road 17 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 12 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 13 Cronkbourne","1","2","3"},1f,1f);
        address.run();
        List<TernaryTree.searchAddress> result = address.autoComplete("Ballafletcher", 20);
        List<String[]> expected = new ArrayList<>(List.of(new String[]{"Ballafletcher Road 17 Cronkbourne","2"}, new String[]{"Ballafletcher Road 12 Cronkbourne","2"}, new String[]{"Ballafletcher Road 13 Cronkbourne","2"}));
        assertEquals(result, expected);
    }

    @Test
    void testAutocomplete(){
        Random random = new Random();
        for(int i = 0; i < 1000; i++){
            StringBuilder streetName = new StringBuilder();
            for(int j = 0; j < 10; j++){
                streetName.append((char) ('a' + random.nextInt(26)));
            }
            address.addStreetName(new String[]{streetName.toString(),"1","2","3"},1f,1f);
        }

        address.addStreetName(new String[]{"Ballafletcher Road 17 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 12 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 13 Cronkbourne","1","2","3"},1f,1f);
        address.run();
        List<TernaryTree.searchAddress> result = address.autoComplete("Ballafletcher", 20);
        List<String[]> expected = new ArrayList<>(List.of(new String[]{"Ballafletcher Road 17 Cronkbourne","2"}, new String[]{"Ballafletcher Road 12 Cronkbourne","2"}, new String[]{"Ballafletcher Road 13 Cronkbourne","2"}));
        assertEquals(result, expected);
    }

//    @Test
//    void testFillAddressExpectsMultiple() {
//        Random random = new Random();
//        for(int i = 0; i < 1000; i++){
//            StringBuilder streetName = new StringBuilder();
//            for(int j = 0; j < 10; j++){
//                streetName.append((char) ('a' + random.nextInt(26)));
//            }
//            address.addStreetName(new String[]{streetName.toString(),"1","2","3"},1f,1f);
//        }
//        address.addStreetName(new String[]{"Ballafletcher Road","1","23","Herlev"},1f,1f);
//        address.addStreetName(new String[]{"Ballafletcher Road","2","23","Herlev"},1f,1f);
//        address.addStreetName(new String[]{"Ballafletcher Road","3","23","Islev"},1f,1f);
//        address.run();
//
//        List<TernaryTree.searchAddress> map = address.autoComplete("Ballafletcher", 20);
//        TernaryTree.searchAddress autocomplete = map.iterator().next();
//
//        List<String[]> result = address.fillAddress(autocomplete, map.get(0));
//        List<String[]> expected = new ArrayList<>(List.of(new String[]{"Ballafletcher Road","1","23","Herlev"}, new String[]{"Ballafletcher Road","2","23","Herlev"}, new String[]{"Ballafletcher Road","3","23","Islev"}));
//
//        assertEquals(result, expected);
//    }


}
