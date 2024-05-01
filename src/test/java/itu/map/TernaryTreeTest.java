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

    private static void assertStringArraySetEquals(Set<String[]> result_array, Set<String[]> expected_arrray) {
        Set<List<String>> result = new HashSet<>();
        for(String[] key : result_array){
            List<String> list = new ArrayList<>(Arrays.asList(key));
            result.add(list);
        }
        Set<List<String>> expected = new HashSet<>();
        for(String[] key : expected_arrray){
            List<String> list = new ArrayList<>(Arrays.asList(key));
            expected.add(list);
        }
        assertEquals(expected,result);
    }

    public void addAdresses(){
        address.addStreetName(new String[]{"ab","1","1234","cph"},1,1);
        address.addStreetName(new String[]{"ad","1","1234","cph"},3f,1f);
        address.addStreetName(new String[]{"ja","1","1234","cph"},1f,7f);
        address.addStreetName(new String[]{"jd","1","1234","cph"},4f,1f);
        address.addStreetName(new String[]{"hv","1","1234","cph"},2f,3f);
        address.addStreetName(new String[]{"hvi","1","1234","cph"},12f,14f);
        address.addStreetName(new String[]{"he","1","1234","cph"},1f,1f);
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
        Map<String[], TernaryTree.AddressNode> result_array = address.autoComplete("hv",10);
        Set<String[]> expected_arrray = new HashSet<>(List.of(new String[]{"hv", "1234"}, new String[]{"hvi","1234"}));
        assertStringArraySetEquals(result_array.keySet(), expected_arrray);

    }

    @Test
    void testAutoComplete1() {
        addAdresses();
        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("a", 10);
        Set<String[]> expected = new HashSet<>(List.of(new String[]{"ab","1234"}, new String[]{"ad","1234"}));
        assertStringArraySetEquals(result.keySet(), expected);
    }

    @Test
    void testAutoComplete2() {
        addAdresses();
        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("ol", 10);
        Set<String[]> expected = new HashSet<>();
        assertStringArraySetEquals(result.keySet(), expected);
    }

    @Test
    void testAutoCompleteMatchingStringNotFound(){
        addAdresses();
        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("h",10);
        Set<String[]> expected = new HashSet<>(List.of(new String[]{"hv","1234"},new String[]{"hvi","1234"},new String[]{"he","1234"}));
        assertStringArraySetEquals(result.keySet(), expected);
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
        newAddress.addStreetName(new String[]{"ad","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"ja","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"jd","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"hv","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"hvi","1","1234","cph"},1f,1f);
        newAddress.addStreetName(new String[]{"he","1","1234","cph"},1f,1f);
        newAddress.run();
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
        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("Ballafletcher", 20);
        Set<String[]> expected = new HashSet<>(List.of(new String[]{"Ballafletcher Road 17 Cronkbourne","2"}, new String[]{"Ballafletcher Road 12 Cronkbourne","2"}, new String[]{"Ballafletcher Road 13 Cronkbourne","2"}));
        assertStringArraySetEquals(result.keySet(), expected);
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
        Map<String[], TernaryTree.AddressNode> result = address.autoComplete("Ballafletcher", 20);
        Set<String[]> expected = new HashSet<>(List.of(new String[]{"Ballafletcher Road 17 Cronkbourne","2"}, new String[]{"Ballafletcher Road 12 Cronkbourne","2"}, new String[]{"Ballafletcher Road 13 Cronkbourne","2"}));
        assertStringArraySetEquals(result.keySet(), expected);
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
        address.run();

        Map<String[], TernaryTree.AddressNode> map = address.autoComplete("Ballafletcher", 20);
        String[] autocomplete = map.keySet().iterator().next();

        List<String[]> result = address.fillAddress(autocomplete, map.get(autocomplete));
        Set<String[]> expected = new HashSet<>(List.of(new String[]{"Ballafletcher Road","1","23","Herlev"}, new String[]{"Ballafletcher Road","2","23","Herlev"}, new String[]{"Ballafletcher Road","3","23","Islev"}));

        assertStringArraySetEquals(new HashSet<>(result), expected);

    }


}
