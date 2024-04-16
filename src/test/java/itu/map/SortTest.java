package itu.map;
import dk.itu.map.utility.Sort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {

    @Test
    void testRun2LengthArray() {
        int[] arr = {2, 1};
        Sort sort = new Sort(arr);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] result = sort.getResult();
        assertArrayEquals(new int[]{1, 2}, result);
    }

    @Test
    void testRun5LengthArray() {
        int[] arr = {1, 3, 2, 4, 5};
        Sort sort = new Sort(arr);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] result = sort.getResult();
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void testRun1LengthArray() {
        int[] arr = {1};
        Sort sort = new Sort(arr);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] result = sort.getResult();
        assertArrayEquals(new int[]{1}, result);
    }

    @Test
    void testRunEmptyArray() {
        int[] arr = {};
        Sort sort = new Sort(arr);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] result = sort.getResult();
        assertArrayEquals(new int[]{}, result);
    }

    @Test
    void testRunAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        Sort sort = new Sort(arr);
        sort.start();
        try {
            sort.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int[] result = sort.getResult();
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }
}