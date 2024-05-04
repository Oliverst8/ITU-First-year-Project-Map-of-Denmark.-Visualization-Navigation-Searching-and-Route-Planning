package dk.itu.map.structures;

import dk.itu.map.structures.ArrayLists.CoordArrayList;
import dk.itu.map.structures.ArrayLists.IntArrayList;

public class TwoDTree extends CoordArrayList {

    public TwoDTree() {
        super();
    }

    public TwoDTree(int length) {
        super(length);
    }

    public TwoDTree(float[] array){
        super(array);
    }

    private boolean shouldGoRight(float[] self, float[] other, int axis) {
        int cmp = Float.compare(self[axis], other[axis]);
        return cmp >= 0;
    }

    private float distance(float[] self, float[] other) {
        return (float) Math.sqrt(Math.pow(self[0] - other[0], 2) + Math.pow(self[1] - other[1], 2));
    }

    private float distToLine(float[] self, float[] other, int axis) {
        return Math.abs(self[axis] - other[axis]);
    }

    private int validatePosition(int index) {
        if(index >= size() || get(index)[0] == -1) return -1;
        return index;
    }

    private int getLeftChild(int i) {
        return validatePosition(2 * i + 1);
    }

    private int getRightChild(int i) {
        return validatePosition(2 * i + 2);
    }

    public int nearestNeighbour(float[] point, int vehicleCode, Graph graph) {
        return nearest(point, 0, 0, 0, vehicleCode, graph);
    }

    private int nearest(float[] goal, int currentNode, int best, int axis, int vehicleCode, Graph graph) {

        if(goal == get(currentNode)) return currentNode;

        boolean shouldGoRight = shouldGoRight(goal, get(currentNode), axis);

        int rightChild = getRightChild(currentNode);
        int leftChild = getLeftChild(currentNode);

        int child = shouldGoRight ? rightChild : leftChild;

        if(child == -1){
            child = shouldGoRight ? leftChild : rightChild;
            if(child == -1) return currentNode;
        }

        int nextCheck = nearest(goal, child, best, (axis + 1) % 2, vehicleCode, graph);
        boolean canBeUsed = graph.canThisBeDrivenOn(nextCheck, vehicleCode);
        if(canBeUsed){
            best = distance(goal, get(best)) > distance(goal, get(nextCheck)) ? nextCheck : best;
        }

        if(child == rightChild){
            if(leftChild == -1) return best;
            float rPrime = distToLine(goal, get(currentNode), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, leftChild, best, (axis + 1) % 2, vehicleCode, graph);
        } else {
            if(rightChild == -1) return best;
            float rPrime = distToLine(goal, get(currentNode), axis);
            if(distance(goal, get(best)) > rPrime) best = nearest(goal, rightChild, best, (axis + 1) % 2, vehicleCode, graph);
        }
        return best;
    }
}
