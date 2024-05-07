package itu.map;

import dk.itu.map.structures.Point;
import dk.itu.map.structures.TernaryTree;
import javafx.scene.paint.Color;
import org.apache.commons.io.FileUtils;
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
        FileUtils.delete(new java.io.File(testPath + "addressTest"));
        //assertEquals(address, newAddress);
        assertTrue(address.equals(newAddress));
    }

    @Test
    void testAutocomplete3() {
        address.addStreetName(new String[]{"Ballafletcher Road 17 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 12 Cronkbourne","1","2","3"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road 13 Cronkbourne","1","2","3"},1f,1f);
        address.finish();
        address.run();
        List<TernaryTree.searchAddress> result = address.autoComplete("Ballafletcher", 20);
        List<String> expected = new ArrayList<>(List.of("Ballafletcher Road 17 Cronkbourne: 2","Ballafletcher Road 12 Cronkbourne: 2", "Ballafletcher Road 13 Cronkbourne: 2"));
        assertEquals(result.toString(), expected.toString());
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
        address.finish();
        address.run();
        List<TernaryTree.searchAddress> result = address.autoComplete("Ballafletcher", 20);
        List<String> expected = new ArrayList<>(List.of("Ballafletcher Road 17 Cronkbourne: 2", "Ballafletcher Road 12 Cronkbourne: 2", "Ballafletcher Road 13 Cronkbourne: 2"));
        assertEquals(result.toString(), expected.toString());
    }

    @Test
    void testFillAddressExpectsMultiple() {
        Random random = new Random();
        for(int i = 0; i < 1000; i++){
            StringBuilder streetName = new StringBuilder();
            for(int j = 0; j < 10; j++){
                streetName.append((char) ('a' + random.nextInt(26)));
            }
            address.addStreetName(new String[]{streetName.toString(),"1","2","3"},1f,1f);
        }
        address.addStreetName(new String[]{"Ballafletcher Road","1","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road","2","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road","3","23","Islev"},1f,1f);

        address.finish();
        address.run();

        List<TernaryTree.searchAddress> list = address.autoComplete("Ballafletcher", 20);
        TernaryTree.searchAddress autocomplete = list.iterator().next();
        List<TernaryTree.searchAddress> result = address.fillAddress(new String[]{autocomplete.streetName, autocomplete.zip}, autocomplete.node, "Ballafletcher Road");
        List<TernaryTree.searchAddress> expected = new ArrayList<>();
        TernaryTree.searchAddress expected1 = new TernaryTree.searchAddress("Ballafletcher Road","23",autocomplete.node);
        expected1.streetNumber = "1";
        expected1.city = "Herlev";
        expected1.point = new Point(1f,1f,"navigation", Color.PURPLE);
        expected.add(expected1);
        TernaryTree.searchAddress expected2 = new TernaryTree.searchAddress("Ballafletcher Road","23",autocomplete.node);
        expected2.streetNumber = "2";
        expected2.city = "Herlev";
        expected2.point = new Point(1f,1f,"navigation", Color.PURPLE);
        expected.add(expected2);
        TernaryTree.searchAddress expected3 = new TernaryTree.searchAddress("Ballafletcher Road","23",autocomplete.node);
        expected3.streetNumber = "3";
        expected3.city = "Islev";
        expected3.point = new Point(1f,1f,"navigation", Color.PURPLE);
        expected.add(expected3);
        assertEquals(expected, result);
    }

    @Test
    void testCheckIfShouldSuggest(){
        Random random = new Random();
        for(int i = 0; i < 1000; i++){
            StringBuilder streetName = new StringBuilder();
            for(int j = 0; j < 10; j++){
                streetName.append((char) ('a' + random.nextInt(26)));
            }
            address.addStreetName(new String[]{streetName.toString(),"1","2","3"},1f,1f);
        }
        address.addStreetName(new String[]{"Ballafletcher Road","1","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road","2","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Ballafletcher Road","3","23","Islev"},1f,1f);

        address.finish();
        address.run();

        List<TernaryTree.searchAddress> list = address.autoComplete("Ballafletcher", 20);
        TernaryTree.searchAddress autocomplete = list.iterator().next();
        List<TernaryTree.searchAddress> result = address.fillAddress(new String[]{autocomplete.streetName, autocomplete.zip}, autocomplete.node, "Ballafletcher Road 1");
        List<TernaryTree.searchAddress> expected = new ArrayList<>();
        TernaryTree.searchAddress expected1 = new TernaryTree.searchAddress("Ballafletcher Road","23",autocomplete.node);
        expected1.streetNumber = "1";
        expected1.city = "Herlev";
        expected1.point = new Point(1f,1f,"navigation", Color.PURPLE);
        expected.add(expected1);

        assertEquals(expected, result);
    }

    @Test
    void testClone(){
        address.addStreetName(new String[]{"Bella Road","1","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Beasty Road","2","24","Herlev"},1f,1f);
        address.finish();
        address.run();

        List<TernaryTree.searchAddress> list = address.autoComplete("Be", 20);
        TernaryTree.searchAddress original = list.get(0);

        TernaryTree.searchAddress clone = list.get(1);

        clone.clone(original);

        assertEquals(original.toString(), clone.toString());
    }

    @Test
    void testReset(){
        address.addStreetName(new String[]{"Bella Road","1","23","Herlev"},1f,1f);
        address.addStreetName(new String[]{"Beasty Road","2","24","Herlev"},1f,1f);
        address.finish();
        address.run();

        List<TernaryTree.searchAddress> list = address.autoComplete("Be", 20);
        TernaryTree.searchAddress original = list.get(0);

        original.reset();

        assertEquals(original.toString(), "null: null");

    }


}
