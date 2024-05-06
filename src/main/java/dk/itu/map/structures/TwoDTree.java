package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;

import java.util.Arrays;

public class TwoDTree extends CoordArrayList {

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public TwoDTree() {
        super();
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     * @param length the initial capacity of the list
     */
    public TwoDTree(int length) {
        super(length);
    }

    /**
     * Constructs a list containing the elements of the specified array.
     * @param array the array whose elements are to be placed into this list
     */
    public TwoDTree(float[] array){
        super(array);
    }

    /**
     * Weather or not you should go left or right when running nearest neighbor search
     * @param self the point you are looking for the nearest neighbor to
     * @param other the point you are comparing to
     * @param axis the axis you are comparing on
     * @return true if you should go right, false if you should go left
     */
    private boolean shouldGoRight(float[] self, float[] other, int axis) {
        int cmp = Float.compare(self[axis], other[axis]);
        return cmp >= 0;
    }

    /**
     * Calculates the distance between two points
     * @param self the first point
     * @param other the second point
     * @return the distance between the two points
     */
    private float distance(float[] self, float[] other) {
        return (float) Math.sqrt(Math.pow(self[0] - other[0], 2) + Math.pow(self[1] - other[1], 2));
    }

    /**
     * Calculates the distance between a point and a line
     * @param self the point
     * @param other the line
     * @param axis the axis the line is on
     * @return the distance between the point and the line
     */
    private float distToLine(float[] self, float[] other, int axis) {
        return Math.abs(self[axis] - other[axis]);
    }

    /**
     * Validates a position in the list
     * @param index the index to validate
     * @return the index if it is valid, -1 otherwise
     */
    private int validatePosition(int index) {
        if(index >= size() || get(index)[0] == -1) return -1;
        return index;
    }

    /**
     * Gets the left child of a node
     * @param i the index of the node
     * @return the index of the left child
     */
    private int getLeftChild(int i) {
        return validatePosition(2 * i + 1);
    }

    /**
     * Gets the right child of a node
     * @param i the index of the node
     * @return the index of the right child
     */
    private int getRightChild(int i) {
        return validatePosition(2 * i + 2);
    }

    /**
     * Finds the nearest neighbor to a point
     * @param point the point to find the nearest neighbor to
     * @param vehicleCode the vehicle code of the vehicle
     * @param graph the graph to that vehicle codes can be used on
     * @return the index of the nearest neighbor
     */
    public int nearestNeighbour(float[] point, int vehicleCode, Graph graph) {
        return nearest(point, 0, 0, 0, vehicleCode, graph);
    }

    /**
     * Finds the nearest neighbor to a point
     * @param goal THe point youre loooking for the neighbor to
     * @param i the index of the current node
     * @param best the index of the best node so far
     * @param axis the axis to compare on
     * @param vehicleCode the vehicle code of the vehicle
     * @param graph the graph that vehicle codes can be used on
     * @return the index of the nearest neighbor
     */
    private int nearest(float[] goal, int i, int best, int axis, int vehicleCode, Graph graph) {

        if(Arrays.equals(goal, get(i))) return i;

        boolean shouldGoRight = shouldGoRight(goal, get(i), axis);

        int rightChild = getRightChild(i);
        int leftChild = getLeftChild(i);

        int child = shouldGoRight ? rightChild : leftChild;

        if(child == -1){
            child = shouldGoRight ? leftChild : rightChild;
            if(child == -1) return i;
        }

        int nextCheck = nearest(goal, child, best, (axis + 1) % 2, vehicleCode, graph);
        IntArrayList edges = graph.getEdgeList(nextCheck);
        boolean canBeUsed = false;
        for(int j = 0; j < edges.size(); j++){
            int edge = edges.get(j);
            if((graph.getVehicleRestrictions().get(edge) & vehicleCode) > 0 ){
                canBeUsed = true;
            }
        }
        if(canBeUsed){
            best = distance(goal, get(best)) > distance(goal, get(nextCheck)) ? nextCheck : best;
        }

        if(child == rightChild){
            if(leftChild == -1) return best;
            float rPrime = distToLine(goal, get(i), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, leftChild, best, (axis + 1) % 2, vehicleCode, graph);
        } else {
            if(rightChild == -1) return best;
            float rPrime = distToLine(goal, get(i), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, rightChild, best, (axis + 1) % 2, vehicleCode, graph);
        }
        return best;
    }


/*
    public float[] nearestNeighbour(float[] coords) {
        Queue<Integer> queue = new LinkedList<>();
        float r = distance(coords, get(0));
        float rPrime = distToLine(coords, get(0), 0);
        int nearest = 0;
        int axis = 0;

        queue.add(nearest);


        while(!queue.isEmpty()) {
            int i = queue.poll();
            boolean goRight; // = goRight(coords, get(i), axis);

            int leftChild = getLeftChild(i);
            int rightChild = getRightChild(i);

            if(leftChild == -1 && rightChild == -1) {
                continue;
            } else if(leftChild == -1) {
                goRight = true;
            } else if(rightChild == -1) {
                goRight = false;
            } else {
                goRight = shouldGoRight(coords, get(leftChild), axis);
            }

            if(goRight){


            } else {

            }

            axis = (axis + 1) % 2;
        }



        throw new UnsupportedOperationException("Not implemented yet");
    }
 */

    /**
     * Finds the nearest neighbor to a point
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     * @param vehicleCode the vehicle code of the vehicle
     * @param graph the graph that vehicle codes can be used on
     * @return the index of the nearest neighbor
     */
    public int nearestNeighbour(float x, float y, int vehicleCode, Graph graph) {
        return nearestNeighbour(new float[]{x,y}, vehicleCode, graph);
    }
}
