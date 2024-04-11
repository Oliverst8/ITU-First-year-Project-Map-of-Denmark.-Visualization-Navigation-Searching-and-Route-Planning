package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;
import dk.itu.map.structures.TwoDTreeBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TwoDTreeBuilderTest {

    TwoDTreeBuilder kdTreeBuilder;
    static CoordArrayList coords;

    @BeforeAll
    static void setUp() {
        coords = new CoordArrayList();
        for(int i = 0; i < 8; i++){
            coords.add(i, i);
        }

    }

    @Test
    void testCreateSubSections(){
        kdTreeBuilder = new TwoDTreeBuilder(coords);
    }

    @Test
    void testCreateKDTree() {
        CoordArrayList coords = TestUtilities.createCoordArrayList(0,0,1,1,2,2,3,3,7,7,8,8,9,9,10,10,11,11,12,12,13,13);
        kdTreeBuilder = new TwoDTreeBuilder(coords);
        kdTreeBuilder.build();
        //IntArrayList expected = new IntArrayList(new int[]{8,2,11,0,3,9,12,-1,1,-1,7,-1,10,-1,13});
        //IntArrayList acutal = kdTreeBuilder.getTreeInts();
        //CoordArrayList expected = TestUtilities.createCoordArrayList(8,8,2,2,11,11,0,0,3,3,9,9,12,12,-1,-1,1,1,-1,-1,7,7,-1,-1,10,10,-1,-1,13,13);
        int[] expected = new int[]{5,2,8,0,3,6,9,-1,1,-1,4,-1,7,-1,10};
        int[] actual = kdTreeBuilder.getTree();
        //CoordArrayList acutal = kdTreeBuilder.getTree();
        assertArrayEquals(expected, actual);
    }
}