package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;

import dk.itu.map.utility.Sort;

import java.util.Arrays;


public class TwoDTreeBuilder {

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
        if(childIndex >= tree.length && child == -1) return;
        tree[childIndex] = child;
    }

    private void setRightChild(int parent, int child) {
        int childIndex = parent * 2 + 2;
        if(childIndex >= tree.length && child == -1) return;
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

        int[][] secondArray = Arrays.copyOf(sortedIndexes, sortedIndexes.length);

        tree[0] = partition(0, coords.size(), 0, 0, sortedIndexes, secondArray);

    }

    public int partition(int start, int range, int rootIndex, int primaryAxis, int[][] readArray, int[][] writeArray) {

        int medianIndex = start + (range - 1) / 2;
        int median = readArray[primaryAxis][medianIndex];
        int leftChildIndex = rootIndex * 2 + 1;
        int rightChildIndex = rootIndex * 2 + 2;
        int rangeLeft = (range - 1) / 2;
        int rangeRight = (int) Math.ceil((range - 1) / 2.0);

        int leftStart = start;
        int rightStart = medianIndex + 1;

        for(int i = start; i < start + range; i++) {
            int index = readArray[(primaryAxis+1)%2][i];
            if(index == median) continue;
            int cmp = compare(index, median, primaryAxis);
            if(cmp > 0) {
                writeArray[(primaryAxis+1)%2][rightStart++] = index;
            } else if(cmp < 0) {
                writeArray[(primaryAxis+1)%2][leftStart++] = index;
            }
        }

        primaryAxis = (primaryAxis + 1) % 2;

        if(range == 1) {
            setLeftChild(rootIndex, -1);
            setRightChild(rootIndex, -1);
        }

        else if(range == 2){
            setLeftChild(rootIndex, -1);
            setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,primaryAxis, writeArray, readArray));
        }

        else if(range > 3) {
            setLeftChild(rootIndex, partition(start,rangeLeft,leftChildIndex,primaryAxis, writeArray, readArray));
            setRightChild(rootIndex, partition(medianIndex + 1,rangeRight,rightChildIndex,primaryAxis, writeArray, readArray));
        }

        return median;

    }

    private int compare(int index1, int index2, int primaryAxis) {
        float[] coord1 = coords.get(index1);
        float[] coord2 = coords.get(index2);
        int cmp = Float.compare(coord1[primaryAxis], coord2[primaryAxis]);
        if(cmp == 0) {
            cmp = Float.compare(coord1[(primaryAxis + 1) % 2], coord2[(primaryAxis + 1) % 2]);
        }
        if(cmp == 0) throw new RuntimeException("Two points are the same\n x_1:" + coord1[0] + " y_1:" + coord1[1] + "\n x_2:" + coord2[0] + " y_2:" + coord2[1]);
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
