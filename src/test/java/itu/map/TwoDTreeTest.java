package itu.map;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.TwoDTree;
import dk.itu.map.structures.TwoDTreeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class TwoDTreeTest {

    @Test
    void testNearestNeighborRootIsIt() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{0, 0, 1, 1, 2,2});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        TwoDTree twoDtree = new TwoDTree();
        int[] tree = builder.getTree();
        for(int i = 0; i < tree.length; i++){
            twoDtree.add(coordArrayList.get(tree[i]));
        }
        int nearest = twoDtree.nearestNeighbour(new float[]{1, 1});
        assertEquals(tree[0], nearest);
    }

    @Test
    void testNearestNeighborRootIsNotItGoOneDown() {
        CoordArrayList coordArrayList = new CoordArrayList(new float[]{0, 0, 1, 1, 2,2});
        TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        TwoDTree twoDtree = new TwoDTree();
        int[] tree = builder.getTree();
        for(int i = 0; i < tree.length; i++){
            twoDtree.add(coordArrayList.get(tree[i]));
        }
        int nearest = twoDtree.nearestNeighbour(new float[]{0, 0});
        assertEquals(tree[1], nearest);
    }

    @Test
    void testNearestNeighborRootIsNotItGoTwoDown() {
        //CoordArrayList coordArrayList = new CoordArrayList(new float[]{0, 0, 1, 1, 2,2, 3, 3, 4, 4, 5, 5});
        TwoDTree twoDtree = new TwoDTree(new float[]{2,2,1,1,3,3,0,0,-1,-1,4,4,5,5});
        /*TwoDTreeBuilder builder = new TwoDTreeBuilder(coordArrayList);
        builder.build();
        int[] tree = builder.getTree();
        TwoDTree twoDtree = new TwoDTree(tree.length);
        for(int i = 0; i < tree.length; i++){
            twoDtree.add(coordArrayList.get(tree[i]));
        }*/
        int nearest = twoDtree.nearestNeighbour(new float[]{3.5f, 4});
        assertEquals(5, nearest);
    }

    @Test
    void testNearestNeighbor(){
        TwoDTree twoDtree = new TwoDTree(new float[]{5,4,2,6,13,3,3,1,-1,-1,10,2,8,7});
        int nearest = twoDtree.nearestNeighbour(9,4);
        assertEquals(5,nearest);
    }

    @Test
    void testNearestNeighborWhereItWouldHitMinus1() {
        TwoDTree twoDtree = new TwoDTree(new float[]{5,4,2,2,13,3,3,1,-1,-1,10,2,8,7});
        int nearest = twoDtree.nearestNeighbour(3,3);
        float[] expected = new float[]{3,1};
        float[] actual = twoDtree.get(nearest);
        assertArrayEquals(actual, expected);
    }

}
