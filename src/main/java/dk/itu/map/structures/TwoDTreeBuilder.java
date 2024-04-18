package dk.itu.map.structures;

import dk.itu.map.parser.GraphBuilder;
import dk.itu.map.structures.ArrayLists.CoordArrayList;

import dk.itu.map.utility.Sort;

import java.util.Arrays;
import java.util.HashSet;


public class TwoDTreeBuilder {

    int tempCounterPartetion = 0;
    int tempCounterNotEqual = 0;
    private int[][] sortedIndexes;

    private CoordArrayList coords;

    private int[] tree;

    public TwoDTreeBuilder(CoordArrayList coords) {
        this.coords = coords;
        this.sortedIndexes = new int[2][];
        int h = (int) (Math.log(coords.size())/Math.log(2)+ 1);
        int size = (int) Math.pow(2, h) - 1;
        tree = new int[size];
    }

    private void setLeftChild(int parent, int child) {
        int childIndex = parent * 2 + 1;
        if(childIndex == 9){
            System.out.println("Right child is -1");
        }
        if(childIndex >= tree.length && child == -1) return;
        if(coords.get(child)[0] == 54.145023f && coords.get(child)[1] == -4.5019617f){
            System.out.println("left child is 3");
        }
        if(tree[childIndex] != 0) throw new IllegalArgumentException("Child already exists on index: " + childIndex + " value: " + tree[childIndex] + " \nvalue of parent: " + parent + " \nvalue of child: " + child);
        tree[childIndex] = child;
    }

    private void setRightChild(int parent, int child) {
        int childIndex = parent * 2 + 2;
        if(childIndex == 9){
            System.out.println("Right child is -1");
        }
        if(childIndex >= tree.length && child == -1) return;
        if(tree[childIndex] != 0) throw new IllegalArgumentException("Child already exists on index: " + childIndex + " value: " + tree[childIndex] + " \nvalue of parent: " + parent + " \nvalue of child: " + child);
        if(coords.get(child)[0] == 54.145023f && coords.get(child)[1] == -4.5019617f){
            System.out.println("left child is 3");
        }
        tree[childIndex] = child;
    }

    public void build(){
        PointSort pointX = new PointSort(coords.size(), 0);
        PointSort pointY = new PointSort(coords.size(), 1);
        pointX.start();
        pointY.start();
        try {
            pointX.join();
            pointY.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sortedIndexes[0] = pointX.getSortedIndexes();
        sortedIndexes[1] = pointY.getSortedIndexes();

        while(!checkIfSorted(sortedIndexes[0], 0,  true)){
            System.out.println("X is not sorted: Resorting");
            insertionSort(sortedIndexes[0], 0, coords.size(), 0);
            insertionSort(sortedIndexes[1], 1, coords.size(), 0);
        }
        while(!checkIfSorted(sortedIndexes[1], 1, true)){
            System.out.println("Y is not sorted: Resorting");
            insertionSort(sortedIndexes[0], 0, coords.size(), 0);
            insertionSort(sortedIndexes[1], 1, coords.size(), 0);
        }

        int[][] secondArray = new int[2][coords.size()];

        for(int i = 0; i < sortedIndexes.length; i++){
            for(int j = 0; j < sortedIndexes[i].length; j++){
                secondArray[i][j] = sortedIndexes[i][j];
            }
        }

        tree[0] = partition(0, coords.size(), 0, 0, sortedIndexes, secondArray);

    }

    public static int getIndexOf(int[] array, int value){
        for(int i = 0; i < array.length; i++){
            if(array[i] == value) return i;
        }
        return -1;
    }

    public boolean checkIfSorted(int[] array, int axis, boolean isSorted){
        for(int i = 0; i < array.length - 1; i++){
            if(compare(array[i] , array[i + 1], axis, isSorted) > 0) return false;
        }
        return true;
    }

    public boolean checkIfSorted(int[] array, int axis, int range, int start){
        for(int i = start; i < start + range - 1; i++){
            if(compare(array[i] , array[i + 1], axis) >= 0) return false;
        }
        return true;
    }

    public void insertionSort(int[] array, int axis, int range, int start){
        for(int i = start; i < start + range; i++){
            int j = i;
            while(j > start && compare(array[j - 1], array[j], axis) > 0){
                int temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
                j--;
            }
        }
    }

    public static int[] buildArrayFromRange(int[] array, int start, int range) {
        int[] newArray = new int[range];
        for(int i = 0; i < range; i++) {
            newArray[i] = array[start + i];
        }
        return newArray;
    }

    private int getLeftChildIndex(int parentIndex){
        return parentIndex * 2 + 1;
    }

    private int getRightChildIndex(int parentIndex){
        return parentIndex * 2 + 2;
    }

    public static boolean arrayEquals(int[] array1, int[] array2, int start, int range){
        int[] array3 = buildArrayFromRange(array1, start, range);
        int[] array4 = buildArrayFromRange(array2, start, range);
        HashSet<Integer> one = GraphBuilder.getDifference(array3, array4);
        HashSet<Integer> two = GraphBuilder.getDifference(array4, array3);
        if(one.isEmpty() && two.isEmpty()){
            return true;
        }
        return false;
    }

    public int partition(int start, int range, int rootIndex, int primaryAxis, int[][] readArray, int[][] writeArray) {

        tempCounterPartetion++;

        int medianIndex = start + (range - 1) / 2;
        int median = readArray[primaryAxis][medianIndex];

        int leftChildIndex = rootIndex * 2 + 1;
        int rightChildIndex = rootIndex * 2 + 2;
        int rangeLeft = (range - 1) / 2;
        int rangeRight = (int) Math.ceil((range - 1) / 2.0);

        int leftStart = start;
        int rightStart = medianIndex + 1;

        int secondaryAxis = (primaryAxis + 1) % 2;


        if(range == 1) {
            setLeftChild(rootIndex, -1);
            setRightChild(rootIndex, -1);
            return median;
        }

        else if(range == 2){
            //Hvordan er vi sikre op at den skal være til højre?
            //setLeftChild(rootIndex, -1);
            //setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,secondaryAxis, writeArray, readArray));
                //setLeftChild(rightChildIndex, -1);
                //setRightChild(rightChildIndex, -1);
            int otherIndex = readArray[primaryAxis][medianIndex+1];
            if(compare(medianIndex, medianIndex+1, secondaryAxis) > 0){
                setRightChild(rootIndex, -1);
                //setLeftChild(rootIndex, partition(medianIndex + 1,rangeRight,leftChildIndex,secondaryAxis, writeArray, readArray));
                setLeftChild(rootIndex, otherIndex);
                int leftChildIndexOfChild = getLeftChildIndex(rootIndex);
                setLeftChild(leftChildIndexOfChild, -1);
                setRightChild(leftChildIndexOfChild, -1);
            } else {
                setLeftChild(rootIndex, -1);
                //setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,secondaryAxis, writeArray, readArray));
                setRightChild(rootIndex, otherIndex);
                int rightChildIndexOfChild = getRightChildIndex(rootIndex);
                setLeftChild(rightChildIndexOfChild, -1);
                setRightChild(rightChildIndexOfChild, -1);
            }
            return median;
        }
        for(int i = start; i < start + range; i++) {
            int index = readArray[secondaryAxis][i]; //i = 7 er hvor den fejler
            writeArray[primaryAxis][i] = readArray[primaryAxis][i];
            if(index == median){
                //writeArray[secondaryAxis][medianIndex] = index;
                continue;
            }

            int cmp = compare(index, median, primaryAxis);
            if(cmp > 0) {
                writeArray[secondaryAxis][rightStart++] = index;
            } else if(cmp < 0) {
                writeArray[secondaryAxis][leftStart++] = index;
            }
        }


        primaryAxis = secondaryAxis;

        setLeftChild(rootIndex, partition(start,rangeLeft,leftChildIndex,primaryAxis, writeArray, readArray));
        setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,primaryAxis, writeArray, readArray));

        return median;

    }

    private int compare(int index1, int index2, int primaryAxis) {
        return compare(index1, index2, primaryAxis, true);
    }

    private int compare(int index1, int index2, int primaryAxis, boolean shouldChange) {

        float[] coord1 = coords.get(index1);
        float[] coord2 = coords.get(index2);
        int cmp = Float.compare(coord1[primaryAxis], coord2[primaryAxis]);
        if(cmp == 0) {
            cmp = Float.compare(coord1[(primaryAxis + 1) % 2], coord2[(primaryAxis + 1) % 2]);
        }
        if(cmp == 0){
            if(shouldChange) {
                float temp = Math.nextAfter(coord2[1], Double.POSITIVE_INFINITY);
                coords.set(index2, new float[]{coord2[0], temp});

                /*if(temp != coords.get(index2)[1]){
                    System.out.println();
                }*/
            } else{
                throw new RuntimeException("Two points are equal");
            }
            cmp = -1;
        }
        return cmp;
    }

    public int[] getTree(){
        return tree;
    }

    private class PointSort extends Sort {
        private int[] array;
        private int primaryAxis;
        public PointSort(int size, int primaryAxis) {
            super(new int[]{});
            this.array = new int[size];
            this.primaryAxis = primaryAxis;
        }

        @Override
        protected int compare(int i, int j){
            return TwoDTreeBuilder.this.compare(i, j, primaryAxis);
        }

        @Override
        public void run(){
            for(int i = 0; i < array.length; i++){
                array[i] = i;
            }
            arrayToSort = array;
            super.run();
        }


        public int[] getSortedIndexes(){
            return super.getResult();
        }
    }

}
