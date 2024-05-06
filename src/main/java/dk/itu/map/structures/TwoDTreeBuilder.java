package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.utility.Sort;

public class TwoDTreeBuilder {

    private int[][] sortedIndexes; //Array of the indexs in sorted order (pos 0 sorted by x, pos 1 sorted by y)

    private CoordArrayList coords; //Array of the coordinates

    private int[] tree; //Array of the tree

    /**
     * Constructor for the TwoDTreeBuilder
     * @param coords The coordinates to build the tree from
     */
    public TwoDTreeBuilder(CoordArrayList coords) {
        this.coords = coords;
        this.sortedIndexes = new int[2][];
        int h = (int) (Math.log(coords.size())/Math.log(2)+ 1);
        int size = (int) Math.pow(2, h) - 1;
        tree = new int[size];
    }

    /**
     * Sets the left child of a parent
     * @param parent The parent position
     * @param child The child value
     */
    private void setLeftChild(int parent, int child) {
        int childIndex = parent * 2 + 1;
        if(childIndex >= tree.length && child == -1) return;
        tree[childIndex] = child;
    }

    /**
     * Sets the right child of a parent
     * @param parent The parent position
     * @param child The child value
     */
    private void setRightChild(int parent, int child) {
        int childIndex = parent * 2 + 2;
        if(childIndex >= tree.length && child == -1) return;
        tree[childIndex] = child;
    }

    /**
     * Builds the tree
     */
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

    /**
     * Checks if an array is sorted
     * @param array The array to check
     * @param axis The axis to check on
     * @param isSorted If it can increment value, if two points are equal
     * @return If the array is sorted
     */
    public boolean checkIfSorted(int[] array, int axis, boolean isSorted){
        for(int i = 0; i < array.length - 1; i++){
            if(compare(array[i] , array[i + 1], axis, isSorted) > 0) return false;
        }
        return true;
    }

    /**
     * Sorts an array using insertion sort
     * @param array The array to sort
     * @param axis The axis to sort on
     * @param range The range to sort
     * @param start The start index
     */
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

    /**
     * Gets the left child index of a parent
     * @param parentIndex The parent index
     * @return The left child index
     */
    private int getLeftChildIndex(int parentIndex){
        return parentIndex * 2 + 1;
    }

    /**
     * Gets the right child index of a parent
     * @param parentIndex The parent index
     * @return The right child index
     */
    private int getRightChildIndex(int parentIndex){
        return parentIndex * 2 + 2;
    }


    /**
     * Partitions and builds the tree
     * @param start The start index of the array
     * @param range The range of the array to be build from
     * @param rootIndex The root index the new part of the tree is búild from
     * @param primaryAxis The primary axis to build the tree from
     * @param readArray The array to read from
     * @param writeArray The array to write to
     * @return the root of the tree
     */
    public int partition(int start, int range, int rootIndex, int primaryAxis, int[][] readArray, int[][] writeArray) {

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
            } else {
                writeArray[secondaryAxis][leftStart++] = index;
            }
        }


        primaryAxis = secondaryAxis;

        setLeftChild(rootIndex, partition(start,rangeLeft,leftChildIndex,primaryAxis, writeArray, readArray));
        setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,primaryAxis, writeArray, readArray));

        return median;

    }

    /**
     * Compares two points (If two points are equal it increments one)
     * @param index1 The index of the first point
     * @param index2 The index of the second point
     * @param primaryAxis The primary axis to compare on
     * @return The comparison value
     */
    private int compare(int index1, int index2, int primaryAxis) {
        return compare(index1, index2, primaryAxis, true);
    }

    /**
     * Compares two points
     * @param index1 The index of the first point
     * @param index2 The index of the second point
     * @param primaryAxis The primary axis to compare on
     * @param shouldChange If two points are equal, should it increment one
     * @return The comparison value
     */
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
            } else{
                throw new RuntimeException("Two points are equal");
            }
            cmp = -1;
        }
        return cmp;
    }

    /**
     * @return The tree
     */
    public int[] getTree(){
        return tree;
    }

    private class PointSort extends Sort {
        private int[] array; //Array to sort
        private int primaryAxis; //The primary axis to sort on

        /**
         * Constructor for the PointSort
         * @param size The size of the array
         * @param primaryAxis The primary axis to sort on
         */
        public PointSort(int size, int primaryAxis) {
            super(new int[]{});
            this.array = new int[size];
            this.primaryAxis = primaryAxis;
        }

        /**
         * Compares two points
         * @param i The index of the first point
         * @param j The index of the second point
         * @return The comparison value
         */
        @Override
        protected int compare(int i, int j){
            return TwoDTreeBuilder.this.compare(i, j, primaryAxis);
        }

        /**
         * Runs the sorting
         */
        @Override
        public void run(){
            for(int i = 0; i < array.length; i++){
                array[i] = i;
            }
            arrayToSort = array;
            super.run();
        }

        /**
         * @return the sorted array
         */
        public int[] getSortedIndexes(){
            return super.getResult();
        }
    }

}
