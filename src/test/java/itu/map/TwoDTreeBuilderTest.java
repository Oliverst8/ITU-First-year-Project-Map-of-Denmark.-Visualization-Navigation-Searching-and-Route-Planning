package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.TwoDTreeBuilder;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    void buildTree() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{0, 0, 1, 1, 2,2, 3, 3, 4, 4, 5, 5});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] actual = builder.getTree();
        int[] expected = new int[]{2,0,4,-1,1,3,5};
        assertArrayEquals(expected, actual);
    }

    @Test
    void buildTree2() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{0, 0, 1, 1, 2,2, 3, 3, 4, 4, 5, 5,6,6,7,7});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] actual = builder.getTree();
        int[] expected = new int[]{3,1,5,0,2,4,6,-1,-1,-1,-1,-1,-1,-1,7};
        assertArrayEquals(expected, actual);
    }

//    @Test
//    void buildTreeChildShouldBeToTTheRight() {
//        CoordArrayList coordArrayList = new CoordArrayList(new float[]{1,1,2,0});
//        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
//        builder.build();
//
//        int[] actual = builder.getTree();
//        int[] expected = new int[]{0,-1,1};
//        assertArrayEquals(expected, actual);
//    }

//    @Test
//    void buildTreeChildShouldBeToTTheLeft() {
//        CoordArrayList coordArrayList = new CoordArrayList(new float[]{1,1,0,2});
//        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
//        builder.build();
//
//        int[] actual = builder.getTree();
//        int[] expected = new int[]{0,1,-1};
//        assertArrayEquals(expected, actual);
//    }

    @Test
    void buildTree3() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{5, 5, 7, 7, 3,3, 2, 2, 0, 0, 4, 4,6,6,1,1});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] actual = builder.getTree();
        int[] expected = new int[]{2,7,0,4,3,5,6,-1,-1,-1,-1,-1,-1,1,-1};
        assertArrayEquals(expected, actual);
    }


    @Test
    void buildTree4() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{2,2,3,3,1,1});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] actual = builder.getTree();
        int[] expected = new int[]{0,2,1};
        assertArrayEquals(expected, actual);
    }

    @Test
    void buildTree5() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{2,2,3,3});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] actual = builder.getTree();
        int[] expected = new int[]{0,-1,1};
        assertArrayEquals(expected, actual);
    }


}